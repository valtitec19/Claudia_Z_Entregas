package com.clauzon.clauzentregas.Clases;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzentregas.R;


public class RutaHolder extends RecyclerView.ViewHolder {
    private TextView nombre,hora_inicio,hora_fin,tiempo_recorrido;
    private ImageView foto_inicio,foto_fin;
    private SwitchCompat switchCompat;
    private Button editar;

    public RutaHolder(@NonNull View itemView) {
        super(itemView);
        nombre=(TextView)itemView.findViewById(R.id.nombre_de_la_ruta);
        hora_inicio=(TextView)itemView.findViewById(R.id.hora_inicio_ruta);
        hora_fin=(TextView)itemView.findViewById(R.id.hora_fin_ruta);
        tiempo_recorrido=(TextView)itemView.findViewById(R.id.tiempo_recorrido);
        foto_inicio=(ImageView)itemView.findViewById(R.id.inicio_ruta);
        foto_fin=(ImageView)itemView.findViewById(R.id.fin_ruta);
        switchCompat=(SwitchCompat)itemView.findViewById(R.id.switch_ruta);
        editar=(Button)itemView.findViewById(R.id.editar_ruta);
        this.setIsRecyclable(true);
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(TextView hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public TextView getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(TextView hora_fin) {
        this.hora_fin = hora_fin;
    }

    public TextView getTiempo_recorrido() {
        return tiempo_recorrido;
    }

    public void setTiempo_recorrido(TextView tiempo_recorrido) {
        this.tiempo_recorrido = tiempo_recorrido;
    }

    public ImageView getFoto_inicio() {
        return foto_inicio;
    }

    public void setFoto_inicio(ImageView foto_inicio) {
        this.foto_inicio = foto_inicio;
    }

    public ImageView getFoto_fin() {
        return foto_fin;
    }

    public void setFoto_fin(ImageView foto_fin) {
        this.foto_fin = foto_fin;
    }

    public SwitchCompat getSwitchCompat() {
        return switchCompat;
    }

    public void setSwitchCompat(SwitchCompat switchCompat) {
        this.switchCompat = switchCompat;
    }


    public Button getEditar() {
        return editar;
    }

    public void setEditar(Button editar) {
        this.editar = editar;
    }
}
