package com.clauzon.clauzentregas.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<HolderHome> implements View.OnClickListener{

    private View.OnClickListener listener;
    private List<Producto> lista = new ArrayList();
    private Context c;

    public AdapterHome() {
    }

    @NonNull
    @Override
    public HolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(c).inflate(R.layout.recycler_home,parent,false);
        view.setOnClickListener(this);
        return new HolderHome(view);
    }

    public AdapterHome(Context c){
        this.c = c;
    }

    public void add_producto(Producto producto) {
        lista.add(producto);
        notifyItemInserted(lista.size());
    }



    @Override
    public void onBindViewHolder(@NonNull HolderHome holder, int position) {
        holder.getNombre_item().setText(lista.get(position).getNombre_producto());
        holder.getDescripcion().setText(lista.get(position).getDescripcion());
        holder.getPrecio_venta().setText("$" + String.valueOf(lista.get(position).getVenta_producto()));
        if (lista.get(position).isEstado() == true) {
            holder.getEstado().setText("Estado: Activo");
        } else if (lista.get(position).isEstado() == false) {
            holder.getEstado().setText("Estado: Inactivo");
        }
        Glide.with(c).load(lista.get(position).getFoto_producto()).centerCrop().override(450, 450).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto_item());
        holder.getCantidad_item().setText("#" + String.valueOf(lista.get(position).getCantidad_producto()));
        holder.getCategoria().setText(lista.get(position).getCategoria());
        holder.getEstado2().setText(lista.get(position).getEstado_producto());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public String nombre_p(int i) {
        String nombre = lista.get(i).getNombre_producto();
        return nombre;
    }

    public Producto getPos(int i){

        return lista.get(i);
    }

    public List<Producto> get_lista(){

        return lista;
    }

    public Producto cual_producto(String cual) {
        Producto p_t = new Producto();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre_producto().equals(cual)) {
                p_t = lista.get(i);
            }
        }
        return p_t;
    }

    public void filterList(List<Producto> filteredList) {
        lista=filteredList;
        notifyDataSetChanged();
    }

    public List<Producto> getLista(){
        return lista;
    }

    @Override
    public void onClick(View view) {

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
