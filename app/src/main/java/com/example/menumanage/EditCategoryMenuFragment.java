package com.example.menumanage;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class EditCategoryMenuFragment extends Fragment {

    SQLiteDatabase db;
    Button uploadImage,saveCM,closeCM,clearCM;
    EditText nameCM;
    ImageView img;
    String id_categorymenu;
    public static final int REQUEST_CODE = 100;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_category_menu, container, false);
        //Widget
        uploadImage = (Button) v.findViewById(R.id.btnUploadImageedit);
        saveCM = (Button) v.findViewById(R.id.btnSaveCMedit);
        closeCM = (Button) v.findViewById(R.id.btnCloseCMedit);
        img = (ImageView) v.findViewById(R.id.imgedit);
        nameCM = (EditText) v.findViewById(R.id.edtNameCMedit);
        clearCM = (Button) v.findViewById(R.id.btnClearCM);

        getData();

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

                Bundle bundle = new Bundle();
                Intent intent = getActivity().getIntent();
                if (saveCategoryMenu()) {
                    CategoryMenu r = new CategoryMenu(id_categorymenu, nameCM.getText().toString(), img.toString());
                    bundle.putSerializable("room", r);
                    intent.putExtra("data", bundle);
                    getActivity().setResult(HomePage.SAVE_CLASS,intent);
                    Toast.makeText(getActivity(), "Cập nhật danh mục món ăn thành công", Toast.LENGTH_LONG).show();
                    db.close();
                    Intent intent1 = new Intent(getActivity(),HomePage.class);
                    startActivity(intent1);
                }
            }
        });

        closeCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),HomePage.class);
                startActivity(intent1);
            }
        });

        clearCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameCM.setText("");
                img.setImageURI(null);
            }
        });
           return v;


}

    private  void getData(){
        Bundle bundle = getArguments();
        CategoryMenu categoryMenu = (CategoryMenu) bundle.getSerializable("room");
        id_categorymenu= categoryMenu.getId_categorymenu();
        nameCM.setText(categoryMenu.getName_categorymenu());
        String imagePath = categoryMenu.getImage_categorymenu();

        Uri uri = Uri.parse(imagePath);
        img.setImageURI(uri);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            img.setImageURI(uri);
        }
    }
    @SuppressLint("SuspiciousIndentation")
    public   boolean saveCategoryMenu(){
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
            db = getActivity().openOrCreateDatabase ("menu.db",Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("name_categorymenu", nameCM.getText().toString());
            values.put("image_categorymenu",imageUri.toString());
            if (db.update("tblCategoryMenu", values, "id_categorymenu=?", new String[]{id_categorymenu}) != -1)
            {return true;}
        }catch(Exception exception){

            exception.getMessage();
        }
        return false;
    }


}