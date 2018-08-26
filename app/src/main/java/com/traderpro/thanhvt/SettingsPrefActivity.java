package com.traderpro.thanhvt;

import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.traderpro.GCM.Config;

public class SettingsPrefActivity extends AppCompatActivity {

    Button btnCopy;

    EditText edKey;
    ImageView imgBTC;
    ImageView imgETH;
    Switch onOffSwitch;
    TextView tvNgonNgu;
    TextView tvNameNN;
    TextView tvKeyCode;
    TextView tvDesDonate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pref);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        edKey = (EditText) findViewById(R.id.edKey);

        imgBTC = (ImageView) findViewById(R.id.imageButton);
        imgETH = (ImageView) findViewById(R.id.imageButton2);
        tvNgonNgu = (TextView) findViewById(R.id.textView39);
        tvKeyCode = (TextView) findViewById(R.id.textView_key);
        tvDesDonate = (TextView) findViewById(R.id.textView2);
        onOffSwitch = (Switch) findViewById(R.id.switch1);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                if(!isChecked) {
                    tvNgonNgu.setText(R.string.lang);
                    tvKeyCode.setText(R.string.key_code);
                    tvDesDonate.setText(R.string.description_donate);
                }else {
                    tvNgonNgu.setText(R.string.lang_Vn);
                    tvKeyCode.setText(R.string.key_code_Vn);
                    tvDesDonate.setText(R.string.description_donate_Vn);
                    //onOffSwitch.setText();
                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.NGON_NGU, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("NN", isChecked == true ? "VN" : "EN");
                editor.commit();
            }

        });

        SharedPreferences pref = getSharedPreferences(Config.NGON_NGU, 0);
        String strNN = pref.getString("NN", "VN");
        onOffSwitch.setChecked(strNN.equals("VN") ? true : false);
        //tvNgonNgu.setText();
        tvNgonNgu.setText(strNN.equals("VN") ? R.string.lang_Vn : R.string.lang);
        tvKeyCode.setText(strNN.equals("VN") ? R.string.key_code_Vn : R.string.key_code);
        tvDesDonate.setText(strNN.equals("VN") ? R.string.description_donate_Vn : R.string.description_donate);


        String key = getIntent().getStringExtra("KEY");
        //Log.e("key", key);
        edKey.setText(key);
        btnCopy = (Button) findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(edKey.getText().toString());
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
            }
        });

        imgBTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText("1c2J3WR3SWwKiv7C8ApHvRq9ApJyGsNwr");
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
            }
        });

        imgETH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText("0x7e7207b528d9de0eb9c5fc02f8dea05091bf5a6d");
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
            }
        });
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
