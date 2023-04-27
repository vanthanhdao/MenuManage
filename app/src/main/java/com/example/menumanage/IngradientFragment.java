package com.example.menumanage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class IngradientFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_ingradient, container, false);
        TextView ingradient = (TextView) v.findViewById(R.id.txtingradientM);
        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                Menu menu = (Menu) bundle.getSerializable("menu");
                String ingredient = menu.getIngredient_menu();
                ingradient.setText(ingredient );

            }
        }catch(Exception exception){
            Toast.makeText(getActivity(), "Lỗi "+ exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return v;
    }
}