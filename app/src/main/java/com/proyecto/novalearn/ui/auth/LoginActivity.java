package com.proyecto.novalearn.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.jwt.JWT;
import com.auth0.android.result.Credentials;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.proyecto.novalearn.MainActivity;
import com.proyecto.novalearn.R;
import com.proyecto.novalearn.auth.Auth0Manager;
import com.proyecto.novalearn.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private AuthenticationAPIClient authClient;
    private SessionManager sessionManager;

    private TabLayout tabLayout;
    private TextInputEditText etEmail, etPassword, etNombre;
    private TextInputLayout tilPassword, tilNombre;
    private MaterialButton btnAcceder;
    private ProgressBar progressBar;
    private TextView tvPasswordHint;

    private boolean esRegistro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Auth0 auth0 = Auth0Manager.getAuth0(this);
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
        tilPassword = findViewById(R.id.tilPassword);
        tilNombre = findViewById(R.id.tilNombre);
        btnAcceder = findViewById(R.id.btnAcceder);
        progressBar = findViewById(R.id.progressBar);
        tvPasswordHint = findViewById(R.id.tvPasswordHint);

        tabLayout.addTab(tabLayout.newTab().setText("Iniciar Sesión"));
        tabLayout.addTab(tabLayout.newTab().setText("Registrarse"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                esRegistro = tab.getPosition() == 1;
                tilNombre.setVisibility(esRegistro ? View.VISIBLE : View.GONE);
                tvPasswordHint.setVisibility(esRegistro ? View.VISIBLE : View.GONE);
                tilPassword.setError(null);
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
                .validateClaims()
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
                            android.util.Log.e("AUTH0_ERROR", "Code: " + error.getCode());
                            android.util.Log.e("AUTH0_ERROR", "Desc: " + error.getDescription());

                            String msg;
                            if (error.getCode() != null) {
                                switch (error.getCode()) {
                                    case "invalid_user_password":
                                        msg = "Email o contraseña incorrectos";
                                        break;
                                    case "too_many_attempts":
                                        msg = "Demasiados intentos. Espera un momento";
                                        break;
                                    default:
                                        msg = "Error al iniciar sesión";
                                        break;
                                }
                            } else {
                                msg = "Error de conexión";
                            }
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
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

        if (!validarPassword(password)) return;

        mostrarCarga(true);

        authClient.signUp(email, password, nombre, "Username-Password-Authentication")
                .validateClaims()
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
                            android.util.Log.e("AUTH0_ERROR", "Code: " + error.getCode());
                            android.util.Log.e("AUTH0_ERROR", "Desc: " + error.getDescription());

                            String msg;
                            if (error.getCode() != null) {
                                switch (error.getCode()) {
                                    case "user_exists":
                                    case "username already exists":
                                        msg = "Este email ya está registrado";
                                        break;
                                    case "invalid_password":
                                        msg = "La contraseña no cumple los requisitos";
                                        break;
                                    case "invalid_signup":
                                        msg = "Datos de registro inválidos";
                                        break;
                                    default:
                                        msg = "Error al registrar: " + error.getDescription();
                                        break;
                                }
                            } else {
                                msg = "Error de conexión";
                            }
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                        });
                    }
                });
    }

    private boolean validarPassword(String password) {
        if (password.length() < 8) {
            tilPassword.setError("Mínimo 8 caracteres");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            tilPassword.setError("Debe tener al menos una mayúscula");
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            tilPassword.setError("Debe tener al menos un número");
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) {
            tilPassword.setError("Debe tener al menos un carácter especial (!@#$...)");
            return false;
        }
        tilPassword.setError(null);
        return true;
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