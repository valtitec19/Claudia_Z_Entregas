package com.clauzon.clauzentregas.Clases;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzentregas.R;

public class HolderVerRuta extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView estacion,linea;
    private CheckBox checkBox_estacion;

    public HolderVerRuta(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.imagen_estacion);
        estacion = (TextView)itemView.findViewById(R.id.estacion);
        linea = (TextView)itemView.findViewById(R.id.hora_estacion);
        checkBox_estacion = (CheckBox)itemView.findViewById(R.id.checkbox_add_estacion);
//        this.setIsRecyclable(false);

    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getEstacion() {
        return estacion;
    }

    public void setEstacion(TextView estacion) {
        this.estacion = estacion;
    }

    public TextView getLinea() {
        return linea;
    }

    public void setLinea(TextView linea) {
        this.linea = linea;
    }

    public CheckBox getCheckBox_estacion() {
        return checkBox_estacion;
    }

    public void setCheckBox_estacion(CheckBox checkBox_estacion) {
        this.checkBox_estacion = checkBox_estacion;
    }
}
