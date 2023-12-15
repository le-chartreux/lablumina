package com.example.projetamio.ui.theme;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetamio.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchDataTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private View rootView;

    public FetchDataTask(Context mContext, View rootView) {
        this.mContext = mContext;
        this.rootView = rootView;
    }

    @Override
    protected String doInBackground(String... urls) {
        int responseCode = 0;
        StringBuilder allData = new StringBuilder();
        String lastValue = "";
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Toast.makeText(mContext, responseCode, Toast.LENGTH_LONG).show();
            }
            Log.d("HTTP Response", "Code: " + responseCode);

            InputStream in = new BufferedInputStream(connection.getInputStream());
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("data")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        String timestamp = "", label = "", value = "", mote = "";
                        while (reader.hasNext()) {
                            String n = reader.nextName();
                            switch (n) {
                                case "timestamp":
                                    timestamp = reader.nextString();
                                    break;
                                case "label":
                                    label = reader.nextString();
                                    break;
                                case "value":
                                    value = reader.nextString();
                                    break;
                                case "mote":
                                    mote = reader.nextString();
                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }
                        }
                        lastValue = "Valeur: " + value + ", Date: " + timestamp;
                        allData.append("Mote: ").append(mote).append(", Label: ").append(label).append(", Valeur: ").append(value).append(", Timestamp: ").append(timestamp).append("\n");
                        reader.endObject();
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Dernière valeur: " + lastValue + "\n\nToutes les données:\n" + allData.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("HTTP Response", "Body: " + result);
        TextView tvLastValue = rootView.findViewById(R.id.tvLastValue);
        TextView tvAllData = rootView.findViewById(R.id.tvAllData);
        tvLastValue.setText(result.split("\n\n")[0]); // Afficher dernière valeur
        tvAllData.setText(result.split("\n\n")[1]); // Afficher toutes les données
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }
}