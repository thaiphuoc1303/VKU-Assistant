package com.example.notifition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ThemNhacNhoActivity extends AppCompatActivity {

    EditText edtTieude, edtChiTiet;
    TextView tvThoiGian;
    Spinner spinnerLapLai;
    Button btnThem;
    SimpleDateFormat simpleDateFormat;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhac_nho);

        edtTieude = findViewById(R.id.edtTieuDe);
        edtChiTiet = findViewById(R.id.edtChiTiet);
        tvThoiGian = findViewById(R.id.tvThoigian);
        spinnerLapLai = findViewById(R.id.laplai);
        btnThem = findViewById(R.id.btnThem);
        dataBase = new DataBase(this, "database.sqlite", null, 1);

        simpleDateFormat = new SimpleDateFormat("HH: mm dd-MM-yyyy");
        ArrayList<String> arrLapLai = new ArrayList<>();
        arrLapLai.add("Không lặp lại");
        arrLapLai.add("Hàng tuần");
        arrLapLai.add("Hàng tháng");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, arrLapLai);
        spinnerLapLai.setAdapter(adapter);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        tvThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ThemNhacNhoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(ThemNhacNhoActivity.this, new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                                tvThoiGian.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },hour, minute, true);
                        timePickerDialog.show();
                    }
                },year, month, dayofMonth);
                datePickerDialog.show();

            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tieude = edtTieude.getText()+"";
                String chitiet = edtChiTiet.getText()+"";
                String thoigian = tvThoiGian.getText()+"";
                int nhaclai = spinnerLapLai.getSelectedItemPosition();
                dataBase.QueryData("INSERT INTO nhacnho (ten, chitiet, thoigian, laplai) VALUES ('" +
                        tieude + "', '" +
                        chitiet + "', '" +
                        thoigian + "', " +
                        nhaclai + ")");
                edtChiTiet.setText("");
                edtTieude.setText("");
                tvThoiGian.setText("Chọn thời gian");
                spinnerLapLai.setSelection(0);
                Toast toast = Toast.makeText(ThemNhacNhoActivity.this, getString(R.string.themthanhcong), Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }
}