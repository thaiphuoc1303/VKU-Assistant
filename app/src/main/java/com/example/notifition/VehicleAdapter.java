package com.example.notifition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Integer> icon;

    public VehicleAdapter (Context context, int layout, ArrayList<Integer> icon){
        this.context = context;
        this.layout = layout;
        this.icon = icon;
    }

    @Override
    public int getCount() {
        return icon.size();
    }

    @Override
    public Object getItem(int position) {
        return icon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        ImageView img = (ImageView) convertView.findViewById(R.id.icon);
        img.setImageResource(icon.get(position));
        return convertView;
    }
}
