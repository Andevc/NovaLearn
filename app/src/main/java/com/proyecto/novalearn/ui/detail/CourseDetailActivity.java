package com.proyecto.novalearn.ui.detail;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.proyecto.novalearn.R;
import com.proyecto.novalearn.data.Curso;
import com.proyecto.novalearn.data.DBHelper;
import com.proyecto.novalearn.data.Leccion;
import com.proyecto.novalearn.utils.SessionManager;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private static final String TAG = "CourseDetailActivity";

    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private Curso curso;
    private MaterialButton btnInscribirse;
    private TextView tvProgreso;
    private ProgressBar progressCurso;
    private List<Leccion> lecciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_course_detail);

            dbHelper      = new DBHelper(this);
            sessionManager = new SessionManager(this);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            int cursoId = getIntent().getIntExtra("curso_id", -1);
            curso = dbHelper.obtenerCursoPorId(cursoId);

            if (curso == null) { finish(); return; }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(curso.getNombre());
            }

            ((TextView) findViewById(R.id.tvNombre)).setText(curso.getNombre());
            ((TextView) findViewById(R.id.tvDescripcion)).setText(curso.getDescripcion());
            ((TextView) findViewById(R.id.tvInstructor)).setText("👤 " + curso.getInstructor());
            ((TextView) findViewById(R.id.tvDuracion)).setText("⏱ " + curso.getDuracion());
            ((TextView) findViewById(R.id.tvCategoria)).setText(curso.getCategoria().toUpperCase());

            tvProgreso   = findViewById(R.id.tvProgreso);
            progressCurso = findViewById(R.id.progressCurso);

            lecciones = dbHelper.obtenerLeccionesDeCurso(curso.getId());

            RecyclerView rvLecciones = findViewById(R.id.rvLecciones);
            rvLecciones.setLayoutManager(new LinearLayoutManager(this));
            rvLecciones.setAdapter(new LeccionAdapter(
                    lecciones, curso.getId(), this, this::actualizarProgreso));

            actualizarProgreso();

            btnInscribirse = findViewById(R.id.btnInscribirse);
            actualizarBoton();
            btnInscribirse.setOnClickListener(v -> inscribirse());

        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate", e);
        }
    }

    private void actualizarProgreso() {
        String email   = sessionManager.getEmail();
        int total      = lecciones.size();
        int leidas     = dbHelper.contarLeccionesLeidas(email, curso.getId());
        int porcentaje = total > 0 ? (leidas * 100) / total : 0;

        tvProgreso.setText(leidas + "/" + total + " lecciones completadas");
        progressCurso.setMax(100);
        progressCurso.setProgress(porcentaje);
    }

    private void actualizarBoton() {
        String email   = sessionManager.getEmail();
        boolean inscrito = dbHelper.estaInscrito(email, curso.getId());
        if (inscrito) {
            btnInscribirse.setText("✓ Ya inscrito");
            btnInscribirse.setEnabled(false);
        } else {
            btnInscribirse.setText("Inscribirse al Curso");
            btnInscribirse.setEnabled(true);
        }
    }

    private void inscribirse() {
        dbHelper.inscribir(sessionManager.getEmail(), curso.getId());
        actualizarBoton();
        Toast.makeText(this, "¡Inscripción exitosa!", Toast.LENGTH_SHORT).show();
        enviarNotificacion();
    }

    private void enviarNotificacion() {
        String channelId = "novalearn_channel";
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(
                    channelId, "NovaLearn", NotificationManager.IMPORTANCE_DEFAULT));
        }
        nm.notify((int) System.currentTimeMillis(),
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("¡Inscripción exitosa!")
                        .setContentText("Te inscribiste a: " + curso.getNombre())
                        .setAutoCancel(true).build());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}