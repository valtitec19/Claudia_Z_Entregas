package com.clauzon.clauzentregas.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzentregas.R;

public class HolderHome extends RecyclerView.ViewHolder{

    private TextView nombre_item, precio_venta, categoria,descripcion,estado,cantidad_item,estado2;
    private ImageView foto_item;

    public HolderHome(@NonNull View itemView) {
        super(itemView);
        nombre_item = (TextView) itemView.findViewById(R.id.nombre_item);
        precio_venta = (TextView) itemView.findViewById(R.id.precio_venta);
        categoria=(TextView)itemView.findViewById(R.id.categoria_item);
        descripcion=(TextView)itemView.findViewById(R.id.descripcion_item);
        foto_item= (ImageView) itemView.findViewById(R.id.foto_item);
        estado=(TextView)itemView.findViewById(R.id.estado_del_item_);
        cantidad_item=(TextView)itemView.findViewById(R.id.cantidad_de_items);
        estado2=(TextView)itemView.findViewById(R.id.estado_del_item_2);
        nombre_item.setSelected(true);
        precio_venta.setSelected(true);
        categoria.setSelected(true);
        estado.setSelected(true);
        cantidad_item.setSelected(true);
        estado2.setSelected(true);
        descripcion.setSelected(true);
    }

    public TextView getNombre_item() {
        return nombre_item;
    }

    public void setNombre_item(TextView nombre_item) {
        this.nombre_item = nombre_item;
    }

    public TextView getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(TextView precio_venta) {
        this.precio_venta = precio_venta;
    }

    public TextView getCategoria() {
        return categoria;
    }

    public void setCategoria(TextView categoria) {
        this.categoria = categoria;
    }

    public TextView getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(TextView descripcion) {
        this.descripcion = descripcion;
    }

    public TextView getEstado() {
        return estado;
    }

    public void setEstado(TextView estado) {
        this.estado = estado;
    }

    public TextView getCantidad_item() {
        return cantidad_item;
    }

    public void setCantidad_item(TextView cantidad_item) {
        this.cantidad_item = cantidad_item;
    }

    public TextView getEstado2() {
        return estado2;
    }

    public void setEstado2(TextView estado2) {
        this.estado2 = estado2;
    }

    public ImageView getFoto_item() {
        return foto_item;
    }

    public void setFoto_item(ImageView foto_item) {
        this.foto_item = foto_item;
    }

}
