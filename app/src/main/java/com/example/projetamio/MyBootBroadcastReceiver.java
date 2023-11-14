package com.example.projetamio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class MyBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBootBroadcastReceiver", "Boot message received");
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.projetamio", context.MODE_PRIVATE);
        boolean startAtBoot = sharedPreferences.getBoolean("com.example.projetamio.startatboot", false);
        if (startAtBoot) {
            context.startService(new Intent(context, MainService.class));
        }
    }
}