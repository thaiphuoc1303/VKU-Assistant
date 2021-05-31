package com.example.notifition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class ListCVActivity extends AppCompatActivity {
    ListView listView;
    DataBase dataBase;
    ArrayList<NhacNhoItem> nhacNhoItems;
    NhacNhoAdapter adapter;
    Handler handler;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.themMonHoc:
                Intent intent = new Intent(this, ThemNhacNhoActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cv);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.list);

        dataBase = new DataBase(this, "database.sqlite", null, 1);
        Cursor cursor = dataBase.GetData("SELECT * FROM nhacnho");
        nhacNhoItems = new ArrayList<>();
        int stt = 0;
        while (cursor.moveToNext()){
            String ten = cursor.getString(1);
            String chitiet = cursor.getString(2);
            String thoigian = cursor.getString(3);
            String id = cursor.getString(0);
            int laplai = Integer.parseInt(cursor.getString(4));
            int trangthai = Integer.parseInt(cursor.getString(5));
            stt ++;
            NhacNhoItem nhacNhoItem = new NhacNhoItem(ten, chitiet, thoigian, laplai, trangthai, id, stt);
            nhacNhoItems.add(nhacNhoItem);
        }

        adapter = new NhacNhoAdapter(this, R.layout.nhac_nho_row, nhacNhoItems);
        listView.setAdapter(adapter);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor cursor1 = dataBase.GetData("SELECT * FROM nhacnho");
                nhacNhoItems = new ArrayList<>();
                int stt = 1;
                while (cursor1.moveToNext()){
                    String ten = cursor1.getString(1);
                    String chitiet = cursor1.getString(2);
                    String thoigian = cursor1.getString(3);
                    String id = cursor1.getString(0);
                    int laplai = Integer.parseInt(cursor1.getString(4));
                    int trangthai = Integer.parseInt(cursor1.getString(5));
                    NhacNhoItem nhacNhoItem = new NhacNhoItem(ten, chitiet, thoigian, laplai, trangthai, id, stt);
                    nhacNhoItems.add(nhacNhoItem);
                    stt ++;
                }
                adapter = new NhacNhoAdapter(ListCVActivity.this, R.layout.nhac_nho_row, nhacNhoItems);
                listView.setAdapter(adapter);
                handler.postDelayed(this, 2000);
            }
        }, 2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(0);
    }
}