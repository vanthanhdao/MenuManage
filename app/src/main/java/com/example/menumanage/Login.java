package com.example.menumanage;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    protected static final String DATABASE_NAME = "menu.db";
    SQLiteDatabase db;
    Button btnlogin,btnsignup;
    EditText edtemail,edtpassword;
    ImageButton imgbtnpass;
    boolean isEnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Anh xa Widget
        InitWidget();

        //Tao Database
        InitDB();


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent= new Intent(Login.this,HomePage.class);
                //startActivity(intent);
                String Email = edtemail.getText().toString();
                String Password = edtpassword.getText().toString();
                if(Email.isEmpty()){
                    Toast.makeText(Login.this, "Bạn chưa nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
                    edtemail.requestFocus(); //Di chuển trỏ chuột vào email
                }else  if (Password.isEmpty()){

                    Toast.makeText(Login.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    edtpassword.requestFocus(); //Di chuển trỏ chuột vào username
                }else  if (isUser(Email,Password)){
                    Intent intent = new Intent(Login.this,HomePage.class);
                    intent.putExtra("emailaccount",Email);
                    intent.putExtra("passwordaccount",Password);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(), "Tài khoản hoặc mật khẩu không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });

        imgbtnpass.setOnClickListener(v -> {
            if(!isEnable){
                isEnable=true;
                edtpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
                isEnable=false;
                edtpassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

    }




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
                sql ="insert into tblAccount (fullname_account,email_account,password_account,repassword_account,image_account) values ('Đào Văn Thanh','admin@gmail.com','admin','adminadmin','q')";
                db.execSQL (sql);
            }
            if(!isTableExists (db, "tblMenu")) {
                sql =  "CREATE TABLE tblMenu (" ;
                sql += "id_menu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"   ;
                sql += "id_categorymenu INTEGER NOT NULL ,"   ;
                sql += "name_menu TEXT,"   ;
                sql += "image_menu TEXT,"   ;
                sql += "decreption_menu TEXT,"   ;  //Chi tiet mon an
                sql += "ingredient_menu TEXT,"   ;  //Nguyen lieu mon an
                sql += "make_menu TEXT,"   ;   //Cach lam món an
                sql += "video_menu TEXT);"   ;  //Video món ăn
                db.execSQL(sql);
            }
            if(!isTableExists (db, "tblCategoryMenu")){
                sql=  "CREATE TABLE tblCategoryMenu ("   ;
                sql+= "id_categorymenu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," ;
                sql+= "name_categorymenu TEXT,"  ;
                sql+= "image_categorymenu TEXT);"   ;
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

    private void InitWidget() {
        btnlogin = (Button) findViewById(R.id.loginbtn);
        btnsignup = (Button) findViewById(R.id.signupbtn_login);
        edtemail = (EditText) findViewById(R.id.email_login);
        edtpassword = (EditText) findViewById(R.id.password_login);
        imgbtnpass = (ImageButton) findViewById(R.id.imgbtnpass);
    }

    //Kiểm tra email và password
    private  boolean isUser(String email, String password){
        try {

            db= openOrCreateDatabase (DATABASE_NAME, MODE_PRIVATE, null);
            Cursor rawQuery = db.rawQuery ("select * from tblAccount where email_account=? and password_account =?",
                    new String[] {email, password});
            rawQuery.moveToFirst();
            if (rawQuery.getCount()>0)
                return true;
        }catch (Exception exception){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        return false;
    }











}