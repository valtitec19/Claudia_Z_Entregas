package com.clauzon.clauzentregas.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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

public class ToolsFragment extends Fragment {

    private Button boton;
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
        return root;
    }


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