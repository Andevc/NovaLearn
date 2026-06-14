package com.proyecto.novalearn;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.novalearn.data.Curso;
import com.proyecto.novalearn.data.DBHelper;
import com.proyecto.novalearn.ui.auth.LoginActivity;
import com.proyecto.novalearn.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // Redirigir a Login si no hay sesión activa
        if (!sessionManager.haySesion()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        // Solo cargar cursos si la BD está vacía (evita duplicados al cerrar/abrir sesión)
        if (!dbHelper.tieneCursos()) {
            cargarCursosDesdeJson();
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    private void cargarCursosDesdeJson() {
        try {
            InputStream is = getAssets().open("cursos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Curso curso = new Curso(
                        0,
                        obj.getString("nombre"),
                        obj.getString("descripcion"),
                        obj.getString("instructor"),
                        obj.getString("duracion"),
                        obj.getString("categoria")
                );
                dbHelper.insertarCurso(curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}