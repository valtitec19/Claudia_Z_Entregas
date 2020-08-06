package com.clauzon.clauzentregas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AvisoPrivacidadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso_privacidad);
    }

    public void regresar_registro(View view) {
        startActivity(new Intent(AvisoPrivacidadActivity.this,RegistroActivity.class));
        finish();
    }
}
