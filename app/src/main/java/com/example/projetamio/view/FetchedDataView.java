package com.example.projetamio.view;

import android.widget.ImageView;
import android.widget.TextView;

public class FetchedDataView {
    private final ImageView bulb;

    private final TextView value;

    public FetchedDataView(ImageView bulb, TextView value) {
        this.bulb = bulb;
        this.value = value;
    }

    public ImageView getBulb() {
        return bulb;
    }

    public TextView getValue() {
        return value;
    }
}
