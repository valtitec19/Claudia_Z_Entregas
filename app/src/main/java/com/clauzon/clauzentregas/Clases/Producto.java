package com.clauzon.clauzentregas.Clases;

public class Producto {
    private String nombre_producto, descripcion, id_producto, foto_producto,estado_producto,categoria;
    private boolean estado=false;
    private float compra_producto, venta_producto;
    private int cantidad_producto;

    public Producto() {
    }

    public Producto(String nombre_producto, String descripcion, String id_producto, String foto_producto, boolean estado, float compra_producto, float venta_producto, int cantidad_producto,String estado_producto,String categoria) {
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.id_producto = id_producto;
        this.foto_producto = foto_producto;
        this.estado = estado;
        this.compra_producto = compra_producto;
        this.venta_producto = venta_producto;
        this.cantidad_producto = cantidad_producto;
        this.estado_producto=estado_producto;
        this.categoria=categoria;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getFoto_producto() {
        return foto_producto;
    }

    public void setFoto_producto(String foto_producto) {
        this.foto_producto = foto_producto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public float getCompra_producto() {
        return compra_producto;
    }

    public void setCompra_producto(float compra_producto) {
        this.compra_producto = compra_producto;
    }

    public float getVenta_producto() {
        return venta_producto;
    }

    public void setVenta_producto(float venta_producto) {
        this.venta_producto = venta_producto;
    }

    public int getCantidad_producto() {
        return cantidad_producto;
    }

    public void setCantidad_producto(int cantidad_producto) {
        this.cantidad_producto = cantidad_producto;
    }

    public String getEstado_producto() {
        return estado_producto;
    }

    public void setEstado_producto(String estado_producto) {
        this.estado_producto = estado_producto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
