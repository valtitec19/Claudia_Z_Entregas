package com.clauzon.clauzentregas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzentregas.Clases.AdapterRuta;
import com.clauzon.clauzentregas.Clases.Ruta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RutasActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private AdapterRuta adapterRuta;
    private AdapterRuta adapterRuta2;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ProgressBar progress_circular_rutas;
    private Spinner spinner;
    private String seleccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        toolbar = (Toolbar) findViewById(R.id.toolbar_rutas);
//        setSupportActionBar(toolbar);
        firebaseON();
        inicio_spinner();
        progress_circular_rutas=(ProgressBar)findViewById(R.id.progress_circular_rutas);
        progress_circular_rutas.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_ruta_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterRuta = new AdapterRuta(this,currentUser.getUid());
        adapterRuta2 = new AdapterRuta(this,currentUser.getUid());

        reference.child("Rutas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Ruta ruta=dataSnapshot.getValue(Ruta.class);
                try {
                    if(ruta.getEstado() && ruta.getRepartidor().isEmpty()){
                        adapterRuta.add_ruta(ruta);
                    }else  if(ruta.getEstado() && ruta.getRepartidor().equals(currentUser.getUid())){
                        adapterRuta2.add_ruta(ruta);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        recyclerView.setAdapter(adapterRuta);
        progress_circular_rutas.setVisibility(View.GONE);
    }

    public void firebaseON(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }


    private void inicio_spinner() {
        String[] opciones = {"Rutas disponibles","Mis rutas"};
        final ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(this,R.layout.spinner,opciones);
        spinner= (Spinner)findViewById(R.id.spinner_pedidos);
        spinner.setAdapter(adapter_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seleccion=adapterView.getItemAtPosition(i).toString();
                if(seleccion.equals("Rutas disponibles")){
                    recyclerView.setAdapter(adapterRuta);
                }else if(seleccion.equals("Mis rutas")){
                    recyclerView.setAdapter(adapterRuta2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,NavDrawerActivity.class));
        finish();
    }
}
