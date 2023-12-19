package com.example.projetamio.fetchedData;

import androidx.annotation.NonNull;

import com.example.projetamio.utils.Constants;
import com.example.projetamio.view.MainView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchedDataRequest {
    /**
     * To get the sensor information, we should call this static function. To treat the result:
     * call.enqueue(new Callback() {...})
     * In this case, we will have two functions inside the Callback. On is in case of success and the
     * other one in case it fails.
     */
    public static Call getSensorInformation() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL)
                .build();

        return new OkHttpClient().newCall(request);
    }

    /**
     * This function make a new request to the URL, getting the status of the sensors. In case of
     * success, it updates the UI and all the singletons (SensorDataHolder and MainView).
     */
    public static void getAndUpdateSensorInformation() {
        // After the call specification, it will make the call.
        Call call = getSensorInformation();

        call.enqueue(new Callback() {
            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    // Convert JSON data to class and update the UI.
                    treatJSONResponseAndUpdateUI(response);
                }
            }

            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // No action.
            }
        });
    }

    /**
     * In case the request was a success, this function converts the JSON to a class and then
     * updates the UI.
     *
     * @param response -> The JSON response from the request.
     * @throws IOException -> ObjectMapper requests a IOException.
     */
    public static void treatJSONResponseAndUpdateUI(Response response) throws IOException {
        // If response body is not null, it will map the JSON to a class.
        ObjectMapper objectMapper = new ObjectMapper();
        FetchedData fetchedData = objectMapper.readValue(response.body().string(), FetchedData.class);
        FetchedData.updateSingleton(fetchedData);

        // After converting from JSON to a class, now the UI can be updated.
        MainView.getInstance().updateViewAccordingToData(fetchedData);
    }
}
