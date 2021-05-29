package com.example.notifition.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notifition.DataBase;
import com.example.notifition.R;
import com.example.notifition.model.LocationItem;

import java.util.ArrayList;

public class LocationAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<LocationItem> locationItems;

    public LocationAdapter(Context context, int layout, ArrayList<LocationItem> locationItems){
        this.context = context;
        this.layout = layout;
        this.locationItems = locationItems;
    }

    @Override
    public int getCount() {
        return locationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return locationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        TextView tvTen, tvMota;
        ImageButton btnEdit, btnDelete;
        DataBase dataBase = new DataBase(context, "database.sqlite", null, 1);
        tvTen = convertView.findViewById(R.id.tvTen);
        tvMota = convertView.findViewById(R.id.tvMota);
        btnEdit = convertView.findViewById(R.id.btnEdit);
        btnDelete = convertView.findViewById(R.id.btnDelete);
        LocationItem locationItem = locationItems.get(position);
        tvTen.setText(locationItem.getTen());
        tvMota.setText(locationItem.getMota());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "DELETE FROM diadiem WHERE id = "+ locationItem.getId();
                dataBase.QueryData(sql);
                Toast toast = Toast.makeText(context, "Đã xóa địa điểm!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_location_layout);
                dialog.show();
                EditText edtTen, edtMota, edtLat, edtLng;
                Button btnChon, btnHuy, btnXacNhan;
                edtTen = dialog.findViewById(R.id.edtTen);
                edtMota = dialog.findViewById(R.id.edtMoTa);
                edtLat = dialog.findViewById(R.id.edtLat);
                edtLng =dialog.findViewById(R.id.edtLng);
                btnChon = dialog.findViewById(R.id.btnChon);
                btnHuy = dialog.findViewById(R.id.btnHuy);
                btnXacNhan = dialog.findViewById(R.id.btnXacNhan);

                edtTen.setText(locationItem.getTen());
                edtMota.setText(locationItem.getMota());
                edtLat.setText(locationItem.getLat());
                edtLng.setText(locationItem.getLng());

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ten = edtTen.getText()+"";
                        String mota = edtMota.getText()+"";
                        String lat = edtLat.getText()+"";
                        String lng = edtLng.getText()+"";
                        if (ten!="" && lat!= "" && lng != ""){
                            String sql = "UPDATE diadiem set ten= '" + ten +
                                    "', chitiet = '" + mota +
                                    "', lat = '" + lat +
                                    "', lng = '" + lng +
                                    "' WHERE id =" + locationItem.getId();
                            dataBase.QueryData(sql);
                            Toast toast = Toast.makeText(context, "Đã sửa!", Toast.LENGTH_SHORT);
                            toast.show();

                            dialog.cancel();
                        }
                        else {
                            Toast toast = Toast.makeText(context, "Điền đầy đủ các ô", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        });
        return convertView;
    }
}
