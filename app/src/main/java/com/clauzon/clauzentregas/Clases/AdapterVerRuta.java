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

public class AdapterVerRuta extends RecyclerView.Adapter<HolderEstacion> implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Estacion> list = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    private ArrayList<Estacion> new_ruta=new ArrayList<>();
    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void add_lista(Estacion estacion) {
        list.add(estacion);
        notifyItemInserted(this.list.size());
    }

    public AdapterVerRuta(Context context, ArrayList<Estacion> new_ruta) {
        this.context = context;
        this.new_ruta = new_ruta;


    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderEstacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_estacion, parent, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        view.setOnClickListener(this);
        return new HolderEstacion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderEstacion holder, final int position) {

        holder.getLinea().setText(list.get(position).getHora());
        holder.getEstacion().setText(list.get(position).getNombre());
        Glide.with(context).load(list.get(position).getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getImageView());
        holder.getCheckBox_estacion().setVisibility(View.GONE);


    }

    public ArrayList<Estacion> get_list() {
        return new_ruta;
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
