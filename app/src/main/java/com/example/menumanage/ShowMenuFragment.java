package com.example.menumanage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;


public class ShowMenuFragment extends Fragment {


    ArrayList<Menu> MenuList = new ArrayList<Menu>();
    AdapterMenu adapterMenu;
    SQLiteDatabase db;
    //Giữ vị trí trên listview
    int posselected = -1;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Menu dataObject;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_menu, container, false);
        tabLayout= (TabLayout) v.findViewById(R.id.tab_layout);

//      ImageView imgScale = (ImageView) v.findViewById(R.id.imageViewScale);
        VideoView videomenu = (VideoView)  v.findViewById(R.id.videoView);
        Animation animationScale = AnimationUtils.loadAnimation(getActivity(),R.anim.scale);



//        imgScale.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                v.startAnimation(animationScale);
//                return false;
//            }
//        });


        // Lấy dữ liệu từ bundle
        Bundle bundle = getArguments();
        Menu menu = (Menu) bundle.getSerializable("menu");
//        String imagePath = menu.getImage_menu();
//        Uri uri = Uri.parse(imagePath);
//        imgScale.setImageURI(uri);

        //Show video
        String videoPath = menu.vedio_menu;
        Uri uri = Uri.parse(videoPath);
        videomenu.setVideoURI(uri);
        videomenu.start();
        MediaController mediaController = new MediaController(getActivity());
        videomenu.setMediaController(mediaController);
        mediaController.setAnchorView(videomenu);
        // Đặt bundle vào Fragment B
        DecreptionFragment fragment = new DecreptionFragment();
        fragment.setArguments(bundle);
        // Thực hiện thay thế Fragment A bằng Fragment B
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_showmenu, fragment);
        fragmentTransaction.commit();



        // Click item của tablayout
         tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position==0){
                        // Lấy dữ liệu từ bundle
                        Bundle bundle = getArguments();
                        Menu menu = (Menu) bundle.getSerializable("menu");
                        // Đặt bundle vào Fragment B
                        DecreptionFragment fragment = new DecreptionFragment();
                        fragment.setArguments(bundle);
                        // Thực hiện thay thế Fragment A bằng Fragment B
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_showmenu, fragment);
                        fragmentTransaction.commit();
                    }
                    else if(position==1){

                        Bundle bundle = getArguments();
                        Menu menu = (Menu) bundle.getSerializable("menu");
                        IngradientFragment fragment = new IngradientFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_showmenu, fragment);
                        fragmentTransaction.commit();
                    }
                    else if(position==2){
                        Bundle bundle = getArguments();
                        Menu menu = (Menu) bundle.getSerializable("menu");;
                        MakeFragment fragment = new MakeFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_showmenu,fragment);
                        fragmentTransaction.commit();
                    }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
    }





}