package com.proyecto.novalearn.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.proyecto.novalearn.R;
import com.proyecto.novalearn.auth.Auth0Manager;
import com.proyecto.novalearn.ui.auth.LoginActivity;
import com.proyecto.novalearn.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        TextInputEditText etNuevoNombre = view.findViewById(R.id.etNuevoNombre);
        TextInputEditText etEmail = view.findViewById(R.id.etEmail);

        etNuevoNombre.setText(sessionManager.getNombre());
        etEmail.setText(sessionManager.getEmail());

        CircleImageView imgFoto = view.findViewById(R.id.imgFoto);
        String foto = sessionManager.getFoto();
        if (foto != null && !foto.isEmpty()) {
            Glide.with(this).load(foto).into(imgFoto);
        }

        // Guardar nombre
        view.findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            String nuevoNombre = etNuevoNombre.getText().toString().trim();
            if (nuevoNombre.isEmpty()) {
                Toast.makeText(requireContext(),
                        "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            sessionManager.guardarSesion(
                    sessionManager.getEmail(),
                    nuevoNombre,
                    sessionManager.getFoto(),
                    sessionManager.getToken()
            );
            Toast.makeText(requireContext(),
                    "Nombre actualizado", Toast.LENGTH_SHORT).show();
        });

        // Cambiar contraseña — Auth0 envía correo
        view.findViewById(R.id.btnCambiarPassword).setOnClickListener(v -> {
            Auth0 auth0 = Auth0Manager.getAuth0(requireContext());
            AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);

            client.resetPassword(sessionManager.getEmail(),
                            "Username-Password-Authentication")
                    .start(new Callback<Void, AuthenticationException>() {
                        @Override
                        public void onSuccess(Void result) {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(),
                                            "Te enviamos un correo para cambiar tu contraseña",
                                            Toast.LENGTH_LONG).show());
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(),
                                            "Error al enviar correo", Toast.LENGTH_SHORT).show());
                        }
                    });
        });

        // Logout
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Auth0Manager.getInstance(requireContext()).logout(requireActivity(),
                    new Callback<Void, AuthenticationException>() {
                        @Override
                        public void onSuccess(Void result) {
                            sessionManager.cerrarSesion();
                            requireActivity().runOnUiThread(() -> {
                                Intent intent = new Intent(requireContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            });
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(),
                                            "Error al cerrar sesión", Toast.LENGTH_SHORT).show());
                        }
                    });
        });
    }
}