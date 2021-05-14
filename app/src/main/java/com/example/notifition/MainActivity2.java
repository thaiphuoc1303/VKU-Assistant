package com.example.notifition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    DataBase dataBase;
    ArrayList<monhoc> monhocs;
    ListView list;
    MonHocAdapter adapter;
    Menu menu;
    ImageButton btnReload;
    double lng, lat;
    public LocationRequest locationRequest;
    LocationManager locationManager;
    public LocationListener locationListener;

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.danhsach:
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

        list = (ListView) findViewById(R.id.listview);
        btnReload = (ImageButton) findViewById(R.id.imgbtnReload);
        dataBase = new DataBase(this, "database.sqlite", null, 1);
        dataBase.QueryData("CREATE TABLE IF NOT EXISTS lichhoc (" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    thu       INTEGER," +
                "    hocphan   VARCHAR," +
                "    phong     VARCHAR," +
                "    thoigian VARCHAR," +
                "    trangthai INTEGER DEFAULT (0)" +
                ")");
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

            monhocs.add(new monhoc(id, mon, phong, thu, thoiGian, i + ""));

        }
        adapter = new MonHocAdapter(this, R.layout.monhoc, monhocs);
        list.setAdapter(adapter);
        Intent intent = new Intent(this, ServiceNotification.class);
        startService(intent);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor2 = dataBase.GetData("SELECT * FROM lichhoc");
                monhocs.clear();
                int j = 0;
                while (cursor2.moveToNext()) {
                    j++;
                    String id = cursor2.getString(0);
                    String thu = cursor2.getString(1);
                    String mon = cursor2.getString(2);
                    String phong = cursor2.getString(3);
                    String thoiGian = cursor2.getString(4);

                    monhocs.add(new monhoc(id, mon, phong, thu, thoiGian, j + ""));

                }
                adapter.notifyDataSetChanged();

            }
        });

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        createLocationRequest();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = (double) location.getLatitude();
                lng = (double) location.getLongitude();
//                Toast toast = Toast.makeText(MainActivity2.this, lat+" "+ lng, Toast.LENGTH_SHORT);
//                toast.show();
            }
        };
        int fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coars = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fine != PackageManager.PERMISSION_GRANTED || coars != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//            fusedLocationClient.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }


}