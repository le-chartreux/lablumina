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

    public static Call getSensorInformation() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL)
                .build();

        return new OkHttpClient().newCall(request);
    }

    public static void getAndUpdateSensorInformation() {
        // After the call specification, it will make the call.
        Call call = getSensorInformation();

        call.enqueue(new Callback() {
            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    treatJSONResponseAndUpdateUI(response);
                }
            }

            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // No action.
            }
        });
    }

    public static void treatJSONResponseAndUpdateUI(Response response) throws IOException {
        // If response body is not null, it will map the JSON to a class.
        ObjectMapper objectMapper = new ObjectMapper();
        FetchedData fetchedData = objectMapper.readValue(response.body().string(), FetchedData.class);
        FetchedData.updateSingleton(fetchedData);

        // After converting from JSON to a class, now the UI can be updated.
        MainView.getInstance().updateViewAccordingToData(fetchedData);
    }
}
