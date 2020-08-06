package com.clauzon.clauzentregas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.Clases.Repartidor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmarRegistroActivity extends AppCompatActivity {

    private Repartidor repartidor;
    private TextView txt1,txt2,txt3,txt4,txt5,txt6;
    private ImageView imageView1,imageView2;
    private Button btn1,btn2;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private String temp_pass,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_registro);
        Intent i = getIntent();
        repartidor = (Repartidor) i.getSerializableExtra("repartidor");
        temp_pass=i.getStringExtra("pass");
        inicia_views(repartidor);
        firebaseON();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(repartidor.getCorreo(), temp_pass)
                        .addOnCompleteListener(ConfirmarRegistroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser currenUser=mAuth.getCurrentUser();
                                                id=currenUser.getUid();
                                                repartidor.setId(id);
                                                DatabaseReference referenceRepartidores= database.getReference("Repartidores/"+id);
                                                referenceRepartidores.setValue(repartidor);;
                                                recuperar_token_dispositivo();
                                                Toast.makeText(ConfirmarRegistroActivity.this, "Verifica tu correo en tu bandeja de entrada", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ConfirmarRegistroActivity.this,ActivacionActivity.class));
                                                finish();
                                            }else{

                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(ConfirmarRegistroActivity.this, "Error de registro", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void recuperar_token_dispositivo() {
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
                        enviar_notificacion(token,"Tus datos estan siendo revisados","Cuanto seas autorizado podras comenzar a realizar entregas",repartidor.getImagenes().get(0));
                    }
                });

    }


    private void enviar_notificacion(String token,String titulo, String detalle,String imagen) {
        RequestQueue mRequestQue = Volley.newRequestQueue(this);

        JSONObject json = new JSONObject();
        try {

            json.put("to", token);

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("titulo", titulo);
            notificationObj.put("detalle", detalle);
            notificationObj.put("imagen",imagen);



            //replace notification with data when went send data
            json.put("data", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,null,null) {


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAE3HNDFU:APA91bEmPKbwtdaQIrU9g2GmxBEwy7zqHzdwG-L3I7o6HzrKhJ5BupTBTqhN67ytbObOv_NUILcDMaG-HwCLi2tEFKDwOWShs14ZOGpWZOh2DJNhxwjAQIfPtWgn7sxWuDR9VfT4uPQW");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void inicia_views(Repartidor repartidor){
        txt1=(TextView)findViewById(R.id.txt_nombre_final);
        txt2=(TextView)findViewById(R.id.txt_email_final);
        txt3=(TextView)findViewById(R.id.txt_telefono_final);
        txt4=(TextView)findViewById(R.id.txt_horario_final);
        txt5=(TextView)findViewById(R.id.txt_cobertura_final);
        txt6=(TextView)findViewById(R.id.txt_estado_final);
        imageView1=(ImageView)findViewById(R.id.imageView_foto_final);
        imageView2=(ImageView)findViewById(R.id.imageView_estado_final);
        btn1=(Button)findViewById(R.id.btn_enviar_final);
        btn2=(Button)findViewById(R.id.btn_editar_registro);
        txt1.setText(repartidor.getNombre()+" "+repartidor.getApellidos());
        txt2.setText(repartidor.getCorreo());
        txt3.setText(repartidor.getTelefono().toString());
        txt4.setText(repartidor.getHorario_inicio()+" - "+repartidor.getHorario_fin());
        txt5.setText("");
        for(int i=0;i<repartidor.getCobertura().size();i++){
            String k=txt5.getText().toString();
            txt5.setText(k+(repartidor.getCobertura().get(i)+" "));
        }
        if(repartidor.getEstado()){

        }else{
            txt6.setText("INACTIVO");
        }
        Glide.with(ConfirmarRegistroActivity.this).load(repartidor.getImagenes().get(0)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView1);
    }
    public void firebaseON(){
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }
}
