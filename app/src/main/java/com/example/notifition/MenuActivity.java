package com.example.notifition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.Permission;

public class MenuActivity extends AppCompatActivity {
    ImageView imgLogo, imgbar, imgtkb, imgnhacnho, imgmap, imgsetting, imgSun, imgchim,
            iconTKB, iconNhacNho, iconMap, iconSetting;
    TextView tvTKB, tvNhacNho, tvMap, tvSetting;
    DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            Uri uri = Uri.fromParts("package", getPackageName(), null);
//            intent.setData(uri);
//            startActivity(intent);

        }
        else {
            Intent intent = new Intent(this, ServiceNotification.class);
            startService(intent);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        anhxa();
        imgtkb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        imgnhacnho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ListCVActivity.class);
                startActivity(intent);
            }
        });
        imgmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MapMenuActivity.class);
                startActivity(intent);
            }
        });
        imgsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        dataBase = new DataBase(this, "database.sqlite", null, 1);
        dataBase.QueryData("CREATE TABLE IF NOT EXISTS lichhoc (" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    thu       INTEGER," +
                "    hocphan   VARCHAR," +
                "    phong     VARCHAR," +
                "    thoigian VARCHAR," +
                "    trangthai INTEGER DEFAULT (0)," +
                "       diadiem VARCHAR" +
                ")");
        dataBase.QueryData("CREATE TABLE IF NOT EXISTS diadiem(" +
                "id      INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    ten     VARCHAR, " +
                "    chitiet VARCHAR, " +
                "    lat     VARCHAR DEFAULT (0), " +
                "    lng     VARCHAR DEFAULT (0) " +
                ")"
        );
        dataBase.QueryData("CREATE TABLE IF NOT EXISTS nhacnho(" +
                "id      INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    ten     VARCHAR, " +
                "    chitiet VARCHAR, " +
                "    thoigian     VARCHAR, " +
                "    laplai     VARCHAR DEFAULT (0)," +
                "   trangthai INTEGER DEFAULT (0)" +
                ")"
        );
        //thêm dữ liệu
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Dãy A Khu V', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.972284', '108.249423')");
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Dãy B Khu V', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.973209', '108.249612')");
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Khu K', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.975047', '108.252507')");
//        dataBase.QueryData("INSERT INTO diadiem (ten,chitiet,lat,lng) " +
//                "VALUES('Kí túc xá', 'Trường Đại học Công nghệ Thông tin & Truyền Thông Việt - Hàn', '15.973317', '108.252440')");

    }
    void anhxa(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        Animation animation1 =AnimationUtils.loadAnimation(MenuActivity.this, R.anim.sun);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.chim);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.item1);
        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.item2);

        imgbar = findViewById(R.id.imgbar);
        imgLogo = findViewById(R.id.imgLogo);
        imgtkb = findViewById(R.id.imgtkb);
        imgnhacnho = findViewById(R.id.imgnhacnho);
        imgmap = findViewById(R.id.imgmap);
        imgsetting = findViewById(R.id.imgsetting);
        imgchim = findViewById(R.id.imgchim);
        imgSun = findViewById(R.id.imgSun);
        iconTKB = findViewById(R.id.iconTKB);
        iconNhacNho = findViewById(R.id.iconNhacNho);
        iconMap = findViewById(R.id.iconMap);
        iconSetting = findViewById(R.id.iconSetting);
        tvMap = findViewById(R.id.tvMap);
        tvNhacNho = findViewById(R.id.tvNhacNho);
        tvSetting = findViewById(R.id.tvSetting);
        tvTKB = findViewById(R.id.tvTKB);

        imgLogo.setAnimation(animation);
        imgbar.setAnimation(animation);
        imgSun.startAnimation(animation1);
        imgchim.startAnimation(animation2);
        imgtkb.setAnimation(animation3);
        iconTKB.setAnimation(animation3);
        tvTKB.setAnimation(animation3);
        imgnhacnho.setAnimation(animation4);
        iconNhacNho.setAnimation(animation4);
        tvNhacNho.setAnimation(animation4);
        imgmap.setAnimation(animation3);
        iconMap.setAnimation(animation3);
        tvMap.setAnimation(animation3);
        imgsetting.setAnimation(animation4);
        iconSetting.setAnimation(animation4);
        tvSetting.setAnimation(animation4);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==10){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(this, ServiceNotification.class);
                startService(intent);
            }
        }
    }
}