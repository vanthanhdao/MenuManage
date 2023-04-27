package com.example.menumanage;

import static android.app.Activity.RESULT_OK;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class EditMenuFragment extends Fragment {

    SQLiteDatabase db;
    Button uploadImage,saveM,closeM,clearM,uploadVideo;
    EditText nameM,decriptionM,ingradientM,makeM;
    ImageView img;
    VideoView video;
    Uri uri;
    String id_menu;
    String id_categorymenu;
    public static final int REQUEST_CODE = 100;
    public static final int REQUEST_CODE1 = 101;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_menu, container, false);
        //Widget
        uploadImage = (Button) v.findViewById(R.id.btnUploadImageeditM);
        saveM = (Button) v.findViewById(R.id.btnSaveeditM);
        closeM = (Button) v.findViewById(R.id.btnCloseeditM);
        clearM = (Button) v.findViewById(R.id.btnCleareditM);
        uploadVideo = (Button) v.findViewById(R.id.btnUploadVideoeditM);
        img = (ImageView) v.findViewById(R.id.imageVieweditM);
        video = (VideoView) v.findViewById(R.id.uploadvideoeditM);
        nameM = (EditText) v.findViewById(R.id.edtNameeditM);
        decriptionM = (EditText) v.findViewById(R.id.edtDecreptioneditM);
        ingradientM = (EditText) v.findViewById(R.id.edtIngradienteditM);
        makeM = (EditText) v.findViewById(R.id.edtMakeeditM);

        getData();


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
                intent.setType("video/*");
                startActivityForResult(intent, REQUEST_CODE1);

            }
        });

        closeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),HomePage.class);
                startActivity(intent1);
            }
        });

        clearM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameM.setText("");
                decriptionM.setText("");
                ingradientM.setText("");
                makeM.setText("");
                img.setImageURI(null);
                img.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                String videoPath ="";
                Uri uri1 = Uri.parse(videoPath);
                video.setVideoURI(uri1);


            }
        });

        saveM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                Intent intent = getActivity().getIntent();
                if (saveMenu()) {
                    if(nameM.getText()!=null && decriptionM.getText()!=null && ingradientM.getText()!=null && makeM.getText()!=null ){
                        Menu r = new Menu(id_menu,id_categorymenu, nameM.getText().toString(), img.toString(),decriptionM.getText().toString()
                                ,ingradientM.getText().toString(),makeM.getText().toString(),video.toString());
                        bundle.putSerializable("room", r);
                        intent.putExtra("data", bundle);
                        getActivity().setResult(InsertCategoryMenuFragment.SAVE_CLASS, intent);
                        Toast.makeText(getActivity(), "Cập nhậtc món ăn thành công", Toast.LENGTH_LONG).show();
                        db.close();
                        Intent intent1 = new Intent(getActivity(), HomePage.class);
                        startActivity(intent1);
                    }
                } else{Toast.makeText(getActivity(), "Bạn chưa nhập đủ thông tin!!!", Toast.LENGTH_SHORT).show();}
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            img.setImageURI(uri);
        }else if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            video.setVideoURI(uri);
            video.start();
            MediaController mediaController = new MediaController(getActivity());
            video.setMediaController(mediaController);
            mediaController.setAnchorView(video);


        }
    }
    private void getData() {
        Bundle bundle = getArguments();
        Menu menu = (Menu) bundle.getSerializable("room");
        nameM.setText(menu.getName_menu());
        String imagePath = menu.getImage_menu();
        Uri uri = Uri.parse(imagePath);
        img.setImageURI(uri);
        decriptionM.setText(menu.getDecreption_menu());
        ingradientM.setText(menu.getIngredient_menu());
        makeM.setText(menu.getMake_menu());
        String videoPath = menu.getVedio_menu();
        Uri uri1 = Uri.parse(videoPath);
        video.setVideoURI(uri1);
        video.start();
        MediaController mediaController = new MediaController(getActivity());
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);
    }

    public  boolean saveMenu(){
        try {
            //Xử lý thêm ảnh
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




            //Xy73 lý thêm video
            String videoFileName = null;
            if (uri.getScheme().equals("content")) {
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        videoFileName = cursor.getString(index);
                    }
                    cursor.close();
                }
            }
            if (videoFileName == null) {
                videoFileName = uri.getPath();
                int cut = videoFileName.lastIndexOf('/');
                if (cut != -1) {
                    videoFileName = videoFileName.substring(cut + 1);
                }
            }
            // Lấy đường dẫn tuyệt đối của tập tin video vừa lưu
            String videoPath = getContext().getFilesDir().getAbsolutePath() + "/" + videoFileName;
            // Sao chép nội dung của video được chọn vào tập tin mới
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                FileOutputStream OutputStream = new FileOutputStream(videoPath );
                IOUtils.copy(inputStream, OutputStream); // sử dụng thư viện Apache Commons IO
                OutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Lưu đường dẫn tới tập tin video vào biến videoUri
            Uri videoUri = Uri.fromFile(new File(videoPath));
            // Lưu video vào tập tin mới trên thiết bị
            String filename1 = videoFileName ;
            FileOutputStream videoOutputStream = null;




            //Lưu vào databse
            db = getContext().openOrCreateDatabase ("menu.db",Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            Bundle bundle = getArguments();
            Menu menu = (Menu) bundle.getSerializable("room");
            id_menu = menu.getId_menu();
            id_categorymenu = menu.getId_categorymenu();
            values.put("image_menu",imageUri.toString());
            values.put("name_menu", nameM.getText().toString());
            values.put("decreption_menu",decriptionM.getText().toString());
            values.put("ingredient_menu",ingradientM.getText().toString());
            values.put("make_menu",makeM.getText().toString());
            values.put("video_menu",videoUri.toString());
            values.put("id_categorymenu",id_categorymenu);
            if (db.update("tblMenu", values, "id_menu=?", new String[]{id_menu}) != -1)
            {return true;}
        }catch(Exception exception){
            exception.getMessage();
        }
        return false;
    }

}