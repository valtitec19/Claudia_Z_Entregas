package com.clauzon.clauzentregas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.Clases.Pedidos;
import com.clauzon.clauzentregas.Clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FinalizarPedido extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Pedidos recibido;
    private String concepto2;

    private TextView usuario,descripcon,fecha,lugar,producto,costo,estado_pago,cantidad;
    private ImageView imageView;
    private RadioButton pendiente,cancelado,entregado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);
        inicio_views();
        firebaseON();
        try {
            Intent i = getIntent();
            recibido = (Pedidos) i.getSerializableExtra("pedido");
            recupera_pedidos();
        }catch (Exception e){

        }
        try {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.cancel(0);
            Intent i = getIntent();
            concepto2 = i.getExtras().getString("concepto2");
            databaseReference.child("Pedidos").child(concepto2).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    recibido=dataSnapshot.getValue(Pedidos.class);
                    recupera_pedidos();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }

    }

    public void inicio_views(){
        usuario=(TextView)findViewById(R.id.usuario_pedido_final);
        descripcon=(TextView)findViewById(R.id.descripcion_pedido_final);
        fecha=(TextView)findViewById(R.id.fecha_pedido_final);
        lugar=(TextView)findViewById(R.id.lugar_pedido_final);
        producto=(TextView)findViewById(R.id.producto_pedido_final);
        costo=(TextView)findViewById(R.id.costo_pedido_final);
        estado_pago=(TextView)findViewById(R.id.estado_pago);
        cantidad=(TextView)findViewById(R.id.cantidad_pedido_final);
        imageView=(ImageView) findViewById(R.id.imageView_pedido_finalizado);
        pendiente=(RadioButton)findViewById(R.id.r_button_pendiente);
        cancelado=(RadioButton)findViewById(R.id.r_button_cancelado);
        entregado=(RadioButton)findViewById(R.id.r_button_entregado);
    }

    public void firebaseON(){
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
    }

    public void recupera_pedidos(){
        databaseReference.child("Pedidos/"+recibido.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pedidos pedidos=dataSnapshot.getValue(Pedidos.class);
                try {
                    producto.setText(pedidos.getNombre());
                }catch (Exception e){

                }
                descripcon.setText(pedidos.getDescripcion());
                lugar.setText(pedidos.getDireccion_entrega());
                fecha.setText(pedidos.getHora_entrega());
                costo.setText("$"+String.valueOf(pedidos.getCosto()*pedidos.getCantidad()));
                estado_pago.setText(pedidos.getEstado());
                cantidad.setText(recibido.getCantidad()+" Unidades");
                Glide.with(FinalizarPedido.this).load(recibido.getFoto()).centerCrop().override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario u=snapshot.getValue(Usuario.class);
                    try {
                        if(u.getId().equals(recibido.getUsuario_id())){
                            usuario.setText("Pedido para "+u.getNombre()+" "+u.getApellidos());
                        }
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Aceptar(View view) {
        if(cancelado.isChecked()){
            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);
            builder.setTitle("Pedido cancelado").setMessage("Al aceptar el pedido sera removido de tus entregas");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recibido.setRepartidor_id("no asignado");
                    recibido.setEstado("Cancelado");
                    DatabaseReference databaseReference1=database.getReference("Pedidos/"+recibido.getId());
                    databaseReference1.setValue(recibido);
                    DatabaseReference reference=database.getReference("Usuarios/"+recibido.getUsuario_id());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario usuario = snapshot.getValue(Usuario.class);
                            try {
                                enviar_recordatorio(usuario.getToken(),"Pedido cancelado",recibido.getNombre()+" ponte en contacto a traves de nuestras redes sociales ",recibido.getFoto());
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    startActivity(new Intent(FinalizarPedido.this,NavDrawerActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }else if(entregado.isChecked()){
            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);
            builder.setTitle("Pedido completado").setMessage("¿El pedido fue completado exitosamente?");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recibido.setEstado("Completado");
                    DatabaseReference databaseReference1=database.getReference("Pedidos/"+recibido.getId());
                    databaseReference1.setValue(recibido);
                    Toast.makeText(FinalizarPedido.this, "Felicidades por una entrega exitosa!", Toast.LENGTH_SHORT).show();
                    DatabaseReference reference=database.getReference("Usuarios/"+recibido.getUsuario_id());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario usuario = snapshot.getValue(Usuario.class);
                            enviar_recordatorio(usuario.getToken(),"Paquete entregado",recibido.getNombre(),recibido.getFoto());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(FinalizarPedido.this, NavDrawerActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }else if(pendiente.isChecked()){
            startActivity(new Intent(FinalizarPedido.this,NavDrawerActivity.class));
            finish();
        }
    }

    private void enviar_recordatorio(String token,String titulo, String detalle,String imagen) {
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
}
