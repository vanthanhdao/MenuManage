package com.example.menumanage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;


public class MenuListFragment extends Fragment  {

    GridView lstMenu1;
    ArrayList<Menu> MenuList = new ArrayList<Menu>();
    AdapterMenu adapterMenu;
    SQLiteDatabase db;
    //Giữ vị trí trên listview
    int posselected = -1;
    SearchView search;
    public static final int OPEN_CLASS = 113;
    public static final int EDIT_CLASS = 114;
    public static final int SAVE_CLASS = 115;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_list, container, false);
        lstMenu1 = (GridView) v.findViewById(R.id.lstMenu1);


        getMenuList();

        registerForContextMenu(lstMenu1);

        //Gắn sự kiện cho girdview lấy vị trí để xóa 1 đối tượng khỏi arraylist (Đè lên màn hình di động)
        lstMenu1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posselected=position;
                return false;
            }
        });




        //Nhấn button thêm danh mục món ăn
        FloatingActionButton AddCategoryMenu = (FloatingActionButton) v.findViewById(R.id.floatingActionButtoneditM);
        AddCategoryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmnent();
            }
        });

        lstMenu1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Tạo bundle và đặt dữ liệu vào bundle
                Menu menu = MenuList.get(position) ;
                Bundle bundle = new Bundle();
                bundle.putSerializable("menu", (Serializable) menu);

                // Đặt bundle vào Fragment B
                ShowMenuFragment fragment = new ShowMenuFragment();
                fragment.setArguments(bundle);


                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();

            }
        });











        return v;
    }


//Hien thi danh sach mon an trong danh muc
    private void getMenuList() {
        try {

            Bundle bundle = getArguments();
            CategoryMenu categoryMenu = (CategoryMenu) bundle.getSerializable("room");
            String id = categoryMenu.getId_categorymenu();
            db = getActivity().openOrCreateDatabase(Login.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT id_menu,id_categorymenu,name_menu,image_menu,decreption_menu,ingredient_menu,make_menu,video_menu from tblMenu\n" +
                    "WHERE id_categorymenu=?",new String[]{id});
            c.moveToFirst();
            while (!c.isAfterLast()) {
                MenuList.add(new Menu(c.getString(0).toString(), c.getString(1).toString(),c.getString(2).toString(),c.getString(3).toString()
                ,c.getString(4).toString(),c.getString(5).toString(),c.getString(6).toString(),c.getString(7).toString()));
                c.moveToNext();
            }
            //Adapter liên kết giữa một tập hợp dữ liệu và một Adapter View
            adapterMenu = new AdapterMenu(getActivity(), android.R.layout.simple_list_item_1, MenuList);
            lstMenu1.setAdapter(adapterMenu);
            adapterMenu.notifyDataSetChanged();
        }catch(Exception exception){
            Toast.makeText(getActivity(), "Lỗi "+ exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    public void confirmDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận xóa món ăn");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_warning_amber_24) ;
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage ("Bạn có chắc xóa món ăn này này?");
        alertDialogBuilder.setCancelable (false) ;

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = getActivity().openOrCreateDatabase(Login.DATABASE_NAME,Context.MODE_PRIVATE,null);
                String id_menu = MenuList.get(posselected).getId_menu();
                if(db.delete("tblMenu","id_menu=?",new String[]{id_menu})!=-1){
                    MenuList.remove(posselected);
                    adapterMenu.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Xóa món ăn thành công thành công", Toast.LENGTH_SHORT).show();
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

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menueditM:
                Menu room = MenuList.get(posselected) ;
                Bundle bundle = new Bundle();
                bundle.putSerializable("room", (Serializable) room);
                EditMenuFragment fragment = new EditMenuFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();
                return true;
            case R.id.menudeleteM: confirmDelete();
                return true;

            default:
                return super.onContextItemSelected(item);

        }

    }

    public  void replaceFragmnent() {
        Bundle bundle = getArguments();
        CategoryMenu categoryMenu = (CategoryMenu) bundle.getSerializable("room");
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("room", (Serializable) categoryMenu);
        AddMenuFragment fragment1 = new AddMenuFragment();
        fragment1.setArguments(bundle1);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment1)
                .commit();

    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MenuListFragment.OPEN_CLASS:
                if (resultCode == MenuListFragment.SAVE_CLASS) {
                    Bundle bundle = data.getBundleExtra("data");
                    Menu room = (Menu) bundle.getSerializable("room");
                    MenuList.add(room);
                    adapterMenu.notifyDataSetChanged();
                }
                break;


            case MenuListFragment.EDIT_CLASS:
                if (resultCode==MenuListFragment.SAVE_CLASS) {
                    Bundle bundle = data.getBundleExtra("data");
                    Menu room = (Menu) bundle.getSerializable("room");
                    MenuList.set(posselected, room);
                    adapterMenu.notifyDataSetChanged();
                }
                break;


        }
    }
}