package com.example.menumanage;

import java.io.Serializable;

public class Account implements Serializable {

    private  String id_account;
    private  String name_account;
    private String email_account;
    private String password_account;
    private String repassword_account;
    private String image_account;

    public Account(String id_account, String name_account, String email_account, String password_account, String repassword_account, String image_account) {
        this.id_account = id_account;
        this.name_account = name_account;
        this.email_account = email_account;
        this.password_account = password_account;
        this.repassword_account = repassword_account;
        this.image_account = image_account;
    }

    public String getId_account() {
        return id_account;
    }

    public void setId_account(String id_account) {
        this.id_account = id_account;
    }

    public String getName_account() {
        return name_account;
    }

    public void setName_account(String name_account) {
        this.name_account = name_account;
    }

    public String getImage_account() {
        return image_account;
    }

    public void setImage_account(String image_account) {
        this.image_account = image_account;
    }

    public String getEmail_account() {
        return email_account;
    }

    public void setEmail_account(String email_account) {
        this.email_account = email_account;
    }

    public String getPassword_account() {
        return password_account;
    }

    public void setPassword_account(String password_account) {
        this.password_account = password_account;
    }

    public String getRepassword_account() {
        return repassword_account;
    }

    public void setRepassword_account(String repassword_account) {
        this.repassword_account = repassword_account;
    }

    @Override
    public String toString() {
        return name_account;
    }
}
