package com.clauzon.clauzentregas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.Clases.Repartidor;
import com.clauzon.clauzentregas.Clases.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private String url_foto = "";
    private Boolean val1 = false, val2 = false;
    private EditText txt_nombre, txt_apellido, txt_celular, txt_correo, txt_pass, txt_pass_confirm, txt_direccion, tarjeta;
    private Spinner s_dias, s_meses, s_años, s_genero, s_hora_inicio, s_hora_fin;
    private Button registrar;
    private CircleImageView circleImageView;
    private CheckBox checkBox;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Repartidor repartidor;
    private FirebaseDatabase database;
    private String numero_tarjeta;
    private String email, password, nombre, apellido, telefono, d, m, a, fecha, genero, id, hora_inicio, hora_fin, direccion;
    private List<String> entregas = new ArrayList<>();
    private List<String> cobertura = new ArrayList<>();
    private ImageView ine_frontal,ine_trasera;
    private ArrayList<String> imagenes =new ArrayList<>();
    private int foto=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        requestMultiplePermissions();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        //Inicio_Spinners();
        inicio_views();
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numero_tarjeta=tarjeta.getText().toString();
                telefono = txt_celular.getText().toString();
                apellido = txt_apellido.getText().toString();
                nombre = txt_nombre.getText().toString();
                email = txt_correo.getText().toString();
                direccion = txt_direccion.getText().toString();
                fecha = d + "/" + m + "/" + a;
                if (isValidEmail(email)) {
                    if (validarPass()) {
                        if (!nombre.isEmpty() && !apellido.isEmpty() && valida_numero(telefono)) {
                            if (checkBox.isChecked()) {
                                if (url_foto.isEmpty() ) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                                    builder.setTitle("Datos incompletos").setMessage("Por favor cargue una foto de perfil");
                                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(RegistroActivity.this, "Registro incompleto", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.create().show();
                                } else {
                                    repartidor = new Repartidor(nombre, apellido, email, telefono, "", direccion, "", "", false, entregas, cobertura,imagenes,numero_tarjeta,"");
                                    Intent i = new Intent(RegistroActivity.this, ConfirmarRegistroActivity.class);
                                    i.putExtra("repartidor", repartidor);
                                    i.putExtra("pass",txt_pass.getText().toString());
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                Toast.makeText(RegistroActivity.this, "Debes aceptar los Terminos y Condiciones", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistroActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistroActivity.this, "Las contraseñas deben ser identicas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, "correo electronico no valido", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void correo_existente(final String correo_test){

        DatabaseReference reference = database.getReference();
        reference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Usuario usuario = ds.getValue(Usuario.class);
                    if(usuario.getCorreo().equals(correo_test)){
                        Toast.makeText(RegistroActivity.this, "El correo que ingresó se encuentra asociado a otra cuenta", Toast.LENGTH_SHORT).show();
                        txt_correo.setError("Correo existente");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Repartidores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Repartidor repartidor = ds.getValue(Repartidor.class);
                    if(repartidor.getCorreo().equals(correo_test)){
                        Toast.makeText(RegistroActivity.this, "El correo que ingresó se encuentra asociado a una cuenta de cliente", Toast.LENGTH_SHORT).show();}
                    txt_correo.setError("Correo existente");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean isValidEmail(CharSequence target) {
        correo_existente(txt_correo.getText().toString());
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private Boolean validarPass() {
        String pass1, pass2;
        pass1 = txt_pass.getText().toString();
        pass2 = txt_pass_confirm.getText().toString();
        if (pass1.equals(pass2)) {
            if (pass1.length() > 6 && pass1.length() < 20) {
                return true;
            } else {
                Toast.makeText(this, "La contraseña debe tener entre 7 y 20 caracteres", Toast.LENGTH_SHORT).show();
                return false;

            }
        } else {
            return false;
        }
    }

//    public void Inicio_Spinners() {
//        s_hora_inicio = (Spinner) findViewById(R.id.spinner_hora_inicio_registro);
//        s_hora_fin = (Spinner) findViewById(R.id.spinner_hora_fin_registro);
//        ArrayAdapter<CharSequence> horario = ArrayAdapter.createFromResource(this, R.array.horario, android.R.layout.simple_spinner_item);
//        s_hora_inicio.setAdapter(horario);
//        s_hora_fin.setAdapter(horario);
//
//        s_hora_inicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                hora_inicio = adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        s_hora_fin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                hora_fin = adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//    }

    public Boolean valida_numero(String numero) {
        if (numero.length() == 10) {
            return true;
        } else {
            return false;
        }
    }

    public void inicio_views() {
        tarjeta=(EditText)findViewById(R.id.tarjeta);
        checkBox = (CheckBox) findViewById(R.id.terminos_y_condiciones_registro);
        txt_nombre = (EditText) findViewById(R.id.nombre_registro);
        txt_apellido = (EditText) findViewById(R.id.apellido_registro);
        txt_celular = (EditText) findViewById(R.id.telefono_registro);
        txt_correo = (EditText) findViewById(R.id.correo_registro);
        txt_pass = (EditText) findViewById(R.id.contraseña_registro);
        txt_pass_confirm = (EditText) findViewById(R.id.contraseña_registro_confirmada);
        txt_direccion = (EditText) findViewById(R.id.direccion_registro);
        progressDialog = new ProgressDialog(this);
        circleImageView = (CircleImageView) findViewById(R.id.foto_registro);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foto=0;
                showPictureDialog();
            }
        });
        registrar = (Button) findViewById(R.id.enviar_registro);
        ine_frontal=(ImageView)findViewById(R.id.id_frontal);
        ine_frontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imagenes.size()>0){
                    foto=1;
                    showPictureDialog();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Datos incompletos").setMessage("Haz click sobre el rostro sonriente para subir una foto de perfil");
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(RegistroActivity.this, "Registro incompleto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                }
            }
        });
        ine_trasera=(ImageView)findViewById(R.id.id_trasera);
        ine_trasera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagenes.size()>1){
                    foto=2;
                    showPictureDialog();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Datos incompletos").setMessage("Por favor cargue una foto frontal de su INE");
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(RegistroActivity.this, "Registro incompleto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }

    //////////////////////*******************/////////////////////*****************
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Cargar foto de perfil");
        String[] pictureDialogItems = {
                "Subir foto desde galeria",
                "Tomar nueva foto"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                progressDialog.setTitle("Cargar foto de perfil");
                progressDialog.setMessage("Por favor espere");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Uri contentURI = data.getData();
                storageReference = firebaseStorage.getReference("IMAGENES REPARTIDORES");
                final StorageReference foto_subida = storageReference.child(contentURI.getLastPathSegment());
                foto_subida.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                url_foto = uri.toString();
                                imagenes.add(url_foto);
                                progressDialog.dismiss();
                                if(foto==0){
                                    Glide.with(RegistroActivity.this).load(imagenes.get(0)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
                                    Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();
                                }
                                if(foto==1){
                                    Glide.with(RegistroActivity.this).load(imagenes.get(1)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(ine_frontal);
                                    Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();
                                }else if(foto==2){
                                    Glide.with(RegistroActivity.this).load(imagenes.get(2)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(ine_trasera);
                                    Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
//                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    imageButton.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            progressDialog.setTitle("Subiendo foto ");
            progressDialog.setMessage("Por favor espere");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
            Uri uri = getImageUri(this, thumbnail);
            storageReference = firebaseStorage.getReference("IMAGENES CATALOGO PRODUCTOS");
            final StorageReference foto_subida = storageReference.child(uri.getLastPathSegment());
            foto_subida.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            url_foto = uri.toString();
                            imagenes.add(url_foto);
                            progressDialog.dismiss();
                            if(foto==0){
                                Glide.with(RegistroActivity.this).load(imagenes.get(0)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
                                Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();
                            }
                            if(foto==1){
                                Glide.with(RegistroActivity.this).load(imagenes.get(1)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(ine_frontal);
                                Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();
                            }else if(foto==2){
                                Glide.with(RegistroActivity.this).load(imagenes.get(2)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(ine_trasera);
                                Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            });


            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void licencia(View view) {

    }
}
