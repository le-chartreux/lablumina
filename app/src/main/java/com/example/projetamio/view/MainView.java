package com.example.projetamio.view;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.projetamio.R;
import com.example.projetamio.fetchedData.FetchedData;
import com.example.projetamio.fetchedData.FetchedDataRequest;
import com.example.projetamio.fetchedData.SensorInformation;
import com.example.projetamio.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainView {
    @SuppressLint("StaticFieldLeak")
    private static final MainView MAIN_VIEW = new MainView();
    private List<FetchedDataView> fetchedDataViews;
    private TextView time;
    private Button update;
    private Button configuration;

    public static MainView getInstance() {
        return MAIN_VIEW;
    }

    public void configureUpdateButton() {
        // When button is clicked, disable to button (to avoid spam) and make a request.
        update.setOnClickListener((view) -> {
            update.setEnabled(false);
            FetchedDataRequest.getAndUpdateSensorInformation();

            // After BUTTON_DEACTIVATION_TIME_IN_SECONDS, the button should be able to be clicked again.
            new Handler().postDelayed(() -> update.setEnabled(true), Constants.BUTTON_DEACTIVATION_TIME_IN_SECONDS * 1000);
        });
    }

    public void updateViewAccordingToData(FetchedData fetchedData) {
        if (fetchedData.data.size() == 0) {
            Log.d("debug", "No data fetched");
            return;
        }

        int limit = Math.min(Constants.MAX_NUMBER_OF_SENSORS, fetchedData.data.size());
        for (int i = 0; i < limit; i++) {
            SensorInformation currentSensorInformation = fetchedData.data.get(i);
            FetchedDataView currentFetchedDataView = fetchedDataViews.get(i);

            // When the sensor value is more than a specific threshold, led is on.
            if (currentSensorInformation.value > Constants.SENSOR_THRESHOLD)
                currentFetchedDataView.getBulb().post(() -> currentFetchedDataView.getBulb().setImageResource(R.drawable.bulb_on));
            else
                currentFetchedDataView.getBulb().post(() -> currentFetchedDataView.getBulb().setImageResource(R.drawable.bulb_off));

            currentFetchedDataView.getValue().post(() -> currentFetchedDataView.getValue().setText("Mote: " + currentSensorInformation.mote + " - Value : " + currentSensorInformation.value));
        }

        time.post(() -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            time.setText(formatter.format(LocalDateTime.now()));
        });
    }

    public void setSensorShow(List<FetchedDataView> fetchedDataViews) {
        this.fetchedDataViews = fetchedDataViews;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public void setUpdate(Button update) {
        this.update = update;
    }

    public Button getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Button configuration) {
        this.configuration = configuration;
    }
}
