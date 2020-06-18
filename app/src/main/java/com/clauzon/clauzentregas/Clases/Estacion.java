package com.clauzon.clauzentregas.Clases;

import java.io.Serializable;

public class Estacion implements Serializable {
    private String nombre;
    private Boolean estado;
    private String hora;
    private String linea;
    private String foto;

    public Estacion() {
    }

    public Estacion(String nombre, Boolean estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public Estacion(String nombre, String hora, String linea, String foto) {
        this.nombre = nombre;
        this.hora = hora;
        this.linea = linea;
        this.foto=foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
