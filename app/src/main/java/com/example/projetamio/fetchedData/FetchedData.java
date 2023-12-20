package com.example.projetamio.fetchedData;

import java.util.List;

public class FetchedData {
    public List<SensorInformation> data;

    private static final FetchedData FETCHED_DATA = new FetchedData();

    public static FetchedData getInstance() {
        return FETCHED_DATA;
    }

    public static void updateSingleton(FetchedData fetchedDataSingleton) {
        getInstance().data = fetchedDataSingleton.data;
    }
}
