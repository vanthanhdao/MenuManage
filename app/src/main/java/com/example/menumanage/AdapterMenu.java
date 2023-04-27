package com.example.menumanage;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdapterMenu extends ArrayAdapter<Menu> implements Filterable {
    ArrayList<Menu> menuList = new ArrayList<Menu>();
    public AdapterMenu(@NonNull Context context, int resource, @NonNull ArrayList<Menu> objects) {
        super(context, resource, objects);
        menuList=objects;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.mymenu,null);
        TextView nameMenu = (TextView) v.findViewById(R.id.txtnamemenu);
        ImageView imgMenu= (ImageView) v.findViewById(R.id.imgmenu);


        //Gan gia tri list view theo position
        Menu Menu = menuList.get(position);

        nameMenu.setText(Menu.getName_menu());

        String imagePath = Menu.getImage_menu();
        Uri uri = Uri.parse(imagePath);
        imgMenu.setImageURI(uri);

        return v;
    }

}
