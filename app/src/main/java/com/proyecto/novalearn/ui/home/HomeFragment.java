package com.proyecto.novalearn.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.novalearn.R;
import com.proyecto.novalearn.data.Curso;
import com.proyecto.novalearn.data.DBHelper;
import com.proyecto.novalearn.ui.detail.CourseDetailActivity;
import com.proyecto.novalearn.utils.SessionManager;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Nombre del usuario en el saludo
            SessionManager sessionManager = new SessionManager(requireContext());
            String nombre = sessionManager.getNombre();
            ((TextView) view.findViewById(R.id.tvHola)).setText("Hola, " + nombre);

            DBHelper dbHelper = new DBHelper(requireContext());
            List<Curso> listaCursos = dbHelper.obtenerCursos();
            Log.d(TAG, "Cursos cargados: " + listaCursos.size());

            RecyclerView rv = view.findViewById(R.id.rvCursos);
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));

            CursoAdapter adapter = new CursoAdapter(listaCursos, curso -> {
                try {
                    Intent intent = new Intent(requireContext(), CourseDetailActivity.class);
                    intent.putExtra("curso_id", curso.getId());
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error al abrir detalle del curso", e);
                }
            });
            rv.setAdapter(adapter);

        } catch (Exception e) {
            Log.e(TAG, "Error en onViewCreated", e);
        }
    }
}