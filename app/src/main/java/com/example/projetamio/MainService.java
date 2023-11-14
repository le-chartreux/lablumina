package com.example.projetamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    private Timer timer;

    public MainService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MainService", "Created");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("MainService", "Call of the timer task");
            }
        };
        this.timer = new Timer();
        this.timer.schedule(timerTask, 0, 30_000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.timer.cancel();
        Log.d("MainService", "Destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}