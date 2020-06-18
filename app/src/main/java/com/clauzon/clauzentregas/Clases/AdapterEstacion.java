package com.clauzon.clauzentregas.Clases;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterEstacion extends RecyclerView.Adapter<HolderEstacion> implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Estacion> list = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    private Ruta ruta;
    private String id;
    private ArrayList<Estacion> new_ruta = new ArrayList<>();
    private int hora, minutos;
    int temp_pos;
    private String linea;

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

    public AdapterEstacion(Context context, ArrayList<Estacion> new_ruta,String linea) {
        this.context = context;
        this.new_ruta = new_ruta;
        this.linea=linea;

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
        temp_pos = position;
//        databaseReference.child("Rutas/"+id).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(int i=0;i<ruta.getEstaciones().size();i++){
//                    if(ruta.getEstaciones().get(i).getNombre().equals(list.get(position).getNombre())){
//                        holder.getCheckBox_estacion().setChecked(true);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        holder.getLinea().setText(list.get(position).getHora());
        holder.getEstacion().setText(list.get(position).getNombre());
        holder.getLinea().setText(list.get(position).getLinea());
        Glide.with(context).load(list.get(position).getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getImageView());
        final int pos = position;
        /************Recupera checks*///////////
        for (int i = 0; i < new_ruta.size(); i++) {
            if (new_ruta.get(i).getNombre().equals(list.get(position).getNombre()) && new_ruta.get(i).getLinea().equals(linea)) {
                holder.getCheckBox_estacion().setChecked(true);
                holder.getLinea().setText(new_ruta.get(i).getHora());
            }
        }
        /************Fin Recupera checks*///////////

        holder.getCheckBox_estacion().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.getCheckBox_estacion().isChecked();
                if (isChecked) {
                    //checkBox clicked and checked
                    new_ruta.add(list.get(position));
                    final Calendar calendar = Calendar.getInstance();
                    hora = calendar.get(Calendar.HOUR_OF_DAY);
                    minutos = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {

                            if(i>12){
                                i=i-12;
                                if(i1<10){
                                    holder.getLinea().setText(i+":0"+i1+"pm");
                                    for (int j = 0; j < new_ruta.size(); j++) {
                                        if (new_ruta.get(j).getNombre().equals(list.get(position).getNombre())) {
                                            new_ruta.get(j).setHora(i+":0"+i1+"pm");
                                            new_ruta.get(j).setFoto(list.get(position).getFoto());
                                        }
                                    }
                                }else if(i1==0){
                                    holder.getLinea().setText(i+":00"+"pm");
                                    for (int j = 0; j < new_ruta.size(); j++) {
                                        if (new_ruta.get(j).getNombre().equals(list.get(position).getNombre())) {
                                            new_ruta.get(j).setHora(i+":00"+"pm");
                                            new_ruta.get(j).setFoto(list.get(position).getFoto());
                                        }
                                    }
                                }else {
                                    holder.getLinea().setText(i+":"+i1+"pm");
                                    for (int j = 0; j < new_ruta.size(); j++) {
                                        if (new_ruta.get(j).getNombre().equals(list.get(position).getNombre())) {
                                            new_ruta.get(j).setHora(i+":"+i1+"pm");
                                            new_ruta.get(j).setFoto(list.get(position).getFoto());
                                        }
                                    }
                                }
                            }else{
                                if(i1<10){
                                    holder.getLinea().setText(i+":0"+i1+"am");
                                    for (int j = 0; j < new_ruta.size(); j++) {
                                        if (new_ruta.get(j).getNombre().equals(list.get(position).getNombre())) {
                                            new_ruta.get(j).setHora(i+":0"+i1+"am");
                                            new_ruta.get(j).setFoto(list.get(position).getFoto());
                                        }
                                    }
                                }else if(i1==0){
                                    holder.getLinea().setText(i+":00"+"am");
                                    for (int j = 0; j < new_ruta.size(); j++) {
                                        if (new_ruta.get(j).getNombre().equals(list.get(position).getNombre())) {
                                            new_ruta.get(j).setHora(i+":00"+"am");
                                            new_ruta.get(j).setFoto(list.get(position).getFoto());

                                        }
                                    }
                                }else {
                                    holder.getLinea().setText(i+":"+i1+"am");
                                    for (int j = 0; j < new_ruta.size(); j++) {
                                        if (new_ruta.get(j).getNombre().equals(list.get(position).getNombre())) {
                                            new_ruta.get(j).setHora(i+":"+i1+"am");
                                            new_ruta.get(j).setFoto(list.get(position).getFoto());
                                        }
                                    }
                                }
                            }


                        }
                    }, hora, minutos, false);
                    timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //cancel button clicked
                            holder.getCheckBox_estacion().setChecked(false);
                            new_ruta.remove(list.get(temp_pos));
                            for(int i=0;i<new_ruta.size();i++){
                                if(new_ruta.get(i).getNombre().equals(list.get(temp_pos).getNombre())){
                                    new_ruta.remove(new_ruta.get(i));
                                }
                            }
                        }
                    });

                    timePickerDialog.setCancelable(false);
                    timePickerDialog.show();
                } else {
                    //checkBox clicked and unchecked
                    new_ruta.remove(list.get(position));
                    for (int i = 0; i < new_ruta.size(); i++) {
                        if (new_ruta.get(i).getNombre().equals(list.get(position).getNombre())) {
                            new_ruta.remove(new_ruta.get(i));
                        }
                    }

                }

            }
        });


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
