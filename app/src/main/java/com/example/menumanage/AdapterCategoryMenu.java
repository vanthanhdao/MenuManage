package com.example.menumanage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class  AdapterCategoryMenu extends ArrayAdapter<CategoryMenu> implements Filterable {
    Context mContext;
    ArrayList<CategoryMenu> categorymenuList = new ArrayList<CategoryMenu>();
    SQLiteDatabase db;
    public AdapterCategoryMenu(@NonNull Context context, int resource, @NonNull ArrayList<CategoryMenu> objects) {
        super(context, resource, objects);
        categorymenuList=objects;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        //LayoutInflater đọc xml layout file và chuyển đổi các thuộc tính của nó thành 1 View trong Java

        if(v==null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.mycategrymenu, null);
        }
        ImageView image_item = (ImageView) v.findViewById(R.id.txtimage_item);
        TextView name_item = (TextView)v.findViewById(R.id.txtname_item);

        //Gan gia tri list view theo position
        if(categorymenuList.size()!=0) {
            CategoryMenu categoryMenu = categorymenuList.get(position);
            name_item.setText(categorymenuList.get(position).getName_categorymenu());
            String imagePath = categoryMenu.getImage_categorymenu();
            Uri uri = Uri.parse(imagePath);
            image_item.setImageURI(uri);
        }


        return v;
    }



}
