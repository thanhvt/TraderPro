package com.traderpro.thanhvt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.traderpro.GCM.Config;

public class SettingsPrefActivity extends AppCompatActivity {

//    Button btnCopy;

//    EditText edKey;

    Switch onOffSwitch;
    TextView tvNgonNgu;
    TextView tvNameNN;
    TextView tvSound;
    TextView tvVibrate;
    Switch onOffSwitchSound;
    Switch onOffSwitchVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pref);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        edKey = (EditText) findViewById(R.id.edKey);

        tvNgonNgu = (TextView) findViewById(R.id.textView39);
        tvSound = (TextView) findViewById(R.id.textView_Sound);
        tvVibrate = (TextView) findViewById(R.id.textView_Sound2);
        onOffSwitch = (Switch) findViewById(R.id.switch1);
        onOffSwitchSound = (Switch) findViewById(R.id.switch2);
        onOffSwitchVibrate = (Switch) findViewById(R.id.switch3);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                if (!isChecked) {
                    tvNgonNgu.setText(R.string.lang);
                    tvSound.setText(R.string.sound);
                    tvVibrate.setText(R.string.vibrate);
                } else {
                    tvNgonNgu.setText(R.string.lang_Vn);
                    tvSound.setText(R.string.sound_Vn);
                    tvVibrate.setText(R.string.vibrate_Vn);
                    //onOffSwitch.setText();
                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.NGON_NGU, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("NN", isChecked == true ? "VN" : "EN");
                editor.commit();
            }

        });

        onOffSwitchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                if (!isChecked) {

                } else {

                    //onOffSwitch.setText();
                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SOUND, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("SOUND", isChecked == true ? "ON" : "OFF");
                editor.commit();
            }

        });

        onOffSwitchVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                if (!isChecked) {

                } else {

                    //onOffSwitch.setText();
                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.VIBRATE, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("VIBRATE", isChecked == true ? "ON" : "OFF");
                editor.commit();
            }

        });
        SharedPreferences pref = getSharedPreferences(Config.NGON_NGU, 0);
        String strNN = pref.getString("NN", "VN");
        SharedPreferences pref2 = getSharedPreferences(Config.SOUND, 0);
        String strSound = pref2.getString("SOUND", "ON");
        SharedPreferences pref3 = getSharedPreferences(Config.VIBRATE, 0);
        String strVibrate = pref3.getString("VIBRATE", "ON");
        onOffSwitch.setChecked(strNN.equals("VN") ? true : false);
        onOffSwitchSound.setChecked(strSound.equals("ON") ? true : false);
        onOffSwitchVibrate.setChecked(strVibrate.equals("ON") ? true : false);
        //tvNgonNgu.setText();
        tvNgonNgu.setText(strNN.equals("VN") ? R.string.lang_Vn : R.string.lang);
        tvSound.setText(strNN.equals("VN") ? R.string.sound_Vn : R.string.sound);
        tvVibrate.setText(strNN.equals("VN") ? R.string.vibrate_Vn : R.string.vibrate);

        // sound
        if (strSound.equals("ON")) {
            //
        } else {
            //
        }

        String key = getIntent().getStringExtra("KEY");
        //Log.e("key", key);
//        edKey.setText(key);
//        btnCopy = (Button) findViewById(R.id.btnCopy);
//        btnCopy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                clipboard.setText(edKey.getText().toString());
//                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
//            }
//        });

//        imgBTC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                clipboard.setText("1c2J3WR3SWwKiv7C8ApHvRq9ApJyGsNwr");
//                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        imgETH.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                clipboard.setText("0x7e7207b528d9de0eb9c5fc02f8dea05091bf5a6d");
//                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
