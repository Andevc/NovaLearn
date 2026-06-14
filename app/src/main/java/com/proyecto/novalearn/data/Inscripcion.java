package com.proyecto.novalearn.data;

public class Inscripcion {
    private int id;
    private String usuario;
    private int cursoId;

    public Inscripcion() {}

    public Inscripcion(int id, String usuario, int cursoId) {
        this.id = id;
        this.usuario = usuario;
        this.cursoId = cursoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public int getCursoId() { return cursoId; }
    public void setCursoId(int cursoId) { this.cursoId = cursoId; }
}