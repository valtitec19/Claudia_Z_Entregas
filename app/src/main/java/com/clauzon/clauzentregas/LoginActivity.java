package com.clauzon.clauzentregas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clauzon.clauzentregas.Clases.Repartidor;
import com.clauzon.clauzentregas.Clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText txt_correo,txt_pass;
    private Button btn_iniciar,btn_registrar;
    private String email,password;
    private FirebaseAuth mAuth;// ...
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_correo=(EditText)findViewById(R.id.correo_login);
        txt_pass=(EditText)findViewById(R.id.pass_login);
        btn_iniciar=(Button)findViewById(R.id.iniciar_login);
        btn_registrar=(Button)findViewById(R.id.registro_login);
        //startService(new Intent(this, MyService.class));
        firebaseON();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null && currentUser.isEmailVerified()){
            nextActivity();
        }

    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void registro(View view) {
        Intent intent=new Intent(LoginActivity.this,RegistroActivity.class);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null && currentUser.isEmailVerified()){
            nextActivity();
        }
        //updateUI(currentUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null && currentUser.isEmailVerified()){
            nextActivity();
        }
    }

    private void nextActivity() {
        startActivity(new Intent(LoginActivity.this,NavDrawerActivity.class));
        finish();
    }

    public void iniciar_sesion(View view) {
        //txt_pass.setFocusable(false);
        txt_correo.setEnabled(false);
        txt_pass.setEnabled(false);
        //txt_correo.setFocusable(false);
        email=txt_correo.getText().toString();
        password=txt_pass.getText().toString();
        if(isValidEmail(email)&&validarPass()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(mAuth.getCurrentUser().isEmailVerified()){
                                    databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                Usuario usuario=ds.getValue(Usuario.class);
                                                if(usuario.getCorreo().equals(txt_correo.getText().toString())){
                                                    Toast.makeText(LoginActivity.this, "Este correo pertenece a una cuenta de Cliente", Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                                                }else{
                                                    nextActivity();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }else{
                                    txt_correo.setEnabled(true);
                                    txt_pass.setEnabled(true);
                                    Toast.makeText(LoginActivity.this, "Verifica tu correo en tu bandeja de entrada", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                txt_correo.setEnabled(true);
                                txt_pass.setEnabled(true);
                                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }else {
            Toast.makeText(LoginActivity.this, "contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private Boolean validarPass(){
        String pass1;
        pass1=txt_pass.getText().toString();
        if(pass1.length()>6&&pass1.length()<20){
            return true;
        }else{return false;}
    }


    public void olvide_password(View view) {
        if(isValidEmail(txt_correo.getText().toString())){
            databaseReference.child("Repartidores").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Usuario usuario = ds.getValue(Usuario.class);
                        if(usuario.getCorreo().equals(txt_correo.getText().toString())){
                            mAuth.sendPasswordResetEmail(txt_correo.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Se envío un enlace para restablecer tu contraseña al correo que registraste.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "El correo no exite", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(LoginActivity.this, "El correo no existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {

            txt_correo.setError("Ingrese un correo valido");
        }
    }
}
