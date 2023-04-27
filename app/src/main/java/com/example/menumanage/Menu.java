package com.example.menumanage;

import java.io.Serializable;

public class Menu implements Serializable {

    String id_menu;
    String id_categorymenu;
    String name_menu;
    String image_menu;
    String decreption_menu;
    String ingredient_menu;
    String make_menu;
    String vedio_menu;



    public Menu(String id_menu, String id_categorymenu, String name_menu, String image_menu, String decreption_menu, String ingredient_menu, String make_menu, String vedio_menu ) {
        this.id_menu = id_menu;
        this.id_categorymenu = id_categorymenu;
        this.name_menu = name_menu;
        this.image_menu = image_menu;
        this.decreption_menu = decreption_menu;
        this.ingredient_menu = ingredient_menu;
        this.make_menu = make_menu;
        this.vedio_menu = vedio_menu;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getId_categorymenu() {
        return id_categorymenu;
    }

    public void setId_categorymenu(String id_categorymenu) {
        this.id_categorymenu = id_categorymenu;
    }


    public String getName_menu() {
        return name_menu;
    }

    public void setName_menu(String name_menu) {
        this.name_menu = name_menu;
    }

    public String getImage_menu() {
        return image_menu;
    }

    public void setImage_menu(String image_menu) {
        this.image_menu = image_menu;
    }

    public String getDecreption_menu() {
        return decreption_menu;
    }

    public void setDecreption_menu(String decreption_menu) {
        this.decreption_menu = decreption_menu;
    }

    public String getIngredient_menu() {
        return ingredient_menu;
    }

    public void setIngredient_menu(String ingredient_menu) {
        this.ingredient_menu = ingredient_menu;
    }

    public String getMake_menu() {
        return make_menu;
    }

    public void setMake_menu(String make_menu) {
        this.make_menu = make_menu;
    }
    public String getVedio_menu() {
        return vedio_menu;
    }

    public void setVedio_menu(String vedio_menu) {
        this.vedio_menu = vedio_menu;
    }
    @Override
    public String toString() {
        return name_menu;
    }
}
