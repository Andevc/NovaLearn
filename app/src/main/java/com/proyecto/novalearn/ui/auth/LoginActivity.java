package com.proyecto.novalearn.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.jwt.JWT;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.DatabaseUser;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.proyecto.novalearn.MainActivity;
import com.proyecto.novalearn.R;
import com.proyecto.novalearn.auth.Auth0Manager;
import com.proyecto.novalearn.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private AuthenticationAPIClient authClient;
    private SessionManager sessionManager;
    private Auth0 auth0;

    private TabLayout tabLayout;
    private TextInputEditText etEmail, etPassword, etNombre;
    private TextInputLayout tilNombre;
    private MaterialButton btnAcceder;
    private ProgressBar progressBar;

    private boolean esRegistro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth0 = Auth0Manager.getAuth0(this);
        authClient = new AuthenticationAPIClient(auth0);
        sessionManager = new SessionManager(this);

        if (sessionManager.haySesion()) {
            irAMain();
            return;
        }

        tabLayout = findViewById(R.id.tabLayout);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etNombre = findViewById(R.id.etNombre);
        tilNombre = findViewById(R.id.tilNombre);
        btnAcceder = findViewById(R.id.btnAcceder);
        progressBar = findViewById(R.id.progressBar);

        tabLayout.addTab(tabLayout.newTab().setText("Iniciar Sesión"));
        tabLayout.addTab(tabLayout.newTab().setText("Registrarse"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                esRegistro = tab.getPosition() == 1;
                tilNombre.setVisibility(esRegistro ? View.VISIBLE : View.GONE);
                btnAcceder.setText(esRegistro ? "Crear Cuenta" : "Iniciar Sesión");
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        btnAcceder.setOnClickListener(v -> {
            if (esRegistro) registrar();
            else login();
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCarga(true);

        authClient.login(email, password, "Username-Password-Authentication")
                .setScope("openid profile email")
                .start(new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        procesarCredenciales(credentials);
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        runOnUiThread(() -> {
                            mostrarCarga(false);
                            Toast.makeText(LoginActivity.this,
                                    "Email o contraseña incorrectos",
                                    Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    private void registrar() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCarga(true);

        authClient.createUser(email, password, nombre, "Username-Password-Authentication")
                .start(new Callback<DatabaseUser, AuthenticationException>() {
                    @Override
                    public void onSuccess(DatabaseUser user) {
                        // Después de registrar, hacer login automático
                        authClient.login(email, password, "Username-Password-Authentication")
                                .setScope("openid profile email")
                                .start(new Callback<Credentials, AuthenticationException>() {
                                    @Override
                                    public void onSuccess(Credentials credentials) {
                                        procesarCredenciales(credentials);
                                    }

                                    @Override
                                    public void onFailure(AuthenticationException error) {
                                        runOnUiThread(() -> {
                                            mostrarCarga(false);
                                            Toast.makeText(LoginActivity.this,
                                                    "Cuenta creada. Inicia sesión.",
                                                    Toast.LENGTH_SHORT).show();
                                            tabLayout.getTabAt(0).select();
                                        });
                                    }
                                });
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        runOnUiThread(() -> {
                            mostrarCarga(false);
                            Toast.makeText(LoginActivity.this,
                                    "Error al registrar: " + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    private void procesarCredenciales(Credentials credentials) {
        try {
            JWT jwt = new JWT(credentials.getIdToken());
            String email = jwt.getClaim("email").asString();
            String nombre = jwt.getClaim("name").asString();
            String foto = jwt.getClaim("picture").asString();

            if (email == null) email = "usuario@novalearn.com";
            if (nombre == null) nombre = "Usuario";
            if (foto == null) foto = "";

            sessionManager.guardarSesion(email, nombre, foto, credentials.getAccessToken());
            runOnUiThread(this::irAMain);

        } catch (Exception e) {
            runOnUiThread(() -> {
                mostrarCarga(false);
                Toast.makeText(LoginActivity.this,
                        "Error al procesar sesión", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void mostrarCarga(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        btnAcceder.setEnabled(!mostrar);
    }

    private void irAMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}