package com.clauzon.clauzentregas.ui.utilidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.clauzon.clauzentregas.R;

public class UtilidadesFragment extends Fragment {

    private UtilidadesViewModel utilidadesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        utilidadesViewModel =
                ViewModelProviders.of(this).get(UtilidadesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_utilidades, container, false);
        return root;
    }
}