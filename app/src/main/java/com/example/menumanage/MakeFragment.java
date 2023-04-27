package com.example.menumanage;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MakeFragment extends Fragment {


    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_make, container, false);

        TextView makemenu = (TextView) v.findViewById(R.id.txtmakeM);
        VideoView videomenu = (VideoView)  v.findViewById(R.id.videoView);


        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                Menu menu = (Menu) bundle.getSerializable("menu");
                String make = menu.getMake_menu();
                makemenu.setText(make);


//                String videoPath = menu.vedio_menu;
//                Uri uri = Uri.parse(videoPath);
//                videomenu.setVideoURI(uri);
//                videomenu.start();
//
//                MediaController mediaController = new MediaController(getActivity());
//                videomenu.setMediaController(mediaController);
//                mediaController.setAnchorView(videomenu);


            }
        }catch(Exception exception){
            Toast.makeText(getActivity(), "Lỗi "+ exception.getMessage(), Toast.LENGTH_LONG).show();
        }









        return v;
    }
}