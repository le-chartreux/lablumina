package com.example.projetamio;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;

import com.example.projetamio.ui.theme.FetchDataTask;

public class MainActivity extends Activity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureSharedPreferences();
        this.setStartAtBootCheckBoxFromSharedPreferences();
        this.setOnCheckedChangeListenerForToggleButton1();
        this.setOnCheckedChangeListenerForCheckBox();
        // register broadcast receiver
        MyBootBroadcastReceiver myBootBroadcastReceiver = new MyBootBroadcastReceiver();
        IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        int receiverFlags = 0;
        boolean listenToBroadcastsFromOtherApps = false;
        if (listenToBroadcastsFromOtherApps) {
            receiverFlags = ContextCompat.RECEIVER_EXPORTED;
        } else {
            receiverFlags = ContextCompat.RECEIVER_NOT_EXPORTED;
        }
        ContextCompat.registerReceiver(this, myBootBroadcastReceiver, filter, receiverFlags);

        Button btnRequest = this.findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchDataTask(MainActivity.this, findViewById(android.R.id.content).getRootView()).execute("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last");
            }
        });

        Log.d("MainActivity", "Created");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "Destroyed");
    }

    private void configureSharedPreferences() {
        this.sharedPreferences = this.getSharedPreferences("com.example.projetamio", Context.MODE_PRIVATE);
        if (! this.sharedPreferences.contains("com.example.projetamio.startatboot")) {
            this.sharedPreferences.edit().putBoolean("com.example.projetamio.startatboot", false).apply();
        }
    }

    private void setStartAtBootCheckBoxFromSharedPreferences() {
        CheckBox checkBox = this.findViewById(R.id.checkBox);
        checkBox.setChecked(this.sharedPreferences.getBoolean("com.example.projetamio.startatboot", false));
    }

    private void setOnCheckedChangeListenerForCheckBox() {
        CheckBox checkBox = this.findViewById(R.id.checkBox);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = ((buttonView, isChecked) -> {
            this.sharedPreferences.edit().putBoolean("com.example.projetamio.startatboot", isChecked).apply();
            Log.d("MainActivity", String.format("%b", this.sharedPreferences.getBoolean("com.example.projetamio.startatboot", false)));
        });
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void setOnCheckedChangeListenerForToggleButton1() {
        TextView textView2 = this.findViewById(R.id.textView2);
        ToggleButton toggleButton1 = this.findViewById(R.id.toggleButton1);
        ToggleButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            if (isChecked) {
                startService(new Intent(this, MainService.class));
                textView2.setText("Running");
            } else {
                stopService(new Intent(this, MainService.class));
                textView2.setText("Stopped");
            }
        };
        toggleButton1.setOnCheckedChangeListener(onCheckedChangeListener);
    }

}
