package com.example.notifition;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MonHocAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    ArrayList<monhoc> hocphans;
    private ImageButton imgbtnEdit, imgbtnDelete;
    DataBase dataBase;

    public MonHocAdapter(Context context, int layout, ArrayList<monhoc> hocphan) {
        dataBase = new DataBase(context, "database.sqlite", null, 1);
        this.context = context;
        this.layout = layout;
        this.hocphans = hocphan;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return hocphans.size();
    }

    @Override
    public Object getItem(int position) {
        return hocphans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        TextView tvTen, tvPhong, tvThu, tvGio, idMon;
        idMon = (TextView) convertView.findViewById(R.id.id);
        tvTen = (TextView) convertView.findViewById(R.id.monhoc);
        tvPhong = (TextView) convertView.findViewById(R.id.phong);
        tvThu = (TextView) convertView.findViewById(R.id.thu);
        tvGio = (TextView) convertView.findViewById(R.id.giohoc);
        imgbtnDelete = (ImageButton) convertView.findViewById(R.id.delete);
        imgbtnEdit = (ImageButton) convertView.findViewById(R.id.edit);
        monhoc mon = hocphans.get(position);


        idMon.setText(mon.getStt());
        tvTen.setText(mon.getTen());
        tvGio.setText(mon.getGiohoc());
        tvPhong.setText(mon.getPhong());
        switch (mon.getThu()){
            case "1":
                tvThu.setText(R.string.chuNhat);
                break;
            case "2":
                tvThu.setText(R.string.thu2);
                break;
            case "3":
                tvThu.setText(R.string.thu3);
                break;
            case "4":
                tvThu.setText(R.string.thu4);
                break;
            case "5":
                tvThu.setText(R.string.thu5);
                break;
            case "6":
                tvThu.setText(R.string.thu6);
                break;
            case "7":
                tvThu.setText(R.string.thu7);
                break;
        }
        imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBase.QueryData("DELETE FROM lichhoc WHERE id = " + mon.getId());
                Toast toast = Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        imgbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit(mon);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietActivity.class);
                intent.putExtra("data", mon);
                intent.putExtra("mode", 2);
                ContextCompat.startActivity(context, intent, null);
            }
        });
        return convertView;
    }
    private void dialogEdit(monhoc mon){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.editlayout);
        dialog.show();
        Button xacnhan, huy;
        ArrayList<String> thu = new ArrayList<String>();
        thu.add(context.getString(R.string.chonthu));
        thu.add(context.getString(R.string.chuNhat));
        thu.add(context.getString(R.string.thu2));
        thu.add(context.getString(R.string.thu3));
        thu.add(context.getString(R.string.thu4));
        thu.add(context.getString(R.string.thu5));
        thu.add(context.getString(R.string.thu6));
        thu.add(context.getString(R.string.thu7));
        ArrayList<String> arrDD = new ArrayList<String>();

        Cursor cursor = dataBase.GetData("SELECT * FROM diadiem");
        arrDD.add(context.getString(R.string.chonthu));
        while (cursor.moveToNext()){
            arrDD.add(cursor.getString(1));
        }

        EditText edtTenMon, edtPhong, edtGio;
        Spinner spinner, spinner2;
        spinner = (Spinner) dialog.findViewById(R.id.spinner2) ;
        edtTenMon = (EditText)dialog.findViewById(R.id.editTextTen);
        edtPhong =(EditText)dialog.findViewById(R.id.editTextPhong);
        edtGio =(EditText) dialog.findViewById(R.id.editTextGiohoc);
        spinner2 = (Spinner) dialog.findViewById(R.id.SPdiadiem);
        edtGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH mm");

                TimePickerDialog time = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

                    int year= calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                        edtGio.setText(simpleDateFormat.format(calendar.getTime())+"");
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                time.show();
            }
        });
        ArrayAdapter adapter2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, arrDD);
        ArrayAdapter adapter1 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, thu);
        xacnhan = (Button) dialog.findViewById(R.id.buttonXacNhan);
        huy = (Button) dialog.findViewById(R.id.buttonhuy);
        edtTenMon.setText(mon.getTen());
        edtPhong.setText(mon.getPhong());
        edtGio.setText(mon.getGiohoc());
        spinner.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(Integer.parseInt(mon.getDiadiem()));
        spinner.setSelection(Integer.parseInt(mon.getThu()));

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenmon = edtTenMon.getText().toString().trim();
                String phong = edtPhong.getText().toString().trim();
                String gio = edtGio.getText().toString().trim();
                int ngay = spinner.getSelectedItemPosition();
                int diadiem = spinner2.getSelectedItemPosition();
                if(!tenmon.equals("") && !phong.equals("") && !gio.equals("") && ngay!=0){
                    String sql = "UPDATE lichhoc SET " +
                            "thu = "+ ngay + ", "+
                            "hocphan = '" + tenmon +"', "+
                            "phong = '" +phong +"', "+
                            "thoigian = '" +gio + "', "+
                            "diadiem = '"+ diadiem+ "' "+
                            " WHERE ID ="+mon.getId();
                    dataBase.QueryData(sql);
                    dialog.cancel();
                    Toast toast = Toast.makeText(context, "Đã sửa", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(context, "Điền đầy đủ các ô", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
