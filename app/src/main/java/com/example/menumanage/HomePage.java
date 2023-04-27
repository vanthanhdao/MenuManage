package com.example.menumanage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import com.example.menumanage.databinding.ActivityHomePageBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    GridView lstCategoryMenu;
    ArrayList<CategoryMenu> categoryMenuList = new ArrayList<CategoryMenu>();
    AdapterCategoryMenu adapterCategoryMenu;
    ArrayList<Menu> MenuList = new ArrayList<Menu>();
    AdapterMenu adapterMenu;
    SQLiteDatabase db;

    SearchView search;
    //Giữ vị trí trên listview
    int posselected = -1;


    //Khai báo các biến nhận kết quả trả về từ Activity
    public static final int OPEN_CLASS = 113;
    public static final int EDIT_CLASS = 114;
    public static final int SAVE_CLASS = 115;

    ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        //Bottom Nav
        binding.BottomNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home_menu:
                    replaceFragmnent(new HomePageFragment());
                    break;
                case R.id.profile_menu:
                    Intent intent = getIntent();
                    String emailaccount = intent.getStringExtra("emailaccount");
                    String passwordaccount = intent.getStringExtra("passwordaccount");
                    Bundle bundle = new Bundle();
                    bundle.putString("emailaccount",emailaccount);
                    bundle.putString("passwordaccount",passwordaccount);
                    ProfileFragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, fragment)
                            .addToBackStack(null)
                            .commit();
                    break;
            }
            return true;
        });



        //Sileder quảng cáo
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.qc10, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.qc9, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.qc8, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.qc7, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        //Danh sách damh mục món ăn
        lstCategoryMenu = (GridView) findViewById(R.id.lstMenu);
        getCategoryMenuList();
        getMenuList();

        //Đăng ký tạo menucontext
        registerForContextMenu(lstCategoryMenu);


        //Nhấn button thêm danh mục món ăn
        FloatingActionButton AddCategoryMenu = (FloatingActionButton) findViewById(R.id.btnAddCateMenu);
        AddCategoryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertCategoryMenuFragment fragment = new InsertCategoryMenuFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Gắn sự kiện cho girdview lấy vị trí để xóa 1 đối tượng khỏi arraylist (Đè lên màn hình di động)
        lstCategoryMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posselected=position;
                return false;
            }
        });

        //Gắn sự kiện click item trên girdview hiển thị menu
        lstCategoryMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryMenu room = categoryMenuList.get(position) ;
                Bundle bundle = new Bundle();
                bundle.putSerializable("room", (Serializable) room);
                MenuListFragment fragment = new MenuListFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });



    }

    public void confirmDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận xóa danh mục");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_warning_amber_24) ;
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage ("Bạn có chắc xóa danh mục món ăn này?");
        alertDialogBuilder.setCancelable (false) ;

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = openOrCreateDatabase(Login.DATABASE_NAME,MODE_PRIVATE,null);
                String id_categorymenu = categoryMenuList.get(posselected).getId_categorymenu();
                if(db.delete("tblCategoryMenu","id_categorymenu=?",new String[]{id_categorymenu})!=-1){
                    db.delete("tblMenu","id_categorymenu=?",new String[]{id_categorymenu});
                    categoryMenuList.remove(posselected);
                    adapterCategoryMenu.notifyDataSetChanged();
                    Toast.makeText(getApplication(), "Xóa danh mục thành công thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();

    }

    public void replaceFragmnent(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    //Hiển thị danh sách danh mục món ăn
    private void getCategoryMenuList() {

            try {
                db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                Cursor c = db.query("tblCategoryMenu", null, null, null, null, null, null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
               categoryMenuList.add(new CategoryMenu(c.getInt(0) + "",c.getString(1),c.getString(2) ));
                    c.moveToNext();
                }
                //Adapter liên kết giữa một tập hợp dữ liệu và một Adapter View
                adapterCategoryMenu = new AdapterCategoryMenu(this, android.R.layout.simple_list_item_1, categoryMenuList);
                lstCategoryMenu.setAdapter(adapterCategoryMenu);
                adapterCategoryMenu.notifyDataSetChanged();
            }catch(Exception exception){
                Toast.makeText(getApplication(), "Lỗi "+ exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    //Hien thi danh sach mon an trong danh muc
    private void getMenuList() {
        try {
            db = openOrCreateDatabase(Login.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor c = db.query("tblMenu", null, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                MenuList.add(new Menu(c.getString(0).toString(), c.getString(1).toString(),c.getString(2).toString(),c.getString(3).toString()
                        ,c.getString(4).toString(),c.getString(5).toString(),c.getString(6).toString(),c.getString(7).toString()));
                c.moveToNext();
            }
            //Adapter liên kết giữa một tập hợp dữ liệu và một Adapter View
            adapterMenu = new AdapterMenu( this,android.R.layout.simple_list_item_1, MenuList);
            adapterMenu.notifyDataSetChanged();
        }catch(Exception exception){
            Toast.makeText(this, "Lỗi "+ exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HomePage.OPEN_CLASS:
                if (resultCode == HomePage.SAVE_CLASS) {
                    Bundle bundle = data.getBundleExtra("data");
                    CategoryMenu room = (CategoryMenu) bundle.getSerializable("room");
                    categoryMenuList.add(room);
                    adapterCategoryMenu.notifyDataSetChanged();
                }
                break;


            case HomePage.EDIT_CLASS:
                if (resultCode==HomePage.SAVE_CLASS) {
                    Bundle bundle = data.getBundleExtra("data");
                    CategoryMenu room = (CategoryMenu) bundle.getSerializable("room");
                    categoryMenuList.set(posselected, room);
                    adapterCategoryMenu.notifyDataSetChanged();
                }
                break;


        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.categorymenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuedit:
                CategoryMenu room = categoryMenuList.get(posselected) ;
                Bundle bundle = new Bundle();
                bundle.putSerializable("room", (Serializable) room);
                EditCategoryMenuFragment fragment = new EditCategoryMenuFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.menudelete:
                confirmDelete();
                return true;

            case R.id.setting_menu:

            default:
                return super.onContextItemSelected(item);

        }

    }
}