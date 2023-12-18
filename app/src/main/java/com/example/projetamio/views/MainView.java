package com.example.projetamio.views;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.example.projetamio.fetchedData.FetchedData;
import com.example.projetamio.fetchedData.FetchedDataRequest;
import com.example.projetamio.utils.Constants;
import com.example.projetamio.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainView {
    private List<FetchedDataView> fetchedDataViews;

    private TextView time;

    private Button update;

    private Button configuration;

    @SuppressLint("StaticFieldLeak")
    private static final MainView MAIN_VIEW = new MainView();

    // Get singleton with all MainView information.
    public static MainView getInstance() {
        return MAIN_VIEW;
    }

    /**
     * Make the configuration of the Update button. This button will call a function to make a request
     * and update the UI. Also, the button will be deactivated during a certain time to avoid spam.
     */
    public void configureUpdateButton() {
        // When button is clicked, disable to button (to avoid spam) and make a request.
        update.setOnClickListener((view) -> {
            update.setEnabled(false);
            FetchedDataRequest.getAndUpdateSensorInformation();

            // After BUTTON_DEACTIVATION_TIME_IN_SECONDS, the button should be able to be clicked again.
            new Handler().postDelayed(() -> {
                update.setEnabled(true);
            }, Constants.BUTTON_DEACTIVATION_TIME_IN_SECONDS * 1000);
        });
    }

    /**
     * Update the UI according to data.
     *
     * @param fetchedData -> JSON returned from the request.
     */
    public void updateViewAccordingToData(FetchedData fetchedData) {
        for (int i = 0; i < Constants.QUANTITY_OF_SENSORS; i++) {
            com.example.projetamio.fetchedData.FetchedData.SensorInformation currentSensorInformation = fetchedData.data.get(i);
            FetchedDataView currentFetchedDataView = fetchedDataViews.get(i);

            // Update the name of the sensor.
            currentFetchedDataView.getName().setText(String.valueOf(currentSensorInformation.mote));

            // In case the sensor value is more than a specific threshold, led is on.
            if (currentSensorInformation.value > Constants.SENSOR_THRESHOLD)
                currentFetchedDataView.getLed().setImageResource(R.color.led_on);
            else
                currentFetchedDataView.getLed().setImageResource(R.color.led_off);

            // Update the value of the sensor.
            currentFetchedDataView.getValue().setText(String.valueOf(currentSensorInformation.value));
        }

        // Format the date to a more readable value, and then update the UI with the current time.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        time.setText(formatter.format(LocalDateTime.now()));
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

    public void setConfiguration(Button configuration) {
        this.configuration = configuration;
    }

    public Button getConfiguration() {
        return configuration;
    }
}
