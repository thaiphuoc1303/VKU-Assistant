package com.example.notifition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ChiTietActivity extends AppCompatActivity {
    monhoc mon;
    TextView ten, thu, gio, phong, diadiem;
    DataBase dataBase = new DataBase(this, "database.sqlite", null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhxa();
        Intent intent = getIntent();
        if (intent.getIntExtra("mode", 0) != 0){
            if (intent.getIntExtra("mode", 0) == 2){
                mon = (monhoc) intent.getSerializableExtra("data");
                ten.setText(mon.getTen());
                thu.setText(mon.getThu());
                gio.setText(mon.getGiohoc());
                phong.setText(mon.getPhong());
                String id = mon.getDiadiem().trim();
                String sql = "SELECT ten FROM diadiem WHERE id = " + id;
                Cursor cursor = dataBase.GetData(sql);
                String dd = id;
                while (cursor.moveToNext()){
                    dd = cursor.getString(0);
                }
                diadiem.setText(dd);
            }
            else {Log.e("AAA", "A");
                String id = intent.getStringExtra("id");

                String sql = "SELECT * FROM lichhoc WHERE id = "+ id;
                Cursor cursor = dataBase.GetData(sql);
                while (cursor.moveToNext()){
                    ten.setText(cursor.getString(2));
                    thu.setText(cursor.getString(1));
                    gio.setText(cursor.getString(4));
                    phong.setText(cursor.getString(3));
                    String sql1 = "SELECT ten FROM diadiem WHERE id = " + cursor.getString(6);
                    Cursor cursor1 = dataBase.GetData(sql1);
                    String dd= "";
                    while (cursor1.moveToNext()){
                        dd = cursor1.getString(0);
                    }
                    diadiem.setText(dd);
                }
            }
        }

    }
    void anhxa(){
        ten = findViewById(R.id.tenMon);
        thu = findViewById(R.id.thu);
        gio = findViewById(R.id.gio);
        phong = findViewById(R.id.phong);
        diadiem = findViewById(R.id.diadiem);
    }
}