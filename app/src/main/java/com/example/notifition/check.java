package com.example.notifition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class check extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
       String id = intent.getStringExtra("id");
        String table = intent.getStringExtra("table");
        DataBase dataBase = new DataBase(context, "database.sqlite", null, 1);
        dataBase.QueryData("UPDATE "+ table +" SET trangthai = 1 WHERE id = " + id);
    }
}
