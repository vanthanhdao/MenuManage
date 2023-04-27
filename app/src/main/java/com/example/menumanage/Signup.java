package com.example.menumanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.ContentValues;



public class Signup extends AppCompatActivity {

    private static final String DATABASE_NAME = "menu.db";
    SQLiteDatabase db;
    Button btnsignup;
    EditText edtfullname,edtemail,edtpassword,edtre_password;
    ImageButton imgbtn2,imgbtn1;

    boolean isEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Anh xa Widget
        InitWidget();

        //Tao Database
        InitDB();

        //Xu ly su kien
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fullname = edtfullname.getText().toString();
                String Email = edtemail.getText().toString();
                String Password = edtpassword.getText().toString();
                String RePassword = edtre_password.getText().toString();
                db = openOrCreateDatabase (DATABASE_NAME, MODE_PRIVATE, null);
                String sql;
                if(Fullname.isEmpty()){
                    Toast.makeText(Signup.this, "Bạn chưa nhập họ và tên", Toast.LENGTH_SHORT).show();
                    edtfullname.requestFocus();
                }else  if (Email.isEmpty()){
                    Toast.makeText(Signup.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                    edtemail.requestFocus();
                }else  if (RePassword.isEmpty()){
                    Toast.makeText(Signup.this, "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    edtre_password.requestFocus();
                }else  if (Password.isEmpty()){
                    Toast.makeText(Signup.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    edtpassword.requestFocus();
                }else  if (RePassword.equals(Password)){

                    saveSignup_Info();
                    Intent intent= new Intent(Signup.this,Login.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Signup.this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    edtre_password.requestFocus();
                }

            }
        });

        imgbtn1.setOnClickListener(v -> {
            if(!isEnable){
                isEnable=true;
                edtpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
                isEnable=false;
                edtpassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        imgbtn2.setOnClickListener(v -> {
            if(!isEnable){
                isEnable=true;
                edtre_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
                isEnable=false;
                edtre_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

    }

    //Kiểm tra database
    private void InitDB() {
        db = openOrCreateDatabase (DATABASE_NAME, MODE_PRIVATE, null);
        String sql;
        try{
            if(!isTableExists (db, "tblAccount")){
                sql =  "CREATE TABLE tblAccount (" ;
                sql += "id_account INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
                sql += "fullname_account TEXT ," ;
                sql += "email_account TEXT NOT NULL," ;
                sql += "password_account TEXT NOT NULL," ;
                sql += "repassword_account TEXT ," ;
                sql += "image_account TEXT );" ;
                db.execSQL (sql);
                sql ="insert into tblAccount (email_account,password_account) values ('admin@gmail.com','admin')" ;
                db.execSQL (sql);
            }
            if(!isTableExists (db, "tblMenu")) {
                sql =  "CREATE TABLE tblMenu (" ;
                sql += "id_menu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"   ;
                sql += "id_categorymenu INTEGER NOT NULL,"    ;
                sql += "name_menu TEXT,"   ;
                sql += "image_menu TEXT,"   ;
                sql += "decreption_menu TEXT,"   ;  //Chi tiet mon an
                sql += "ingredient_menu TEXT,"   ;  //Nguyen lieu mon an
                sql += "make_menu TEXT),"   ;   //Cach lam món an
                sql += "video_menu TEXT);"   ;
                db.execSQL(sql);
            }
            if(!isTableExists (db, "tblCategoryMenu")){
                sql=  "CREATE TABLE tblCategoryMenu ("   ;
                sql+= "id_categorymenu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," ;
                sql+= "name_categorymenu TEXT,"  ;
                sql+= "image_categorymenu BLOB);"   ;
                db.execSQL(sql);

            }

        }
        catch (Exception ex){
            Toast.makeText(this, "Khởi tạo dữ liệu không thành công",Toast.LENGTH_SHORT).show();
        }
    }

    //Kiểm tra bảng có tạo chưa
    private boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name" + "= '"+tableName+"'", null);
        if (cursor!=null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    //Anh1 xạ các Widget
    private void InitWidget() {
        btnsignup = (Button) findViewById(R.id.signupbtn_login);
        edtemail = (EditText) findViewById(R.id.email);
        edtpassword = (EditText) findViewById(R.id.password_login);
        edtfullname = (EditText) findViewById(R.id.email_login);
        edtre_password = (EditText) findViewById(R.id.repassword);
        imgbtn1 = (ImageButton) findViewById(R.id.imgbtnpass1);
        imgbtn2 = (ImageButton) findViewById(R.id.imgbtn2);
    }

    //Lưu thông tin đăng ký vào database
    private  void saveSignup_Info(){
        String Fullname = edtfullname.getText().toString();
        String Email = edtemail.getText().toString();
        String Password = edtre_password.getText().toString();
        String RePassword = edtre_password.getText().toString();
        try {
            ContentValues values = new ContentValues();
            values.put("fullname_account",Fullname);
            values.put("email_account",Email);
            values.put("password_account",Password);
            values.put("repassword_account",RePassword);
            db.insert("tblAccount",null,values);
        }catch (Exception ex){
            Toast.makeText(Signup.this, "Khởi tạo dữ liệu không thành công",Toast.LENGTH_SHORT).show();
        }

    }

}