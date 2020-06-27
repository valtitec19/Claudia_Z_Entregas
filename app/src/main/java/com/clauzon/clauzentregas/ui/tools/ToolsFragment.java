package com.clauzon.clauzentregas.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.clauzon.clauzentregas.LoginActivity;
import com.clauzon.clauzentregas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ToolsFragment extends Fragment {

    private Button boton,notiicacion;
    private ToolsViewModel toolsViewModel;
    FirebaseUser currentUser ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        boton=(Button)root.findViewById(R.id.boton_cerrar_sesion);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                borrar_token_dispositivo();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

//        notiicacion=(Button)root.findViewById(R.id.notificacion);
//        notiicacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        return root;
    }
//
//    private void enviar_notificacion(String token) {
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("token",token);
//            JSONObject notificacion = new JSONObject();
//            notificacion.put("titulo", "prueba 1 cliente");
//            notificacion.put("detalle", "detalle cliente");
//            jsonObject.put("data",notificacion);
//
//            String URL = "https://fcm.googleapis.com/fcm/send";
//
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,jsonObject,null,null){
//                @Override
//                public Map<String, String> getHeaders()  {
//                    Map<String,String> header = new HashMap<>();
//                    header.put("content-type", "application/json");
//                    header.put("authorization", "key=AAAAE3HNDFU:APA91bEmPKbwtdaQIrU9g2GmxBEwy7zqHzdwG-L3I7o6HzrKhJ5BupTBTqhN67ytbObOv_NUILcDMaG-HwCLi2tEFKDwOWShs14ZOGpWZOh2DJNhxwjAQIfPtWgn7sxWuDR9VfT4uPQW");
//                    return header;
//                }
//            };
//            requestQueue.add(request);
//
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }


    private void borrar_token_dispositivo() {

        if(currentUser!=null && currentUser.isEmailVerified()){
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
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("token").child(currentUser.getUid());
                            reference.removeValue();

                        }
                    });
        }
    }
}