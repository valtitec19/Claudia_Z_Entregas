package com.clauzon.clauzentregas.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.Clases.Repartidor;
import com.clauzon.clauzentregas.Clases.Usuario;
import com.clauzon.clauzentregas.EditarDatosActivity;
import com.clauzon.clauzentregas.LoginActivity;
import com.clauzon.clauzentregas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToolsFragment extends Fragment {

    private Button boton,editar_dator;
    private String url_foto,nombre;
    private ToolsViewModel toolsViewModel;
    FirebaseUser currentUser ;
    private CircleImageView foto_perfil,foto_logo;
    private TextView txt_nombre;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        boton=(Button)root.findViewById(R.id.boton_cerrar_sesion);
        editar_dator=(Button)root.findViewById(R.id.editar_datos);
        foto_perfil=(CircleImageView)root.findViewById(R.id.foto_user);
        foto_logo=(CircleImageView)root.findViewById(R.id.imagen_claudia_logo);
        txt_nombre=(TextView)root.findViewById(R.id.user_name);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                borrar_token_dispositivo();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        editar_dator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarDatosActivity.class));
                getActivity().finish();
            }
        });

        firebase_ON();
        cargar_datos();
        return root;
    }

    private void firebase_ON() {
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        currentUser=mAuth.getCurrentUser();
        databaseReference=database.getReference();
    }

    public void cargar_datos(){
        Glide.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/clauzon.appspot.com/o/RECURSOS%2FCZON-LogoApp-01.jpg?alt=media&token=95aec94d-854e-42bd-b7c3-d7411656b32b").centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(foto_logo);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            DatabaseReference referenceRepartidores= database.getReference("Repartidores/"+currentUser.getUid());
            referenceRepartidores.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Repartidor repartidor =dataSnapshot.getValue(Repartidor.class);

                    try {
                        nombre=repartidor.getNombre()+" "+repartidor.getApellidos();
                        url_foto=repartidor.getImagenes().get(0);
                        txt_nombre.setText(nombre);
                        Glide.with(getActivity()).load(url_foto).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(foto_perfil);
                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            startActivity(new Intent(getActivity(),LoginActivity.class));
            getActivity().finish();
        }
    }


    private void borrar_token_dispositivo() {

        if(currentUser!=null && currentUser.isEmailVerified()){
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Repartidores/"+currentUser.getUid()).child("token");
                            reference.removeValue();

                        }
                    });
        }
    }
}