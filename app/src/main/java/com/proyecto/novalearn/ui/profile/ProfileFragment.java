package com.proyecto.novalearn.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.bumptech.glide.Glide;
import com.proyecto.novalearn.R;
import com.proyecto.novalearn.auth.Auth0Manager;
import com.proyecto.novalearn.ui.auth.LoginActivity;
import com.proyecto.novalearn.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

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

        SessionManager sessionManager = new SessionManager(requireContext());

        ((TextView) view.findViewById(R.id.tvNombre)).setText(sessionManager.getNombre());
        ((TextView) view.findViewById(R.id.tvEmail)).setText(sessionManager.getEmail());

        CircleImageView imgFoto = view.findViewById(R.id.imgFoto);
        String foto = sessionManager.getFoto();
        if (foto != null && !foto.isEmpty()) {
            Glide.with(this).load(foto).into(imgFoto);
        }

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Auth0Manager.getInstance(requireContext()).logout(requireActivity(),
                    new Callback<Void, AuthenticationException>() {
                        @Override
                        public void onSuccess(Void result) {
                            sessionManager.cerrarSesion();
                            requireActivity().runOnUiThread(() -> {
                                Intent intent = new Intent(requireContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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