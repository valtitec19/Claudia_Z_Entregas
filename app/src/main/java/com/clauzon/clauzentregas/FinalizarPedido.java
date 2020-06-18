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
                producto.setText(pedidos.getNombre());
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
                    if(u.getId().equals(recibido.getUsuario_id())){
                        usuario.setText("Pedido para "+u.getNombre()+" "+u.getApellidos());
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
}
