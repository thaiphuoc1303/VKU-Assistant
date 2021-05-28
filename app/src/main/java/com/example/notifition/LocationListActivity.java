package com.example.notifition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class LocationListActivity extends AppCompatActivity {
    ListView list;
    ArrayList<LocationItem> locationItems;
    DataBase dataBase;
    LocationAdapter locationAdapter;
    Handler handler;

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.list);
        locationItems = new ArrayList<>();
        dataBase = new DataBase(this, "database.sqlite", null, 1);
        String sql = "SELECT * FROM diadiem";
        Cursor cursor = dataBase.GetData(sql);
        while (cursor.moveToNext()){
            String ten = cursor.getString(1);
            String mota = cursor.getString(2);
            String lat = cursor.getString(3);
            String lng = cursor.getString(4);
            String id = cursor.getString(0);
            locationItems.add(new LocationItem(ten, mota, lat, lng, id));
        }
        locationAdapter = new LocationAdapter(this, R.layout.location_row, locationItems);
        list.setAdapter(locationAdapter);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String sql = "SELECT * FROM diadiem";
                Cursor cursor = dataBase.GetData(sql);
                locationItems.clear();
                while (cursor.moveToNext()){
                    String ten = cursor.getString(1);
                    String mota = cursor.getString(2);
                    String lat = cursor.getString(3);
                    String lng = cursor.getString(4);
                    String id = cursor.getString(0);
                    locationItems.add(new LocationItem(ten, mota, lat, lng, id));
                }
                locationAdapter.notifyDataSetChanged();
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }
}