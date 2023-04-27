package com.example.menumanage;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    TextView save;
    CircleImageView  img;
    EditText fullname,email,password;
    Button logout;
    String id_account;
    SQLiteDatabase db;
    ImageButton imageButton;
    boolean isEnable;

    ArrayList<Account> account = new ArrayList<Account>();
    public static final int REQUEST_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_profile, container, false);

        save = (TextView) v.findViewById(R.id.txtSave_account);
        img = (CircleImageView) v.findViewById(R.id.imgView_icon_account);
        fullname = (EditText) v.findViewById(R.id.edtFullName_account);
        email = (EditText) v.findViewById(R.id.edtEmail_account);
        password = (EditText) v.findViewById(R.id.edtPassword_account);
        logout = (Button) v.findViewById(R.id.btnLogout_account);
        imageButton = (ImageButton) v.findViewById(R.id.imageButton);



        getData();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
                save.setText("Lưu");

            }
        });

        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setText("Lưu");
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setText("Lưu");
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setText("Lưu");
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveAccount()) {
                    db.close();
                    Intent intent1 = new Intent(getActivity(),Login.class);
                    startActivity(intent1);
                    Toast.makeText(getActivity(), "Cập nhật thông tin tài khoản thành công", Toast.LENGTH_LONG).show();
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Login.class);
                startActivity(intent);
            }
        });


        imageButton.setOnClickListener(v1 -> {
            if(!isEnable){
                isEnable=true;
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
                isEnable=false;
                password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        return v;
    }

    private void getData() {
        try {
            Bundle bundle =getArguments();
            String emailacc = bundle.getString("emailaccount");
            String passwordacc = bundle.getString("passwordaccount");
            db = getActivity().openOrCreateDatabase(Login.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor c = db.query("tblAccount", null, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                account.add(new Account(c.getString(0).toString(), c.getString(1).toString(),c.getString(2).toString(),c.getString(3).toString()
                        ,c.getString(4).toString(),c.getString(5).toString()));
                c.moveToNext();
            }


            for (int i=0;i<account.size();i++){
                String fullnamedata = account.get(i).getName_account();
                String Emaildata = account.get(i).getEmail_account();
                String Passworddata = account.get(i).getPassword_account();
                if(emailacc.equals(Emaildata) && passwordacc.equals(Passworddata)){
                    fullname.setText(fullnamedata);
                    email.setText(Emaildata);
                    password.setText(Passworddata);
                    id_account = account.get(i).getId_account();
                    String imagePath = account.get(i).getImage_account();
                    Uri uri = Uri.parse(imagePath);
                    img.setImageURI(uri);
                }

            }



        }catch(Exception exception){
            Toast.makeText(getActivity(), "Lỗi "+ exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            img.setImageURI(uri);
        }
    }

    public  boolean saveAccount(){
        try {
            //Xử lý thêm ảnh
            // Lấy ảnh từ ImageView
            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            // Lưu bitmap vào một tập tin mới trên thiết bị
            String fileName = "avatar" + System.currentTimeMillis() + ".jpg";
            FileOutputStream outputStream = null;
            try {
                outputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Lấy đường dẫn tuyệt đối của tập tin hình ảnh vừa lưu
            String imagePath = getActivity().getFilesDir().getAbsolutePath() + "/" + fileName;
            // Lưu đường dẫn tới tập tin hình ảnh vào biến imageUri
            Uri imageUri = Uri.fromFile(new File(imagePath));

            //Lưu vào databse
            db = getContext().openOrCreateDatabase ("menu.db",Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("fullname_account",fullname.getText().toString());
            values.put("email_account", email.getText().toString());
            values.put("password_account",password.getText().toString());
            values.put("image_account",imageUri.toString());
            if (db.update("tblAccount", values, "id_account=?", new String[]{id_account}) != -1)
            {return true;}
        }catch(Exception exception){
            exception.getMessage();
        }
        return false;
    }

}