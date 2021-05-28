package com.example.notifition;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingActivity extends AppCompatActivity {
    Switch swOnOff;
    Spinner thoigian, baolai, phuongtien;
    Button btnLuu;
    SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.setting);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle(getString(R.string.caidat));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhxa();
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tg = thoigian.getSelectedItemPosition();
                switch (tg){
                    case 0:
                        editor.putInt("thoigian", 5);
                        break;
                    case 1:
                        editor.putInt("thoigian", 10);
                        break;
                    case 2:
                        editor.putInt("thoigian", 15);
                        break;
                    case 3:
                        editor.putInt("thoigian", 30);
                        break;
                }
                int bl = baolai.getSelectedItemPosition();
                switch (bl){
                    case 0:
                        editor.putInt("baolai", 1);
                        break;
                    case 1:
                        editor.putInt("baolai", 2);
                        break;
                    case 2:
                        editor.putInt("baolai", 6);
                        break;
                    case 3:
                        editor.putInt("baolai", 10);
                        break;
                }
                int pt = phuongtien.getSelectedItemPosition();
                editor.putInt("phuongtien", pt);
                if(swOnOff.isChecked()){
                    editor.putInt("on", 1);
                }
                else editor.putInt("on", 0);
                editor.commit();
                Toast toast = Toast.makeText(SettingActivity.this, "Đã lưu", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
    void  anhxa(){
        swOnOff = findViewById(R.id.onoff);
        thoigian = findViewById(R.id.thoigian);
        baolai = findViewById(R.id.baoLai);
        btnLuu = findViewById(R.id.luu);
        phuongtien = findViewById(R.id.phuongtien);
        int intThongbao = sharedPreferences.getInt("thoigian", 1),
                intBaolai = sharedPreferences.getInt("baolai", 1);

        int intPhuongtien = (int)sharedPreferences.getInt("phuongtien", 1);
        ArrayList<String> arrPhuongtien = new ArrayList<String>(Arrays.asList(new String[]{getString(R.string.dibo),getString(R.string.xedap), getString(R.string.xemay), getString(R.string.oto)}));
        ArrayAdapter adapterPhuongtien = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, arrPhuongtien);
        phuongtien.setAdapter(adapterPhuongtien);
        phuongtien.setSelection(intPhuongtien);
        ArrayList<String> arrthoigian = new ArrayList<String>(Arrays.asList(new String[]{"5"+getString(R.string.phut), "10"+getString(R.string.phut), "15"+getString(R.string.phut), "30"+getString(R.string.phut)}));
        ArrayAdapter adapterThoiGian = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, arrthoigian);
        thoigian.setAdapter(adapterThoiGian);
        ArrayList<String> arrBaoLai = new ArrayList<String>(Arrays.asList(new String[]{"30"+ getString(R.string.giay), "1"+getString(R.string.phut), "3"+getString(R.string.phut), "5"+getString(R.string.phut)}));
        ArrayAdapter adapterBaolai = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, arrBaoLai);
        baolai.setAdapter(adapterBaolai);

        switch (intBaolai){
            case 1:
                baolai.setSelection(0);
                break;
            case 2:
                baolai.setSelection(1);
                break;
            case 6:
                baolai.setSelection(2);
                break;
            case 10:
                baolai.setSelection(3);
                break;
        }
        switch (intThongbao){
            case 5:
                thoigian.setSelection(0);
                break;
            case 10:
                thoigian.setSelection(1);
                break;
            case 15:
                thoigian.setSelection(2);
                break;
            case 30:
                thoigian.setSelection(3);
                break;

        }
    }

}