package com.clauzon.clauzentregas.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzentregas.Clases.AdapterPedidos;
import com.clauzon.clauzentregas.Clases.Pedidos;
import com.clauzon.clauzentregas.FinalizarPedido;
import com.clauzon.clauzentregas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private TextView no_pedidos;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private AdapterPedidos adapterPedidos, adapterPedidos2;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private String id;
    private Spinner spinner;
    private String seleccion;
    private ArrayList<Pedidos> lista = new ArrayList<>();

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseON();
        verifica_login();
        verifica_pedidos_general();
        no_pedidos = (TextView) root.findViewById(R.id.no_pedidos);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_pedidos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterPedidos = new AdapterPedidos(getContext());
        adapterPedidos2 = new AdapterPedidos(getContext());
        recyclerView.setAdapter(adapterPedidos);
        recyclerView.setHasFixedSize(true);
        rellena_recycler();
        adapterPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pedidos pedidos = adapterPedidos.get_list().get(recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(getActivity(), FinalizarPedido.class);
                intent.putExtra("pedido", pedidos);
                startActivity(intent);

            }
        });
        return root;
    }

    public void rellena_recycler() {
        databaseReference.child("Pedidos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                if (pedidos.getRepartidor_id().equals(id)) {
                    if (pedidos.getEstado().equals("Pago pendiente (En efectivo)") || pedidos.getEstado().equals("Pago Realizado")) {
                        adapterPedidos.add_pedido(pedidos);
                    }
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
    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

    }

    @Override
    public void onResume() {
        super.onResume();
        verifica_login();
    }

    public void verifica_login() {
        if (currentUser != null) {
            id = currentUser.getUid();
        }
    }

    public void verifica_pedidos_general() {

        databaseReference.child("Pedidos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                if (pedidos.getRepartidor_id().equals(id)) {
                    lista.add(pedidos);
                }

                if (lista.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    no_pedidos.setVisibility(View.VISIBLE);
                } else {
                    no_pedidos.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
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

//        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                for (DataSnapshot ds : dataSnapshot.getChildren()) {
////                    Pedidos pedidos = ds.getValue(Pedidos.class);
////                    if (pedidos.getRepartidor_id().equals(id)) {
////                        lista.add(pedidos);
////                    }
////                }
////                if (lista.size() == 0) {
////                    recyclerView.setVisibility(View.GONE);
////                    no_pedidos.setVisibility(View.VISIBLE);
////                } else {
////                    no_pedidos.setVisibility(View.GONE);
////                    recyclerView.setVisibility(View.VISIBLE);
////                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}