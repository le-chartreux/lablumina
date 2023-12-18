package com.example.projetamio.views;

import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class FetchedDataView {
    private final TextView name;

    private final ShapeableImageView led;

    private final TextView value;

    public FetchedDataView(TextView name, ShapeableImageView led, TextView value) {
        this.name = name;
        this.led = led;
        this.value = value;
    }

    public TextView getName() {
        return name;
    }

    public ShapeableImageView getLed() {
        return led;
    }

    public TextView getValue() {
        return value;
    }
}
