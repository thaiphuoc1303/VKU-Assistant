package com.example.notifition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.notifition.Adapter.MonHocAdapter;
import com.example.notifition.model.monhoc;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    DataBase dataBase;
    ArrayList<monhoc> monhocs;
    ListView list;
    MonHocAdapter adapter;
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.listview);

        dataBase = new DataBase(this, "database.sqlite", null, 1);

        //thêm dữ liệu
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Dãy A Khu V', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.972284', '108.249423')");
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Dãy B Khu V', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.973209', '108.249612')");
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Khu K', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.975047', '108.252507')");
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Kí túc xá', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.973317', '108.252440')");
        Cursor cursor = dataBase.GetData("SELECT * FROM lichhoc");
        monhocs = new ArrayList<>();
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
            String id = cursor.getString(0);
            String thu = cursor.getString(1);
            String mon = cursor.getString(2);
            String phong = cursor.getString(3);
            String thoiGian = cursor.getString(4);
            String diadiem = cursor.getString(6);
            monhocs.add(new monhoc(id, mon, phong, thu, thoiGian, diadiem, i + ""));

        }
        adapter = new MonHocAdapter(this, R.layout.monhoc, monhocs);
        list.setAdapter(adapter);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor cursor1 = dataBase.GetData("SELECT * FROM lichhoc");
                monhocs = new ArrayList<>();
                int i = 0;
                while (cursor1.moveToNext()) {
                    i++;
                    String id = cursor1.getString(0);
                    String thu = cursor1.getString(1);
                    String mon = cursor1.getString(2);
                    String phong = cursor1.getString(3);
                    String thoiGian = cursor1.getString(4);
                    String diadiem = cursor1.getString(6);
                    monhocs.add(new monhoc(id, mon, phong, thu, thoiGian, diadiem, i + ""));

                }
                adapter = new MonHocAdapter(MainActivity2.this, R.layout.monhoc, monhocs);
                list.setAdapter(adapter);
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