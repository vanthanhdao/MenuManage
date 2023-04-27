package com.example.menumanage;

import static android.app.Activity.RESULT_OK;

import static com.example.menumanage.MenuListFragment.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
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
import java.util.ArrayList;


public class AddMenuFragment extends Fragment {
    SQLiteDatabase db;
    EditText nameM,decreptionM,ingradientM,makeM;
    Button uploadImage,uploadVideo,saveM,closeM;
    ImageView imageM;
    VideoView videoM;
    Uri uri;
    String id_categorymenu;
    public static final int REQUEST_CODE = 100;
    public static final int REQUEST_CODE1 = 101;
    public static final int SAVE_CLASS = 115;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_add_menu, container, false);
        uploadImage = (Button) v.findViewById(R.id.btnUploadImage);
        uploadVideo = (Button) v.findViewById(R.id.btnUploadVideo);
        saveM = (Button) v.findViewById(R.id.btnSaveM);
        closeM = (Button) v.findViewById(R.id.btnCloseM);
        imageM = (ImageView) v.findViewById(R.id.imageView);
        nameM = (EditText) v.findViewById(R.id.edtNameM);
        decreptionM = (EditText) v.findViewById(R.id.edtNameM);
        ingradientM = (EditText) v.findViewById(R.id.edtNameM);
        makeM = (EditText) v.findViewById(R.id.edtNameM);
        videoM = (VideoView) v.findViewById(R.id.uploadvideo);




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



        saveM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = saveMenu();
                Bundle bundle = new Bundle();
                Intent intent = getActivity().getIntent();
                if (id != -1) { // = -1 Thém khong thanh cong
                    if(nameM.getText()!=null && decreptionM.getText()!=null && ingradientM.getText()!=null && makeM.getText()!=null ){
                        Menu r = new Menu(id + "",id_categorymenu, nameM.getText().toString(), imageM.toString(),decreptionM.getText().toString()
                        ,ingradientM.getText().toString(),makeM.getText().toString(),videoM.toString());
                        bundle.putSerializable("room", r);
                        intent.putExtra("data", bundle);
                        getActivity().setResult(AddMenuFragment.SAVE_CLASS, intent);
                        Toast.makeText(getActivity(), "Thêm danh mục món ăn thành công", Toast.LENGTH_LONG).show();
                        db.close();
                        Intent view = new Intent(getActivity(),HomePage.class);
                        startActivity(view);
                    }
                } else{Toast.makeText(getActivity(), "Bạn chưa nhập đủ thông tin!!!", Toast.LENGTH_SHORT).show();}
            }
        });


        closeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),HomePage.class);
                startActivity(intent1);
            }
        });

        return v;
    }


    public   long saveMenu(){
        try {
        //Xử lý thêm ảnh
            // Lấy ảnh từ ImageView
            BitmapDrawable drawable = (BitmapDrawable) imageM.getDrawable();
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
            Bundle bundle = getArguments();
            CategoryMenu categoryMenu = (CategoryMenu) bundle.getSerializable("room");
            id_categorymenu = categoryMenu.getId_categorymenu();
            ContentValues values = new ContentValues();
            values.put("image_menu",imageUri.toString());
            values.put("name_menu", nameM.getText().toString());
            values.put("decreption_menu",decreptionM.getText().toString());
            values.put("ingredient_menu",ingradientM.getText().toString());
            values.put("make_menu",makeM.getText().toString());
            values.put("video_menu",videoUri.toString());
            values.put("id_categorymenu",id_categorymenu);
            long id =db.insert("tblMenu", null, values);
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageM.setImageURI(uri);
        }else if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            videoM.setVideoURI(uri);
            videoM.start();
            MediaController mediaController = new MediaController(getActivity());
            videoM.setMediaController(mediaController);
            mediaController.setAnchorView(videoM);

        }
    }



}