package com.example.projetamio.fetchedData;

import java.util.List;

public class FetchedData {
    private static final FetchedData FETCHED_DATA = new FetchedData();
    public List<SensorInformation> data;

    public static FetchedData getInstance() {
        return FETCHED_DATA;
    }

    public static void updateSingleton(FetchedData fetchedDataSingleton) {
        getInstance().data = fetchedDataSingleton.data;
    }
}
