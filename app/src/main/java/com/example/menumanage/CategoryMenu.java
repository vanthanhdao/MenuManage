package com.example.menumanage;

import android.graphics.Bitmap;

import java.io.Serializable;

public class CategoryMenu implements Serializable {

    private  String id_categorymenu;
    private  String name_categorymenu;
    private String image_categorymenu;


    public CategoryMenu(String id_categorymenu, String name_categorymenu, String image_categorymenu) {
        this.id_categorymenu = id_categorymenu;
        this.name_categorymenu = name_categorymenu;
        this.image_categorymenu = image_categorymenu;
    }

    public String getId_categorymenu() {
        return id_categorymenu;
    }

    public void setId_categorymenu(String id_categorymenu) {
        this.id_categorymenu = id_categorymenu;
    }

    public String getName_categorymenu() {
        return name_categorymenu;
    }

    public void setName_categorymenu(String name_categorymenu) {
        this.name_categorymenu = name_categorymenu;
    }

    public String getImage_categorymenu() {
        return image_categorymenu;
    }

    public void setImage_categorymenu(String image_categorymenu) {
        this.image_categorymenu = image_categorymenu;
    }

    @Override
    public String toString() {
        return  name_categorymenu ;
    }
}
