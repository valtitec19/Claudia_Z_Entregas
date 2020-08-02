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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdapterPedidos extends RecyclerView.Adapter<HolderPedidos> implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Pedidos> lista = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    private String nombres = "";

    public AdapterPedidos(Context context) {
        this.context = context;
    }

    public void add_pedido(Pedidos pedidos) {
        this.lista.add(pedidos);
        notifyItemInserted(this.lista.size());
    }

    @NonNull
    @Override
    public HolderPedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_pedidos, parent, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        view.setOnClickListener(this);
        return new HolderPedidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderPedidos holder, final int position) {

//        if(lista.get(position).getEstado().equals("Pago pendiente (En efectivo)") || lista.get(position).getEstado().equals("Pagado")){
        holder.getProductos_pedido().setText(lista.get(position).getNombre()+" "+lista.get(position).getColor()+ " "+lista.get(position).getTamano()+" "+lista.get(position).getModelo());
        holder.getLugar().setText("Metro " + lista.get(position).getDireccion_entrega());
        holder.getFecha().setText("Dia: " + lista.get(position).getFecha());
        holder.getHora().setText("Hora: "+lista.get(position).getHora_entrega());
        float cantidad = lista.get(position).getCantidad() * lista.get(position).getCosto();
        Glide.with(context).load(lista.get(position).getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto());


    }



    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public List<Pedidos> get_list(){
        return lista;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
