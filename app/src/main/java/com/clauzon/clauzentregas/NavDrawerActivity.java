package com.clauzon.clauzentregas;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.Clases.Notificacion;
import com.clauzon.clauzentregas.Clases.Pedidos;
import com.clauzon.clauzentregas.Clases.Repartidor;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.clauzon.clauzentregas.Clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private CircleImageView foto;
    private TextView txt_nombre, txt_correo;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseDatabase database;
    private String nombre, correo, url_foto_repartidor;
    private final static String CHANEL_ID = "NOTIFICATION";
    private Boolean aux;
    private Pedidos pedidos;
    private PendingIntent pendingIntent;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        try {
            recuperar_token_dispositivo();
        }catch (Exception e){}
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        foto = (CircleImageView) headerView.findViewById(R.id.imageView);
        txt_nombre = (TextView) headerView.findViewById(R.id.nombre_repartidor);
        txt_correo = (TextView) headerView.findViewById(R.id.correo_repartidor);
        comprueba_estado();
        ShowNotification();
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
                        DatabaseReference reference= database.getReference().child("Repartidores/"+currentUser.getUid()).child("token");
                        reference.setValue(token);

                    }
                });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i=item.getItemId();

        if (i == R.id.action_settings){
            startActivity(new Intent(this, RutasActivity.class));
            finish();
        }else if(i == 16908332){
            drawer.openDrawer(GravityCompat.START);
        }else if(i == R.id.recordatorio){
           DatabaseReference reference =database.getReference("Pedidos");
           reference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for(DataSnapshot da  : snapshot.getChildren()){
                       final Pedidos pedidos = da.getValue(Pedidos.class);
                       if(pedidos.getRepartidor_id().equals(currentUser.getUid())){
                           DatabaseReference reference1=database.getReference("Usuarios");
                           reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   for (DataSnapshot ds : snapshot.getChildren()){
                                       Usuario usuario = ds.getValue(Usuario.class);
                                       if(pedidos.getUsuario_id().equals(usuario.getId())){
                                           Log.e("Notificacion para ", usuario.getNombre() );
                                           enviar_recordatorio(usuario.getToken(),"Tienes una entrega proxima",pedidos.getDireccion_entrega()+" "+pedidos.getDescripcion(),pedidos.getFoto());
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        }

        return true;
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentUser != null) {
            final DatabaseReference referenceRepartidores = database.getReference("Repartidores/" + currentUser.getUid());
            referenceRepartidores.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Repartidor repartidor = dataSnapshot.getValue(Repartidor.class);
                    if (repartidor.getEstado()) {

                    } else {
                        startActivity(new Intent(NavDrawerActivity.this, ActivacionActivity.class));
                        finish();
                    }
                    nombre = repartidor.getNombre() + " " + repartidor.getApellidos();
                    url_foto_repartidor = repartidor.getImagenes().get(0);
                    correo = repartidor.getCorreo();
                    txt_nombre.setText(nombre);
                    txt_correo.setText(correo);
                    Glide.with(NavDrawerActivity.this).load(url_foto_repartidor).centerCrop().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(foto);
                    ShowNotification();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference referenceRepartidores = database.getReference("Repartidores/" + currentUser.getUid());
            referenceRepartidores.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Repartidor repartidor = dataSnapshot.getValue(Repartidor.class);
                    if (repartidor.getEstado()) {

                    } else {
                        startActivity(new Intent(NavDrawerActivity.this, ActivacionActivity.class));
                        finish();
                    }
                    nombre = repartidor.getNombre() + " " + repartidor.getApellidos();
                    url_foto_repartidor = repartidor.getImagenes().get(0);
                    correo = repartidor.getCorreo();
                    txt_nombre.setText(nombre);
                    txt_correo.setText(correo);
                    Glide.with(NavDrawerActivity.this).load(url_foto_repartidor).centerCrop().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(foto);
                    ShowNotification();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            startActivity(new Intent(NavDrawerActivity.this, LoginActivity.class));
            finish();
        }

    }

    public void comprueba_estado() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            final DatabaseReference referenceRepartidores = database.getReference("Repartidores/" + currentUser.getUid());
            referenceRepartidores.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Repartidor repartidor = dataSnapshot.getValue(Repartidor.class);
                    if (repartidor.getEstado()) {

                    } else {
                        startActivity(new Intent(NavDrawerActivity.this, ActivacionActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotificaicon(String concepto,String concepto2) {
        Intent intent = new Intent(getApplicationContext(), FinalizarPedido.class);
        intent.putExtra("concepto2",concepto2);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANEL_ID);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setContentTitle(concepto);
        builder.setContentText("Toca para abrir aplicación");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{500, 500, 500});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(0, builder.build());


    }

    private void ShowNotification() {
        DatabaseReference reference = database.getReference();
        reference.child("Notificaciones").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {
                final Notificacion notificacion = dataSnapshot.getValue(Notificacion.class);
                if (notificacion.getId_usuaio().equals(currentUser.getUid())) {

                    DatabaseReference reference1=database.getReference();
                    reference1.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                pedidos=ds.getValue(Pedidos.class);
                                if(notificacion.getConcepto2().equals(pedidos.getId())){
                                    createNotificationChanel();
                                    createNotificaicon(notificacion.getConcepto(),pedidos.getId());
                                    DatabaseReference databaseReference=database.getReference().child("Notificaciones").child(notificacion.getId_notificacion());
                                    databaseReference.removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
