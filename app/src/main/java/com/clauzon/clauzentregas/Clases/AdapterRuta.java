package com.clauzon.clauzentregas.Clases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.R;
import com.clauzon.clauzentregas.RutasActivity;
import com.clauzon.clauzentregas.VerRutaActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterRuta extends RecyclerView.Adapter<RutaHolder> implements View.OnClickListener{
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Ruta> list = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    private String id;

    public AdapterRuta(Context context,String id) {
        this.context = context;
        this.id=id;
    }

    public void add_ruta(Ruta ruta) {
        list.add(ruta);
        notifyItemInserted(this.list.size());
    }

    @NonNull
    @Override
    public RutaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycker_ruta, parent, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        view.setOnClickListener(this);
        return new RutaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RutaHolder holder, final int position) {
        final int pos_final=list.get(position).getEstaciones().size()-1;
        holder.getNombre().setText(list.get(position).getNombre());
        holder.getHora_inicio().setText(list.get(position).getEstaciones().get(0).getHora());
        holder.getHora_fin().setText(list.get(position).getEstaciones().get(pos_final).getHora());
        Glide.with(context).load(list.get(position).getEstaciones().get(0).getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto_inicio());
        Glide.with(context).load(list.get(position).getEstaciones().get(pos_final).getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto_fin());
            if(list.get(position).getRepartidor().equals(id)){
                holder.getSwitchCompat().setChecked(true);
            }else {
                holder.getSwitchCompat().setChecked(false);
            }


        holder.getSwitchCompat().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean estado=holder.getSwitchCompat().isChecked();
                if(estado){
                    databaseReference.child("Rutas").child(list.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Ruta ruta=dataSnapshot.getValue(Ruta.class);
                            ruta.setRepartidor(id);
                            databaseReference.child("Rutas").child(ruta.getId()).setValue(ruta);
                            context.startActivity(new Intent(context,RutasActivity.class));
                            ((Activity) context).finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    databaseReference.child("Rutas").child(list.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Ruta ruta=dataSnapshot.getValue(Ruta.class);
                                if(id.equals(ruta.getRepartidor())){
                                    ruta.setRepartidor("");
                                }

                            databaseReference.child("Rutas").child(ruta.getId()).setValue(ruta);
                            context.startActivity(new Intent(context,RutasActivity.class));
                            ((Activity) context).finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        holder.getEditar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, VerRutaActivity.class);
                intent.putExtra("ruta",list.get(position));
                context.startActivity(intent);

            }
        });

//        String temp=String.valueOf(list.get(position).getEstaciones().get(0).getHora());
//        String temp2=String.valueOf(list.get(position).getEstaciones().get(pos_final).getHora());
//        String parts[]=temp.split(":");
//        String parts2[]=temp2.split(":");
//        String minutos1=parts[1].substring(0,2);
//        String minutos2=parts2[1].substring(0,2);
//        String horario=parts[1].substring(2,4);
//        String horario2=parts2[1].substring(2,4);
//        Log.e("Horario1 ", horario );
//        Log.e("Horario2 ", horario2 );
//
//        if(horario.equals("am") && horario2.equals("am")){
//            //AMBOS AM
//            if(Integer.parseInt(parts2[0])-Integer.parseInt(parts[0]) >=0){
//                //HORAS 2 MMAYOR A HORAS 1
//                if(Integer.parseInt(minutos2)-Integer.parseInt(minutos1)>=0){
//                    //MINUTOS 2 MAYOR A MINUTOS 1
//                    int min=(Integer.parseInt(minutos2)-Integer.parseInt(minutos1));
//                    int horas=Integer.parseInt(parts2[0])-Integer.parseInt(parts[0]);
//                    holder.getTiempo_recorrido().setText(horas+" horas, "+min+" minutos");
//                }else {
//                    int min=(Integer.parseInt(minutos2)-Integer.parseInt(minutos1))+60;
//                    int horas=Integer.parseInt(parts2[0])-Integer.parseInt(parts[0])-1;
//                    holder.getTiempo_recorrido().setText(horas+" horas, "+min+" minutos");
//                }
//            }else {
//                holder.getTiempo_recorrido().setText("Ruta incongruente");
//            }
//
//
//        }else if(horario.equals("am") && horario2.equals("pm")){
//            //AM PM
//            if(Integer.parseInt(parts2[0])<Integer.parseInt(parts[0])){
//                if(Integer.parseInt(minutos2)-Integer.parseInt(minutos1)>=0){
//                    //MINUTOS 2 MAYOR A MINUTOS 1
//                    int min=(Integer.parseInt(minutos2)-Integer.parseInt(minutos1));
//                    int horas=Integer.parseInt(parts2[0])-Integer.parseInt(parts[0]);
//                    holder.getTiempo_recorrido().setText(horas+12+" horas, "+min+" minutos");
//                }else {
//                    int min=(Integer.parseInt(minutos2)-Integer.parseInt(minutos1))+60;
//                    int horas=Integer.parseInt(parts2[0])-Integer.parseInt(parts[0])-1;
//                    holder.getTiempo_recorrido().setText(horas+12+" horas, "+min+" minutos");
//                }
//            }else {
//                holder.getTiempo_recorrido().setText("Ruta incongruente");
//            }
//        }else if(horario.equals("pm") && horario2.equals("pm")){
//            //PM PM
//            if(Integer.parseInt(parts2[0])>=Integer.parseInt(parts[0])){
//                //HORAS 2 MMAYOR A HORAS 1
//                if(Integer.parseInt(minutos2)-Integer.parseInt(minutos1)>=0){
//                    //MINUTOS 2 MAYOR A MINUTOS 1
//                    int min=(Integer.parseInt(minutos2)-Integer.parseInt(minutos1));
//                    int horas=Integer.parseInt(parts2[0])-Integer.parseInt(parts[0]);
//                    holder.getTiempo_recorrido().setText(horas+" horas, "+min+" minutos");
//                }else {
//                    int min=(Integer.parseInt(minutos2)-Integer.parseInt(minutos1))+60;
//                    int horas=Integer.parseInt(parts2[0])-Integer.parseInt(parts[0])-1;
//                    holder.getTiempo_recorrido().setText(horas+" horas, "+min+" minutos");
//                }
//            }else {
//                holder.getTiempo_recorrido().setText("Ruta incongruente");
//            }
//        }else {
//            holder.getTiempo_recorrido().setText("Ruta incongruente");
//            //SIN SENTIDO
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }
}
