package com.clauzon.clauzentregas.ui.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzentregas.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private CircleImageView red1,red2,logo;
    private TextView txt1,txt2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        red1=(CircleImageView)root.findViewById(R.id.foto_red1);
        red2=(CircleImageView)root.findViewById(R.id.foto_red2);
        txt1=(TextView)root.findViewById(R.id.txt1_redes);
        txt2=(TextView)root.findViewById(R.id.txt2_redes);
        logo=(CircleImageView)root.findViewById(R.id.imagen_claudia_logo_redes);
        Glide.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/clauzon.appspot.com/o/RECURSOS%2Ffacebook-logo-966BBFBC34-seeklogo.com.png?alt=media&token=26a37b2b-45cb-40ec-86d4-58bdce8fef19").centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(red1);
        Glide.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/clauzon.appspot.com/o/RECURSOS%2Ffacebook-logo-966BBFBC34-seeklogo.com.png?alt=media&token=26a37b2b-45cb-40ec-86d4-58bdce8fef19").centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(red2);
        Glide.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/clauzon.appspot.com/o/RECURSOS%2FCZON-LogoApp-01.jpg?alt=media&token=95aec94d-854e-42bd-b7c3-d7411656b32b").centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(logo);
        red1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar_a_red("https://www.facebook.com/claudia.zentenogaldamez");
            }
        });

        red2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar_a_red("https://www.facebook.com/167633064159165/");
            }
        });

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar_a_red("https://www.facebook.com/claudia.zentenogaldamez");
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar_a_red("https://www.facebook.com/167633064159165/");
            }
        });

        return root;
    }

    private void enviar_a_red(String url){
        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}