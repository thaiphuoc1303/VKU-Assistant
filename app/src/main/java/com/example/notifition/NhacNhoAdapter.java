package com.example.notifition;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NhacNhoAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<NhacNhoItem> nhacNhoItems;
    SimpleDateFormat simpleDateFormat;
    DataBase dataBase;

    public  NhacNhoAdapter(Context context, int layout, ArrayList<NhacNhoItem> nhacNhoItems){
        this.context = context;
        this.layout = layout;
        this.nhacNhoItems = nhacNhoItems;
    }
    @Override
    public int getCount() {
        return nhacNhoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return nhacNhoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        simpleDateFormat  = new SimpleDateFormat("HH: mm dd-MM-yyyy");
        TextView tvTen = convertView.findViewById(R.id.tvTen);
        TextView tvChiTiet = convertView.findViewById(R.id.tvChitiet);
        ImageButton btnEdit = convertView.findViewById(R.id.imgEdit);
        ImageButton btnDelete = convertView.findViewById(R.id.imgDelete);
        dataBase = new DataBase(context, "database.sqlite", null, 1);

        NhacNhoItem nhacNhoItem = nhacNhoItems.get(position);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(nhacNhoItem.getThoigian()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvTen.setText(nhacNhoItem.getTen());
        String strChiTiet = "";
        SimpleDateFormat format1 = new SimpleDateFormat("HH: mm ");
        switch (nhacNhoItem.getLaplai()){
            case 0:
                strChiTiet = nhacNhoItem.thoigian;
                break;
            case 1:
                int tuan = calendar.get(Calendar.DAY_OF_WEEK);
                
                switch (tuan){
                    case 1:
                        strChiTiet = format1.format(calendar.getTime())+ "Chủ nhật hàng tuần";
                        break;
                    case 2:
                        strChiTiet = format1.format(calendar.getTime())+ "Thứ hai hàng tuần";
                        break;
                    case 3:
                        strChiTiet = format1.format(calendar.getTime())+ "Thứ 3 hàng tuần";
                        break;
                    case 4:
                        strChiTiet = format1.format(calendar.getTime())+ "Thứ tư hàng tuần";
                        break;
                    case 5:
                        strChiTiet = format1.format(calendar.getTime())+ "Thứ 5 hàng tuần";
                        break;
                    case 6:
                        strChiTiet = format1.format(calendar.getTime())+ "Thứ 6 hàng tuần";
                        break;
                    case 7:
                        strChiTiet = format1.format(calendar.getTime())+ "Thứ 7 hàng tuần";
                }
                break;
            case 2:
                int thang = calendar.get(Calendar.DAY_OF_MONTH);
                strChiTiet = format1.format(calendar.getTime()) + "ngày "+ thang+" hàng tháng";
                break;
            default:
                strChiTiet = nhacNhoItem.thoigian;
                break;
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.layout_edit_nhac_nho);
                dialog.show();
                Button btnHuy, btnLuu;
                EditText edtTieuDe, edtNoiDung;
                Spinner spinner;
                TextView tvThoiGian;
                btnHuy = dialog.findViewById(R.id.btnHuy);
                btnLuu = dialog.findViewById(R.id.btnLuu);
                edtTieuDe = dialog.findViewById(R.id.edtTieuDe);
                edtNoiDung = dialog.findViewById(R.id.edtChiTiet);
                spinner = dialog.findViewById(R.id.laplai);
                tvThoiGian = dialog.findViewById(R.id.tvThoigian);
                ArrayList<String> arrLapLai = new ArrayList<>();
                arrLapLai.add("Không lặp lại");
                arrLapLai.add("Hàng tuần");
                arrLapLai.add("Hàng tháng");
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_expandable_list_item_1, arrLapLai);
                spinner.setAdapter(adapter);

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                tvThoiGian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener(){
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

                edtTieuDe.setText(nhacNhoItem.getTen());
                edtNoiDung.setText(nhacNhoItem.getChitiet());
                tvThoiGian.setText(nhacNhoItem.getThoigian());
                spinner.setSelection(nhacNhoItem.getLaplai());
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btnLuu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(context, "Đã lưu!", Toast.LENGTH_SHORT);
                        toast.show();
                        String edTieuDe = edtTieuDe.getText()+"";
                        String edChiTiet = edtNoiDung.getText()+"";
                        String edThoiGian = tvThoiGian.getText()+"";
                        int lap = spinner.getSelectedItemPosition();
                        String sql = "UPDATE nhacnho SET " +
                                "ten = '" + edTieuDe+
                                "', chitiet = '"+ edChiTiet+
                                "', thoigian = '" + edThoiGian+
                                "', laplai = "+ lap +
                                " WHERE id = "+ nhacNhoItem.getId();
                        dataBase.QueryData(sql);

                        dialog.cancel();

                    }
                });

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBase.QueryData("DELETE FROM nhacnho WHERE id = "+ nhacNhoItem.getId());
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Thông báo!").setMessage("Đã xóa").show();


            }
        });
        tvChiTiet.setText(strChiTiet);
        return convertView;
    }
}
