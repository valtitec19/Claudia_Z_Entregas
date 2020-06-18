package com.clauzon.clauzentregas.Clases;

public class Notificacion {
    private String id_usuaio;
    private String id_notificacion;
    private String concepto;
    private String concepto2;

    public Notificacion() {
    }

    public Notificacion(String id_usuaio, String id_notificacion,String concepto,String concepto2) {
        this.id_usuaio = id_usuaio;
        this.id_notificacion = id_notificacion;
        this.concepto = concepto;
        this.concepto2=concepto2;
    }

    public String getId_usuaio() {
        return id_usuaio;
    }

    public void setId_usuaio(String id_usuaio) {
        this.id_usuaio = id_usuaio;
    }

    public String getId_notificacion() {
        return id_notificacion;
    }

    public void setId_notificacion(String id_notificacion) {
        this.id_notificacion = id_notificacion;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getConcepto2() {
        return concepto2;
    }

    public void setConcepto2(String concepto2) {
        this.concepto2 = concepto2;
    }
}
