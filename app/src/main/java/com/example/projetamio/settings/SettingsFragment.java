package com.example.projetamio.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.projetamio.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

    }
    private void getMailAddressDst(){}

    private void getMailMessage(){}

    private void getStartTimeWorkdays(){}

    private void getEndTimeWorkdays(){}

    private void getStartTimeWeekend(){}

    private void getEndTimeWeekend(){}
}
