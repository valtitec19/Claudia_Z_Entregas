package com.clauzon.clauzentregas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.clauzon.clauzentregas.Clases.Repartidor;

import java.util.ArrayList;

public class LineasActivity extends AppCompatActivity {

    private CheckBox l1,l2,l3,l4,l5,l6,l7,l8,l9,l12,la,lb;
    private ArrayList<String> lineas=new ArrayList<>();
    private Button aceptar;
    private Repartidor repartidor;
    private String temp_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas);
        iniciar_checkboxes();
        Intent i = getIntent();
        Bundle extras = getIntent().getExtras();
        repartidor = (Repartidor) i.getSerializableExtra("repartidor");
        temp_pass=i.getStringExtra("pass");
        aceptar=(Button)findViewById(R.id.btn_aceptar_lineas);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifica_checabok();

            }
        });
    }

    public void iniciar_checkboxes(){
        l1=(CheckBox)findViewById(R.id.linea1);
        l2=(CheckBox)findViewById(R.id.linea2);
        l3=(CheckBox)findViewById(R.id.linea3);
        l4=(CheckBox)findViewById(R.id.linea4);
        l5=(CheckBox)findViewById(R.id.linea5);
        l6=(CheckBox)findViewById(R.id.linea6);
        l7=(CheckBox)findViewById(R.id.linea7);
        l8=(CheckBox)findViewById(R.id.linea8);
        l9=(CheckBox)findViewById(R.id.linea9);
        l12=(CheckBox)findViewById(R.id.linea12);
        la=(CheckBox)findViewById(R.id.lineaA);
        lb=(CheckBox)findViewById(R.id.lineaB);

    }

    public void verifica_checabok(){
        if(!l1.isChecked() && !l2.isChecked() && !l3.isChecked() && !l4.isChecked() && !l5.isChecked() && !l6.isChecked() && !l7.isChecked()
                && !l8.isChecked() && !l9.isChecked() && !l12.isChecked() && !la.isChecked() && !lb.isChecked()){

            AlertDialog.Builder builder = new AlertDialog.Builder(LineasActivity.this);
            builder.setTitle("Opcion no valida").setMessage("Debes seleecionar al menos una linea del metro");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(LineasActivity.this, "Registro incompleto", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LineasActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();

        }else{
            if(l1.isChecked()){
                repartidor.getCobertura().add("Linea 1");
            }
            if(l2.isChecked()){
                repartidor.getCobertura().add("Linea 2");
            }
            if(l3.isChecked()){
                repartidor.getCobertura().add("Linea 3");
            }
            if(l4.isChecked()){
                repartidor.getCobertura().add("Linea 4");
            }
            if(l5.isChecked()){
                repartidor.getCobertura().add("Linea 5");
            }
            if(l6.isChecked()){
                repartidor.getCobertura().add("Linea 6");
            }
            if(l7.isChecked()){
                repartidor.getCobertura().add("Linea 7");
            }
            if(l8.isChecked()){
                repartidor.getCobertura().add("Linea 8");
            }
            if(l9.isChecked()){
                repartidor.getCobertura().add("Linea 9");
            }
            if(l12.isChecked()){
                repartidor.getCobertura().add("Linea 12");
            }
            if(la.isChecked()){
                repartidor.getCobertura().add("Linea A");
            }
            if(lb.isChecked()){
                repartidor.getCobertura().add("Linea B");
            }
            Intent i= new Intent(LineasActivity.this, ConfirmarRegistroActivity.class);
            i.putExtra("repartidor",repartidor);
            i.putExtra("pass",temp_pass);
            startActivity(i);


        }
    }
}
