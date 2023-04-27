package com.example.menumanage;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DecreptionFragment extends Fragment {




    private String name;
    private String decreption;
    String id_menu;
    SQLiteDatabase db;
    ArrayList<Menu> MenuList = new ArrayList<Menu>();
    AdapterMenu adapterMenu;
    TextView nameM,decreptionM;
    String id_categorymenu;
    public static final int REQUEST_CODE = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_decreption, container, false);


       decreptionM = (TextView) v.findViewById(R.id.txtdecreptionM);




        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                Menu menu = (Menu) bundle.getSerializable("menu");
                String id = menu.getId_menu();
                String decreption = menu.getDecreption_menu();
                decreptionM.setText(decreption);

            }
        }catch(Exception exception){
            Toast.makeText(getActivity(), "Lỗi "+ exception.getMessage(), Toast.LENGTH_LONG).show();
        }





        return v;
    }







}