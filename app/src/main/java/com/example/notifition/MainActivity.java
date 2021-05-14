package com.example.notifition;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.security.auth.Destroyable;

public class MainActivity<request> extends AppCompatActivity {
    Button btnThem, btnDong;
    private String CHANNEL_ID;
    ArrayList<String> tg, tieude, noidung, thu;
    EditText thoigian, td, Phong;
    DataBase dataBase;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        dataBase = new DataBase(this, "database.sqlite", null, 1);

        thu = new ArrayList<String>();
        thu.add("Chọn thứ:");
        thu.add("Chủ nhật");
        thu.add("Thứ hai");
        thu.add("Thứ ba");
        thu.add("Thứ tư");
        thu.add("Thứ năm");
        thu.add("Thứ sáu");
        thu.add("Thứ bảy");

        anhxa();
        ArrayAdapter spinAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, thu);

        spinner.setAdapter(spinAdapter);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("HH mm");
        tg = new ArrayList<>();
        tieude = new ArrayList<>();
        noidung = new ArrayList<>();
        thoigian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog time = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    int year= calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                        thoigian.setText(simpleDateFormatMain.format(calendar.getTime())+"");
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                time.show();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hocphan = td.getText().toString().trim();
                String phong = Phong.getText().toString().trim();
                int spin = spinner.getSelectedItemPosition();
                String gio = thoigian.getText().toString().trim();
                if (spin != 0&& hocphan!="" && phong != "" && gio != ""){
                    dataBase.QueryData("INSERT INTO lichhoc (thu, hocphan, phong, thoigian)" +
                            "VALUES("+ spin+", '"+ hocphan+"','" + phong+ "', '"+ gio+"')");
                    thoigian.setText("");
                    Phong.setText("");
                    td.setText("");

                    Toast toast = Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Điền đầy đủ các ô", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void anhxa() {
        spinner = (Spinner) findViewById(R.id.spinner);
        btnThem = (Button) findViewById(R.id.btn3);
        thoigian = (EditText) findViewById(R.id.thoigian);
        td = (EditText) findViewById(R.id.hocphan);
        Phong = (EditText) findViewById(R.id.phong);
        btnDong = (Button) findViewById(R.id.btnclose);
    }


}