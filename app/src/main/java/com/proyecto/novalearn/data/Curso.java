package com.proyecto.novalearn.data;

import java.util.List;

public class Curso {
    private int id;
    private String nombre;
    private String descripcion;
    private String instructor;
    private String duracion;
    private String categoria;
    private String icono;
    private List<Leccion> lecciones;

    public Curso() {}

    public Curso(int id, String nombre, String descripcion,
                 String instructor, String duracion, String categoria, String icono) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.instructor = instructor;
        this.duracion = duracion;
        this.categoria = categoria;
        this.icono = icono;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public List<Leccion> getLecciones() { return lecciones; }
    public void setLecciones(List<Leccion> lecciones) { this.lecciones = lecciones; }
}