package com.clauzon.clauzentregas.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Repartidor implements Serializable {
    private String nombre,apellidos,correo,telefono,id,direccion,horario_inicio,horario_fin;
    private Boolean estado;
    private List<String> entregas=new ArrayList<>();
    private List<String> cobertura=new ArrayList<>();
    private List<String> imagenes=new ArrayList<>();
    private String tarjeta;
    private String token;

    public Repartidor() {
    }

    public Repartidor(String nombre, String apellidos, String correo, String telefono, String id, String direccion, String horario_inicio, String horario_fin, Boolean estado
            , List<String> entregas, List<String> cobertura,List<String> imagenes,String tarjeta,String token) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.id = id;
        this.direccion = direccion;
        this.horario_inicio = horario_inicio;
        this.horario_fin = horario_fin;
        this.estado = estado;
        this.entregas = entregas;
        this.cobertura = cobertura;
        this.imagenes=imagenes;
        this.tarjeta=tarjeta;
        this.token=token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorario_inicio() {
        return horario_inicio;
    }

    public void setHorario_inicio(String horario_inicio) {
        this.horario_inicio = horario_inicio;
    }

    public String getHorario_fin() {
        return horario_fin;
    }

    public void setHorario_fin(String horario_fin) {
        this.horario_fin = horario_fin;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<String> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<String> entregas) {
        this.entregas = entregas;
    }

    public List<String> getCobertura() {
        return cobertura;
    }

    public void setCobertura(List<String> cobertura) {
        this.cobertura = cobertura;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
