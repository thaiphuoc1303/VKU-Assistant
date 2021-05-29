package com.example.notifition;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.notifition.model.Distances;
import com.example.notifition.model.Durations;
import com.example.notifition.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceNotification extends Service {
    DataBase dataBase = new DataBase(this, "database.sqlite", null, 1);
    Cursor cursor;
    Route routes;
    int khoangcach = 0, thoigian = 0;
    String kc = "1 km";
    int conlai = 15;
    double lat = 18.510376, lng = 105.549071;
    LocationListener locationListener;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;

    SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("HH mm");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH: mm dd-MM-yyyy");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("SSS", "sev");
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = (double) location.getLatitude();
                lng = (double) location.getLongitude();
            }
        };
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, ServiceAction.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 1, notificationIntent, 0);
        Notification notification =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, "channel")
                    .setContentTitle(getString(R.string.nhacnho))
                    .setContentText("VKU Assistant đang chạy")
                    .setSmallIcon(R.drawable.notification)
                    .setContentIntent(pendingIntent).setAutoCancel(true).setAutoCancel(true)
                    .build();
        }
        startForeground(1, notification);

        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String datekey = dateFormat.format(calendar.getTime());
                String dS = simpleDateFormat.format(calendar.getTime());
                int thu = calendar.get(Calendar.DAY_OF_WEEK);
                int baotruoc = sharedPreferences.getInt("thoigian", 15);
                cursor = dataBase.GetData("SELECT * FROM lichhoc WHERE thu = "
                        + thu
                );
                while (cursor.moveToNext()) {
                    Date d1, d2;
                    String s1 = simpleDateFormatMain.format(calendar.getTime());
                    String s2 = cursor.getString(4);
                    try {
                        d1 = simpleDateFormatMain.parse(s1);
                        d2 = simpleDateFormatMain.parse(s2);
                        long diff = d2.getTime() - d1.getTime();
                        diff = diff / (60000);
                        if (diff <= baotruoc && diff >= -20) {
                            if (diff > 0){
                                int trangthai =Integer.parseInt(cursor.getString(5));
                                if (trangthai == 0){
                                    long finalDiff = diff;
                                    valueApi(cursor.getString(6));
                                    String mess = cursor.getString(2) + getString(R.string.batdausau) + finalDiff + getString(R.string.phut) + getString(R.string.khoangcach) + kc;
                                    if(diff< conlai) {
                                        mess+= ".\nĐi nhanh kẻo muộn giờ học.";
                                    }
//                                    else mess+= "\nCòn sớm, qua gọi ny đi học cùng luôn";
                                    addNotification(getString(R.string.lichhochomnay), mess, cursor.getString(0),
                                            Integer.parseInt(cursor.getString(6)));
                                }
                            }
                            else{
                                dataBase.QueryData("UPDATE lichhoc SET trangthai = 0 WHERE id = "+ cursor.getString(0));
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                String sql = "SELECT * FROM nhacnho WHERE thoigian LIKE '%"+ datekey+ "'";
                cursor = dataBase.GetData(sql);
                while (cursor.moveToNext()){
                    String time = cursor.getString(3);
                    Date d1, d2;
                    try {
                        d1 = simpleDateFormat.parse(time);
                        d2 = simpleDateFormat.parse(dS);
                        long diff = d1.getTime() - d2.getTime();
                        diff = diff / (60000);
                        if (diff <= baotruoc && diff >= -20) {
                            if (diff > 0){
                                int trangthai =Integer.parseInt(cursor.getString(5));
                                if(trangthai==0){
                                    addNotifications(cursor.getString(1), cursor.getString(2), cursor.getString(0));
                                }
                            }
                            else {
                                Calendar cld = Calendar.getInstance();
                                cld.setTime(simpleDateFormat.parse(time));
                                switch (cursor.getString(4)){
                                    case "0":
                                        dataBase.QueryData("UPDATE nhacnho SET laplai = -1 WHERE id = "+ cursor.getString(0));
                                        break;
                                    case "1":
                                        cld.roll(Calendar.DATE, 7);
                                        String tg = simpleDateFormat.format(cld.getTime());
                                        dataBase.QueryData("UPDATE nhacnho SET trangthai = 0, thoigian = '"+ tg + "' WHERE id = "+ cursor.getString(0));
                                        break;
                                    case "2":
                                        cld.roll(Calendar.MONTH, 1);
                                        String tg1 = simpleDateFormat.format(cld.getTime());
                                        dataBase.QueryData("UPDATE nhacnho SET trangthai = 0, thoigian = '"+ tg1 + "' WHERE id = "+ cursor.getString(0));
                                        break;
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                int dl = sharedPreferences.getInt("baolai", 1);
                mHandler.postDelayed(this, dl*30000);
            }
        }, 1000);

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    public  void addNotifications(String tieude, String noidung, String id){
        Intent intent = new Intent(this, ServiceAction.class);
        Intent intent1 = new Intent(this, check.class);
        intent1.putExtra("id", id);
        intent1.putExtra("table", "nhacnho");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(tieude)
                .setContentText(noidung)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.check, getString(R.string.hoanthanh), pendingIntent1)
                .setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    public void addNotification(String tde, String ndung, String id, int diadiemId) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        Intent notificationIntent = new Intent(this, ChiTietActivity.class);
        Intent intent = new Intent(this, check.class);
        Intent intentMap = new Intent(this, MapActivity.class);
        notificationIntent.putExtra("id", id);
        intentMap.putExtra("data", diadiemId);
        intent.putExtra("id", id);
        intent.putExtra("table", "lichhoc");

        notificationIntent.putExtra("mode", 1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentMap = PendingIntent.getActivity(this, 0, intentMap, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(tde)
                .setContentText(ndung)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_hoanthanh, getString(R.string.hoanthanh), pendingIntent1)
                .addAction(R.drawable.end_green, getString(R.string.chiduong), pendingIntentMap)
                .setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_description);
            String description = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void valueApi(String id) {
        Cursor cursor1 = dataBase.GetData("SELECT * FROM diadiem WHERE id = "+ id);

        OkHttpClient httpClient= new OkHttpClient();

        String origin = lat + "%2c"+ lng;
        String destination= "";
        String mode = "walking";
        while (cursor1.moveToNext()){
            destination = cursor1.getString(3)+"%2c"+ cursor1.getString(4);
        }


        String url = "https://rsapi.goong.io/Direction?origin="+origin+
                "&destination="+destination+"&vehicle=car&api_key=O2RZBszkiGblwi02rA2A7ftHuXts5FCz3WCKM4AT";

//                "https://maps.googleapis.com/maps/api/directions/json?origin="+origin
//                +"&destination="+destination+"&mode="+smode+"&key=" + R.string.API;
        Log.e("SSS", url);
        Request request = new Request.Builder().url(url).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data = response.body().string();
                try {
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray jsonRoutes = jsonData.getJSONArray("routes");
                    JSONObject jsonRoute = jsonRoutes.getJSONObject(0);
                    Log.e("SSS", jsonRoute.toString());
                    JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                    JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                    JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                    JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                    Log.e("SSS", jsonDistance.toString());
                    Durations duration
                            = new Durations(jsonDuration.getString("text"), jsonDuration.getInt("value"));
                    Distances distance
                            = new Distances(jsonDistance.getString("text"), jsonDistance.getInt("value"));
                    khoangcach = distance.getValue();
                    thoigian = duration.getValue();

                    int v =30;
                    int intMode = sharedPreferences.getInt("phuongtien", 0);

                    switch (intMode){
                        case 0:
                            v = 3;
                            break;
                        case 1:
                            v = 5;
                            break;
                        case 2:
                            v = 12;
                            break;
                        case 3:
                            v = 14;
                            break;
                    }
                    kc = distance.getText();
                    Log.e("SSS", kc);
                    conlai = distance.value/(v*60);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
