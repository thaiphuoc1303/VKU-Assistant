package com.example.notifition;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    int khoangcach = 0, thoigian = 0;
    String kc = "";
    double lat = 18.510376, lng = 105.549071;

    SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("HH mm");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        createNotificationChannel();

        Intent notificationIntent = new Intent(this, ServiceAction.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 1, notificationIntent, 0);
        Notification notification =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, "channel")
                    .setContentTitle("Thông báo")
                    .setContentText("VKU Assistant đang chạy")
                    .setSmallIcon(R.drawable.notification)
                    .setContentIntent(pendingIntent).setAutoCancel(true)
                    .build();
        }
        startForeground(0, notification);

    }

    private void startActivities(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Calendar calendar = Calendar.getInstance();
                int thu = calendar.get(Calendar.DAY_OF_WEEK);

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

                        if (diff <= 15 && diff > -20) {
                            if (diff > 0){
                                int trangthai =Integer.parseInt(cursor.getString(5));
                                Log.e("SSS", ""+trangthai);
                                if (trangthai == 0){
                                    valueApi();Log.e("SSS", ""+thu);
                                    String mess = "Học phần " + cursor.getString(2) + " bắt đầu sau " + diff + " phút. Khoảng cách:" + kc;
                                    addNotifications("Lịch học hôm nay", mess, cursor.getString(0));
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
                mHandler.postDelayed(this, 10000);
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

    public void addNotifications(String tde, String ndung, String id) {
        Intent notificationIntent = new Intent(this, NotificationDetailActivity.class);
        Intent intent = new Intent(this, check.class);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(tde)
                .setContentText(ndung)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_hoanthanh, "Hoàn thành", pendingIntent1)
                .setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
    // xin quyền
    public void checkPerMission(){

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

    class NotificationDetailActivity {
    }

    private void valueApi() {

        OkHttpClient httpClient= new OkHttpClient();

        String origin = lat+",%20"+lng;

//                "18.510376,%20105.549071";

        String destination= "16.057588,%20108.172309";
        String mode = "walking";
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination=16.057588,%20108.172309&mode=walking&key=AIzaSyDNI_ZWPqvdS6r6gPVO50I4TlYkfkZdXh8";
//                    "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin+ "&destination="
//                    + destination+ "&mode=" + mode+ "&key=" + "AIzaSyDNI_ZWPqvdS6r6gPVO50I4TlYkfkZdXh8";
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
                    JSONArray jsonRoute = jsonData.getJSONArray("routes");
                    JSONArray jsonLegs = jsonRoute.getJSONObject(0).getJSONArray("legs");
                    JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                    JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                    JSONObject jsonDuration = jsonLeg.getJSONObject("duration");

                    Durations duration
                            = new Durations(jsonDuration.getString("text"), jsonDuration.getInt("value"));
                     Distances distance
                            = new Distances(jsonDistance.getString("text"), jsonDistance.getInt("value"));
                    khoangcach = distance.getValue();
                    thoigian = duration.getValue();
                    kc = distance.getText();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
