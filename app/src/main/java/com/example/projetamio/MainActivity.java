package com.example.projetamio;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.projetamio.mail.MailClient;
import com.example.projetamio.notifications.NotificationService;
import com.example.projetamio.settings.SettingsActivity;
import com.example.projetamio.views.FetchedDataView;
import com.example.projetamio.views.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug","First");
        super.onCreate(savedInstanceState);
        Log.d("debug","Second");
        setContentView(R.layout.activity_main);
        Log.d("debug","Third");

        // Load all elements from the UI.
        loadViewElements();
        Log.d("debug","Four");

        // Associate button with sensor update.
        MainView.getInstance().configureUpdateButton();
        Log.d("debug","Five");

        // Associate button with setting activity.
        associateConfigurationButton();
        Log.d("debug","Six");


        // Load main service
        Intent mainService = new Intent(this, MainService.class);
        Log.d("debug","Seven");
        startService(mainService);
        Log.d("debug","Eight");


        // Send mail service
        sendMail("frereswan@gmail.com");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermissions();
        }

        Intent intent = new Intent(this, NotificationService.class);
        super.startService(intent);
    }

    /**
     * Load all elements from the UI. These elements are stored in the MainView class.
     */
    private void loadViewElements() {
        List<FetchedDataView> fetchedDataViews = new ArrayList<>();
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.name_1), findViewById(R.id.led_1), findViewById(R.id.value_1)));
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.name_2), findViewById(R.id.led_2), findViewById(R.id.value_2)));
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.name_3), findViewById(R.id.led_3), findViewById(R.id.value_3)));
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.name_4), findViewById(R.id.led_4), findViewById(R.id.value_4)));
        MainView.getInstance().setSensorShow(fetchedDataViews);

        TextView time = findViewById(R.id.time);
        MainView.getInstance().setTime(time);

        Button update = findViewById(R.id.update);
        MainView.getInstance().setUpdate(update);

        Button configuration = findViewById(R.id.configuration);
        MainView.getInstance().setConfiguration(configuration);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkNotificationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Random number, just to verify if the result was good.
            int requestCode = 1;
            boolean isPostPermitted = ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.POST_NOTIFICATIONS);

            if (!isPostPermitted) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    /**
     * Associate the configuration button with setting activities
     */
    private void associateConfigurationButton(){
        MainView.getInstance().getConfiguration().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This function allow us to send a mail with two given address TODO: compete this function and associate with a button
     * @param destAddress the e-mail address that we want to send to
     */
    private void sendMail(String destAddress){

        MailClient client = new MailClient(this, destAddress , "AMIO", "Salut c'est moi Tchoupi");

        client.execute();
        Log.d("debug","Sending Mail");
    }
}
