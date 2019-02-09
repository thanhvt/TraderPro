package com.traderpro.thanhvt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;

public class CauHinhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hinh);

        getSupportFragmentManager().beginTransaction()
                // .replace(android.R.id.content, SettingsFragment())
                .replace(R.id.content, new SettingsFragment())
                .commit();

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            // Load the Preferences from the XML file
            addPreferencesFromResource(R.xml.pref);

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            setUI(sharedPrefs.getBoolean("NN", true));
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals("NN")) {
                setUI(sharedPreferences.getBoolean("NN", true));
            }
        }

        public void setUI(boolean nn) {
            if (!nn) {
                SwitchPreference sNN = (SwitchPreference) findPreference("NN");
                sNN.setTitle("Change Language");

                SwitchPreference sSOUND = (SwitchPreference) findPreference("SOUND");
                sSOUND.setTitle("Sound");

                SwitchPreference sVIBRATE = (SwitchPreference) findPreference("VIBRATE");
                sVIBRATE.setTitle("Vibrate");

                SwitchPreference TT1 = (SwitchPreference) findPreference("TT1");
                TT1.setTitle("Algorithm 1");

                SwitchPreference TT2 = (SwitchPreference) findPreference("TT2");
                TT2.setTitle("Algorithm 2");

                SwitchPreference TT3 = (SwitchPreference) findPreference("TT3");
                TT3.setTitle("Algorithm 3");

                SwitchPreference TT4 = (SwitchPreference) findPreference("TT4");
                TT4.setTitle("Algorithm 4");

                SwitchPreference TT5 = (SwitchPreference) findPreference("TT5");
                TT5.setTitle("Algorithm 5");

                SwitchPreference TT6 = (SwitchPreference) findPreference("TT6");
                TT6.setTitle("Algorithm 6");

                SwitchPreference TT7 = (SwitchPreference) findPreference("TT7");
                TT7.setTitle("Algorithm 7");

                SwitchPreference TT8 = (SwitchPreference) findPreference("TT8");
                TT8.setTitle("Algorithm 8");

                SwitchPreference TT9 = (SwitchPreference) findPreference("TT9");
                TT9.setTitle("Algorithm 9");

                SwitchPreference TT10 = (SwitchPreference) findPreference("TT10");
                TT10.setTitle("Algorithm 10");

                SwitchPreference TT11 = (SwitchPreference) findPreference("TT11");
                TT11.setTitle("Algorithm 11");

                SwitchPreference TT12 = (SwitchPreference) findPreference("TT12");
                TT12.setTitle("Algorithm 12");

                SwitchPreference TT13 = (SwitchPreference) findPreference("TT13");
                TT13.setTitle("Algorithm 13");
            } else {
                SwitchPreference sNN = (SwitchPreference) findPreference("NN");
                sNN.setTitle("Thiết lập ngôn ngữ");

                SwitchPreference sSOUND = (SwitchPreference) findPreference("SOUND");
                sSOUND.setTitle("Âm thanh");

                SwitchPreference sVIBRATE = (SwitchPreference) findPreference("VIBRATE");
                sVIBRATE.setTitle("Chế độ rung");

                SwitchPreference TT1 = (SwitchPreference) findPreference("TT1");
                TT1.setTitle("Thuật toán 1");

                SwitchPreference TT2 = (SwitchPreference) findPreference("TT2");
                TT2.setTitle("Thuật toán 2");

                SwitchPreference TT3 = (SwitchPreference) findPreference("TT3");
                TT3.setTitle("Thuật toán 3");

                SwitchPreference TT4 = (SwitchPreference) findPreference("TT4");
                TT4.setTitle("Thuật toán 4");

                SwitchPreference TT5 = (SwitchPreference) findPreference("TT5");
                TT5.setTitle("Thuật toán 5");

                SwitchPreference TT6 = (SwitchPreference) findPreference("TT6");
                TT6.setTitle("Thuật toán 6");

                SwitchPreference TT7 = (SwitchPreference) findPreference("TT7");
                TT7.setTitle("Thuật toán 7");

                SwitchPreference TT8 = (SwitchPreference) findPreference("TT8");
                TT8.setTitle("Thuật toán 8");

                SwitchPreference TT9 = (SwitchPreference) findPreference("TT9");
                TT9.setTitle("Thuật toán 9");

                SwitchPreference TT10 = (SwitchPreference) findPreference("TT10");
                TT10.setTitle("Thuật toán 10");

                SwitchPreference TT11 = (SwitchPreference) findPreference("TT11");
                TT11.setTitle("Thuật toán 11");

                SwitchPreference TT12 = (SwitchPreference) findPreference("TT12");
                TT12.setTitle("Thuật toán 12");

                SwitchPreference TT13 = (SwitchPreference) findPreference("TT13");
                TT13.setTitle("Thuật toán 13");
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(getContext())
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
