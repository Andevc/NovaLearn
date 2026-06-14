package com.proyecto.novalearn.ui.mycourses;

import android.os.Bundle;
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
import com.proyecto.novalearn.utils.SessionManager;

import java.util.List;

public class MyCoursesFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvEmpty;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
        tvEmpty = view.findViewById(R.id.tvEmpty);
        rv = view.findViewById(R.id.rvMisCursos);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        cargarCursos();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarCursos();
    }

    private void cargarCursos() {
        List<Curso> misCursos = dbHelper.obtenerCursosInscritos(sessionManager.getEmail());
        if (misCursos.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(new MisCursosAdapter(misCursos));
        }
    }
}