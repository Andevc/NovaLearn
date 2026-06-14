package com.proyecto.novalearn.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.novalearn.R;
import com.proyecto.novalearn.data.Curso;
import com.proyecto.novalearn.data.DBHelper;
import com.proyecto.novalearn.ui.detail.CourseDetailActivity;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private DBHelper dbHelper;
    private CursoAdapter adapter;
    private List<Curso> listaCursos;

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
            dbHelper = new DBHelper(requireContext());
            listaCursos = dbHelper.obtenerCursos();
            Log.d(TAG, "Cursos cargados: " + listaCursos.size());

            RecyclerView rv = view.findViewById(R.id.rvCursos);
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));

            adapter = new CursoAdapter(listaCursos, curso -> {
                try {
                    Log.d(TAG, "Click en curso id=" + curso.getId() + " nombre=" + curso.getNombre());
                    Intent intent = new Intent(requireContext(), CourseDetailActivity.class);
                    intent.putExtra("curso_id", curso.getId());
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error al abrir detalle del curso", e);
                }
            });
            rv.setAdapter(adapter);

            SearchView searchView = view.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) { return false; }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filtrar(newText);
                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error en onViewCreated", e);
        }
    }
}