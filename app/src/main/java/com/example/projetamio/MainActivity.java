package com.example.projetamio;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.projetamio.mail.MailClient;
import com.example.projetamio.mail.MailService;
import com.example.projetamio.notifications.NotificationService;
import com.example.projetamio.settings.SettingsActivity;
import com.example.projetamio.view.FetchedDataView;
import com.example.projetamio.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadViewElements();

        MainView.getInstance().configureUpdateButton();
        associateConfigurationButton();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermissions();
        }
        Intent notificationService = new Intent(this, NotificationService.class);
        startService(notificationService);

        Intent mailService = new Intent(this, MailService.class);
        startService(mailService);
    }

    /**
     * Load all elements from the UI. These elements are stored in the MainView class.
     */
    private void loadViewElements() {
        List<FetchedDataView> fetchedDataViews = new ArrayList<>();
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.imageView1_2), findViewById(R.id.textView1)));
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.imageView2_2), findViewById(R.id.textView2)));
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.imageView3_2), findViewById(R.id.textView3)));
        fetchedDataViews.add(new FetchedDataView(findViewById(R.id.imageView4_2), findViewById(R.id.textView4)));
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
}
