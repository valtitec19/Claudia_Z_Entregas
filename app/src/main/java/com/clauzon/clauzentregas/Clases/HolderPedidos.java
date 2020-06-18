package com.clauzon.clauzentregas.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzentregas.R;


public class HolderPedidos extends RecyclerView.ViewHolder {
    private TextView productos_pedido,lugar,fecha,hora;
    private ImageView foto;
    public HolderPedidos(@NonNull View itemView) {
        super(itemView);
        productos_pedido=(TextView)itemView.findViewById(R.id.productos_recycler_pedido);
        lugar=(TextView)itemView.findViewById(R.id.lugar_recycler_pedido);
        hora=(TextView)itemView.findViewById(R.id.hora_recycler_pedido);
        foto=(ImageView)itemView.findViewById(R.id.foto_recycler_pedido);
        fecha=(TextView)itemView.findViewById(R.id.fecha_recycler_pedido);

    }

    public TextView getProductos_pedido() {
        return productos_pedido;
    }

    public void setProductos_pedido(TextView productos_pedido) {
        this.productos_pedido = productos_pedido;
    }

    public TextView getLugar() {
        return lugar;
    }

    public void setLugar(TextView lugar) {
        this.lugar = lugar;
    }

    public TextView getFecha() {
        return fecha;
    }

    public void setFecha(TextView fecha) {
        this.fecha = fecha;
    }

    public ImageView getFoto() {
        return foto;
    }

    public void setFoto(ImageView foto) {
        this.foto = foto;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }
}
