package com.proyecto.novalearn.ui.detail;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
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
import com.proyecto.novalearn.utils.SessionManager;

public class CourseDetailActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private Curso curso;
    private MaterialButton btnInscribirse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int cursoId = getIntent().getIntExtra("curso_id", -1);
        curso = dbHelper.obtenerCursoPorId(cursoId);

        if (curso == null) { finish(); return; }

        getSupportActionBar().setTitle(curso.getNombre());

        ((TextView) findViewById(R.id.tvNombre)).setText(curso.getNombre());
        ((TextView) findViewById(R.id.tvDescripcion)).setText(curso.getDescripcion());
        ((TextView) findViewById(R.id.tvInstructor)).setText("👤 " + curso.getInstructor());
        ((TextView) findViewById(R.id.tvDuracion)).setText("⏱ " + curso.getDuracion());
        ((TextView) findViewById(R.id.tvCategoria)).setText(curso.getCategoria().toUpperCase());

        RecyclerView rvLecciones = findViewById(R.id.rvLecciones);
        rvLecciones.setLayoutManager(new LinearLayoutManager(this));

        btnInscribirse = findViewById(R.id.btnInscribirse);
        actualizarBoton();

        btnInscribirse.setOnClickListener(v -> inscribirse());
    }

    private void actualizarBoton() {
        String email = sessionManager.getEmail();
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
        String email = sessionManager.getEmail();
        dbHelper.inscribir(email, curso.getId());
        actualizarBoton();
        Toast.makeText(this, "¡Inscripción exitosa!", Toast.LENGTH_SHORT).show();
        enviarNotificacion();
    }

    private void enviarNotificacion() {
        String channelId = "novalearn_channel";
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "NovaLearn", NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("¡Inscripción exitosa!")
                .setContentText("Te inscribiste a: " + curso.getNombre())
                .setAutoCancel(true);

        nm.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}