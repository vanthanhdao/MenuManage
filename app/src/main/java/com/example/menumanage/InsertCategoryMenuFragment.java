package com.example.menumanage;

import static android.app.Activity.RESULT_OK;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.ContentValues;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class InsertCategoryMenuFragment extends Fragment {


    SQLiteDatabase db;
    Button uploadImage,saveCM,closeCM;
    EditText nameCM;
    ImageView img;

    public static final int REQUEST_CODE = 100;
    public static final int SAVE_CLASS = 115;
    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_insert_category_menu, container, false);
        uploadImage = (Button) v.findViewById(R.id.btnUploadImage);
        saveCM = (Button) v.findViewById(R.id.btnSaveCM);
        closeCM = (Button) v.findViewById(R.id.btnCloseCM);
        img = (ImageView) v.findViewById(R.id.imageView);
        nameCM = (EditText) v.findViewById(R.id.edtNameCM);


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);

            }
        });


        saveCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long id = saveCategoryMenu();
                Bundle bundle = new Bundle();
                Intent intent = getActivity().getIntent();

                if (id != -1) { // = -1 Thém khong thanh cong
                    if(nameCM.getText()!=null ){
                        CategoryMenu r = new CategoryMenu(id + "", nameCM.getText().toString(), img.toString());
                        bundle.putSerializable("room", r);
                        intent.putExtra("data", bundle);
                        getActivity().setResult(InsertCategoryMenuFragment.SAVE_CLASS, intent);
                        Toast.makeText(getActivity(), "Thêm danh mục món ăn thành công", Toast.LENGTH_LONG).show();
                        db.close();
                        Intent intent1 = new Intent(getActivity(),HomePage.class);
                        startActivity(intent1);
                    }
                } else{Toast.makeText(getActivity(), "Bạn chưa nhập đủ thông tin!!!", Toast.LENGTH_SHORT).show();}


            }
        });

        closeCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),HomePage.class);
                startActivity(intent1);
            }
        });



        return v;


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            img.setImageURI(uri);
        }
    }


    public   long saveCategoryMenu(){
        try {

            // Lấy ảnh từ ImageView
            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            Bitmap bitmap = drawable.getBitmap();


            // Lưu bitmap vào một tập tin mới trên thiết bị
            String fileName = "food_image_" + System.currentTimeMillis() + ".jpg";
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

            db = getContext().openOrCreateDatabase ("menu.db",Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();

            values.put("name_categorymenu", nameCM.getText().toString());
            values.put("image_categorymenu",imageUri.toString());
            long id =db.insert("tblCategoryMenu", null, values);
            db.close();
            if(id!=-1)
            {
                return id;
            }



        }catch(Exception exception){

            exception.getMessage();

        }
        return -1;
    }




}
