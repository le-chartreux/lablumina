package com.example.projetamio.mail;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.projetamio.fetchedData.FetchedData;
import com.example.projetamio.utils.Constants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class MailService extends Service {

    private Timer timer;
    private int startTimeS = 23;
    private int endTimeS = 6;
    private int startTimeW = 19;
    private int endTimeW = 23;
    private String addrDst;
    private String message;

    public MailService() {
    }

    public void onCreate() {
        Log.d("MailService", "Created");
        updateSetting();
        this.verifyLights();
    }


    public void onDestroy() {
        this.timer.cancel();
        Log.d("MailService", "Destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public void verifyLights() {
        this.timer = new Timer();
        final Handler handler = new Handler();
        TimerTask task = new TimerTask() {
            public void run() {
                handler.post(() -> {
                    updateSetting();
                    if (checkDay()) {
                        if (checkTime(startTimeS, endTimeS)) {
                            if (FetchedData.getInstance().data != null) {
                                sendMail();
                            }
                        }
                    } else {
                        if (checkTime(startTimeW, endTimeW)) {
                            if (FetchedData.getInstance().data != null) {
                                sendMail();
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, Constants.SENSOR_UPDATE_INTERVAL_IN_MINUTES * 1000 * 60);
    }

    public void sendMail() {
        if (addrDst != null && message != null) {
            MailClient mailClient = new MailClient(addrDst, "IOT Lab Lights status", message);
            mailClient.execute();
            Log.d("MailService", "Email sent.");
        } else {
            Log.d("MailService", "Email sending failed.");
        }
    }

    private boolean checkDay() {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        return dayOfWeek.getValue() >= DayOfWeek.MONDAY.getValue() &&
                dayOfWeek.getValue() <= DayOfWeek.FRIDAY.getValue();
    }

    private boolean checkTime(int startTime, int endTime) {
        if (endTime > startTime) {
            return LocalTime.now().isAfter(LocalTime.of(startTime, 0)) &&
                    LocalTime.now().isBefore(LocalTime.of(startTime, 0));
        } else {
            return LocalTime.now().isAfter(LocalTime.of(startTime, 0)) ||
                    LocalTime.now().isBefore(LocalTime.of(endTime, 0));
        }
    }

    private void updateSetting() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.addrDst = preferences.getString("addressMail", "swan.frere@telecomnancy.net");
        this.message = preferences.getString("message", "Warning, a new light is on!");
        this.startTimeS = Integer.parseInt(preferences.getString("start_time_s", "19"));
        this.endTimeS = Integer.parseInt(preferences.getString("end_time_s", "23"));
        this.startTimeW = Integer.parseInt(preferences.getString("start_time_w", "23"));
        this.endTimeW = Integer.parseInt(preferences.getString("end_time_w", "6"));
    }
}
