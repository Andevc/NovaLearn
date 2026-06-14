package com.proyecto.novalearn.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "novalearn.db";
    private static final int DB_VERSION = 2;

    public static final String TABLE_CURSOS = "cursos";
    public static final String TABLE_INSCRIPCIONES = "inscripciones";
    public static final String TABLE_LECCIONES = "lecciones";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CURSOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "descripcion TEXT," +
                "instructor TEXT," +
                "duracion TEXT," +
                "categoria TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_INSCRIPCIONES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT," +
                "curso_id INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_LECCIONES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "curso_id INTEGER," +
                "titulo TEXT," +
                "contenido TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSCRIPCIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LECCIONES);
        onCreate(db);
    }

    // ── CURSOS ──────────────────────────────────────────

    public long insertarCurso(Curso curso) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", curso.getNombre());
        cv.put("descripcion", curso.getDescripcion());
        cv.put("instructor", curso.getInstructor());
        cv.put("duracion", curso.getDuracion());
        cv.put("categoria", curso.getCategoria());
        long id = db.insert(TABLE_CURSOS, null, cv);
        db.close();
        return id;
    }

    public List<Curso> obtenerCursos() {
        List<Curso> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CURSOS, null);
        while (c.moveToNext()) {
            Curso curso = new Curso(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            );
            lista.add(curso);
        }
        c.close();
        db.close();
        return lista;
    }

    public Curso obtenerCursoPorId(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CURSOS +
                " WHERE id = ?", new String[]{String.valueOf(id)});
        Curso curso = null;
        if (c.moveToFirst()) {
            curso = new Curso(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            );
        }
        c.close();
        db.close();
        return curso;
    }

    public boolean tieneCursos() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CURSOS, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count > 0;
    }

    public int contarCursos() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CURSOS, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count;
    }

    // ── INSCRIPCIONES ────────────────────────────────────

    public void inscribir(String usuario, int cursoId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("usuario", usuario);
        cv.put("curso_id", cursoId);
        db.insert(TABLE_INSCRIPCIONES, null, cv);
        db.close();
    }

    public boolean estaInscrito(String usuario, int cursoId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INSCRIPCIONES +
                        " WHERE usuario = ? AND curso_id = ?",
                new String[]{usuario, String.valueOf(cursoId)});
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count > 0;
    }

    public List<Curso> obtenerCursosInscritos(String usuario) {
        List<Curso> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT c.* FROM " + TABLE_CURSOS + " c " +
                        "INNER JOIN " + TABLE_INSCRIPCIONES + " i ON c.id = i.curso_id " +
                        "WHERE i.usuario = ?",
                new String[]{usuario});
        while (c.moveToNext()) {
            Curso curso = new Curso(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            );
            lista.add(curso);
        }
        c.close();
        db.close();
        return lista;
    }

    public int contarInscritos(String usuario) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INSCRIPCIONES +
                " WHERE usuario = ?", new String[]{usuario});
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count;
    }

    // ── LECCIONES ─────────────────────────────────────────

    public void insertarLeccion(int cursoId, Leccion leccion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("curso_id", cursoId);
        cv.put("titulo", leccion.getTitulo());
        cv.put("contenido", leccion.getContenido());
        db.insert(TABLE_LECCIONES, null, cv);
        db.close();
    }

    public List<Leccion> obtenerLeccionesDeCurso(int cursoId) {
        List<Leccion> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, titulo, contenido FROM " + TABLE_LECCIONES +
                        " WHERE curso_id = ? ORDER BY id ASC",
                new String[]{String.valueOf(cursoId)});
        while (c.moveToNext()) {
            lista.add(new Leccion(c.getInt(0), c.getString(1), c.getString(2)));
        }
        c.close();
        db.close();
        return lista;
    }
}