package com.clauzon.clauzentregas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clauzon.clauzentregas.Clases.AdapterVerRuta;
import com.clauzon.clauzentregas.Clases.Ruta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerRutaActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private AdapterVerRuta adapterVerRuta;
    private Ruta ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ruta);
        Intent intent = getIntent();
        try {
            ruta=(Ruta)intent.getSerializableExtra("ruta");
        }catch (Exception e){

        }
        //Toast.makeText(this, linea, Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_ver_ruta_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterVerRuta = new AdapterVerRuta(this,ruta.getEstaciones());
        for(int i=0;i<ruta.getEstaciones().size();i++){
            adapterVerRuta.add_lista(ruta.getEstaciones().get(i));
        }
        firebaseON();
        recyclerView.setAdapter(adapterVerRuta);
    }

    public void firebaseON() {

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();//Catalogo de los productos

    }

    public void regresar(View view) {
        Intent intent=new Intent(this,RutasActivity.class);
        finish();
        startActivity(intent);
    }

}
