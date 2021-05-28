package com.example.notifition;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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
    ArrayList<String> tg, tieude, noidung, thu, listDiadiem;
    EditText thoigian, td, Phong;
    DataBase dataBase;
    Spinner spinner, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        dataBase = new DataBase(this, "database.sqlite", null, 1);
        listDiadiem = new ArrayList<String>();
        thu = new ArrayList<String>();
        thu.add("Chọn thứ:");
        thu.add(getString(R.string.chuNhat));
        thu.add(getString(R.string.thu2));
        thu.add(getString(R.string.thu3));
        thu.add(getString(R.string.thu4));
        thu.add(getString(R.string.thu5));
        thu.add(getString(R.string.thu6));
        thu.add(getString(R.string.thu7));

        anhxa();
        ArrayAdapter spinAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, thu);
        spinner.setAdapter(spinAdapter);

        listDiadiem.add(getString(R.string.diadiem));
        Cursor cursor = dataBase.GetData("SELECT * FROM diadiem");
        if(cursor !=null){
            while (cursor.moveToNext()){
                listDiadiem.add(cursor.getString(1));
            }

        }
        else {
            Toast toast = Toast.makeText(this, "Địa điểm trống", Toast.LENGTH_SHORT);
            toast.show();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, listDiadiem);
        spinner2.setAdapter(adapter);

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
                int spin1 = spinner.getSelectedItemPosition();
                int spin2 = spinner2.getSelectedItemPosition();
                String gio = thoigian.getText().toString().trim();
                if (spin1 != 0&& hocphan!="" && phong != "" && gio != ""){
                    dataBase.QueryData("INSERT INTO lichhoc (thu, hocphan, phong, thoigian, diadiem)" +
                            "VALUES("+ spin1+", '"+ hocphan+"','" + phong+ "', '"+ gio+"', " +spin2+
                            ")");
                    thoigian.setText("");
                    Phong.setText("");
                    td.setText("");
                    spinner2.setSelection(0);
                    spinner.setSelection(0);
                    Toast toast = Toast.makeText(MainActivity.this, getString(R.string.themthanhcong), Toast.LENGTH_LONG);
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
    private void setSpinner(){

    }
    private void anhxa() {
        spinner = (Spinner) findViewById(R.id.spinner);
        btnThem = (Button) findViewById(R.id.btn3);
        thoigian = (EditText) findViewById(R.id.thoigian);
        td = (EditText) findViewById(R.id.hocphan);
        Phong = (EditText) findViewById(R.id.phong);
        btnDong = (Button) findViewById(R.id.btnclose);
        spinner2 = (Spinner) findViewById(R.id.diadiemSpinner);
    }


}