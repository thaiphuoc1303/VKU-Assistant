package com.example.notifition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddLocationActivity extends AppCompatActivity {
    String lat, lng;
    EditText edtTen, edtMota, edtLat, edtLng;
    Button btnThem;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataBase = new DataBase(this, "database.sqlite", null, 1);

        anhxa();
        Intent intent = getIntent();
        if (intent!= null){
            lat = intent.getStringExtra("lat");
            lng = intent.getStringExtra("lng");
        }
        else{
            lat = "";
            lng = "";
        }
        edtLat.setText(lat);
        edtLng.setText(lng);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = String.valueOf(edtTen.getText());
                String mota = String.valueOf(edtMota.getText());
                String vlLat = String.valueOf(edtLat.getText());
                String vlLng = String.valueOf(edtLng.getText());

                if (ten!="" && vlLat!="" && vlLng != ""){
                    String sql = "INSERT INTO diadiem (ten, chitiet, lat, lng) VALUES ('" +
                            ten + "', '" +
                            mota + "', '" +
                            lat + "', '" +
                            lng + "')";

                    dataBase.QueryData(sql);
                    Toast toast = Toast.makeText(AddLocationActivity.this, getString(R.string.themthanhcong), Toast.LENGTH_SHORT);
                    toast.show();
                    edtTen.setText("");
                    edtMota.setText("");
                    edtLat.setText("");
                    edtLng.setText("");
                }
                else {
                    Toast toast = Toast.makeText(AddLocationActivity.this, "Điền đủ thông tin cần thiết", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    void anhxa(){
        edtTen = findViewById(R.id.edtTen);
        edtMota = findViewById(R.id.edtMoTa);
        edtLat = findViewById(R.id.edtLat);
        edtLng = findViewById(R.id.edtLng);
        btnThem = findViewById(R.id.btnThem);
    }
}