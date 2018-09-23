package com.traderpro.thanhvt;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.traderpro.GCM.Config;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class TradeApiActivity extends AppCompatActivity {

    EditText edBittrexPub, edBittrexPri, edBinancePub, edBinancePri;
    ToggleButton tgBot, tgAPI;
    EditText edBTC;
    final String TAG = "TradeAPI";
    TextView tvUsingBot;
    TextView tvUsingAPI;
    TextView tvAmount;
    TextView tvImportant;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_trade);
        //
        utils = new Utils(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefNN = getSharedPreferences(Config.NGON_NGU, 0);
        String strNN = prefNN.getString("NN", "VN");
        tvUsingBot = (TextView) findViewById(R.id.textView49);
        tvUsingBot.setText(strNN.equals("VN") ? R.string.title_using_bot_Vn : R.string.title_using_bot);
        tvUsingAPI = (TextView) findViewById(R.id.textView20);
        tvUsingAPI.setText(strNN.equals("VN") ? R.string.title_using_API_Vn : R.string.title_using_API);
        tvAmount = (TextView) findViewById(R.id.textView50);
        tvAmount.setText(strNN.equals("VN") ? R.string.des_amount_Vn : R.string.des_amount);
        tvImportant = (TextView) findViewById(R.id.textView51);
        tvImportant.setText(strNN.equals("VN") ? R.string.important_Vn : R.string.important);
        edBittrexPub = (EditText) findViewById(R.id.edBittrexPub);
        edBittrexPri = (EditText) findViewById(R.id.edBittrexPri);
        edBinancePub = (EditText) findViewById(R.id.edBinancePub);
        edBinancePri = (EditText) findViewById(R.id.edBinancePri);
        edBTC = (EditText) findViewById(R.id.edBTC);
        tgBot = (ToggleButton) findViewById(R.id.tgBot);
        tgAPI = (ToggleButton) findViewById(R.id.tgAPI);

        SharedPreferences pref = getSharedPreferences(Config.BOT_API, 0);
        if (pref != null) {
            int API = pref.getInt("USE_API", 0);
            if (API == 1) {
                //String strNN = pref.getString("NN", "VN");
                String bitPub = pref.getString("BIT_PUB", "");
                String bitPri = pref.getString("BIT_PRI", "");
                String binPub = pref.getString("BIN_PUB", "");
                String binPri = pref.getString("BIN_PRI", "");
                String amountBTC = pref.getString("AMOUNT_BTC", "");

                tgAPI.setChecked(true);
                edBittrexPri.setText(bitPri);
                edBittrexPub.setText(bitPub);
                edBinancePri.setText(binPri);
                edBinancePub.setText(binPub);
                edBTC.setText(amountBTC);
            } else {
                String bitPub = pref.getString("BIT_PUB", "");
                String bitPri = pref.getString("BIT_PRI", "");
                String binPub = pref.getString("BIN_PUB", "");
                String binPri = pref.getString("BIN_PRI", "");
                String amountBTC = pref.getString("AMOUNT_BTC", "");

                edBittrexPri.setText(bitPri);
                edBittrexPub.setText(bitPub);
                edBinancePri.setText(binPri);
                edBinancePub.setText(binPub);
                edBTC.setText(amountBTC);
                tgAPI.setChecked(false);

                edBittrexPri.setEnabled(false);
                edBittrexPub.setEnabled(false);
                edBinancePri.setEnabled(false);
                edBinancePub.setEnabled(false);
                edBTC.setEnabled(false);
            }
        }

        tgAPI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edBittrexPri.setEnabled(isChecked);
                edBittrexPub.setEnabled(isChecked);
                edBinancePri.setEnabled(isChecked);
                edBinancePub.setEnabled(isChecked);
                edBTC.setEnabled(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_api, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_save:
                boolean API = tgAPI.isChecked();
                boolean BOT = tgBot.isChecked();

                String bitPub = edBittrexPub.getText() != null ? edBittrexPub.getText().toString().trim() : "";
                String bitPri = edBittrexPri.getText() != null ? edBittrexPri.getText().toString().trim() : "";
                String binPub = edBinancePub.getText() != null ? edBinancePub.getText().toString().trim() : "";
                String binPri = edBinancePri.getText() != null ? edBinancePri.getText().toString().trim() : "";
                String amountBTC = edBTC.getText() != null ? edBTC.getText().toString() : "";

                if (API) {
                    if ((bitPub.length() > 0 && bitPri.length() == 0)
                            || (bitPub.length() == 0 && bitPri.length() > 0)
                            || (binPub.length() > 0 && binPri.length() == 0)
                            || (binPub.length() == 0 && binPri.length() > 0)
                            ) {
                        Toast.makeText(getApplicationContext(), "Please complete all required fields", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (amountBTC.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please complete all required fields", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    Double aBTC = Double.parseDouble(amountBTC);
                    if (aBTC <= 0) {
                        Toast.makeText(getApplicationContext(), "Amount BTC > 0", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.BOT_API, 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("BIT_PUB", bitPub);
                    editor.putString("BIT_PRI", bitPri);
                    editor.putString("BIN_PUB", binPub);
                    editor.putString("BIN_PRI", binPri);
                    editor.putString("AMOUNT_BTC", amountBTC);
                    editor.putInt("USE_API", API == true ? 1 : 0);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Success !", Toast.LENGTH_LONG).show();
                    //
                    UserDevice userDevices = new UserDevice();
                    userDevices.NHASX = utils.getManufacturer();
                    userDevices.TENTB = utils.getProductName();
                    userDevices.OS = "Android";
                    userDevices.SERIAL = utils.getSerialNumber();
                    userDevices.UUID = utils.getUuid();
                    userDevices.VERSION = utils.getOSVersion();
                    userDevices.BIT_PUB = bitPub;
                    userDevices.BIT_PRI = bitPri;
                    userDevices.BIN_PUB = binPub;
                    userDevices.BIN_PRI = binPri;
                    userDevices.AMOUT = amountBTC;
                    userDevices.ACTIVE = "1";
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String regId = prefs.getString("regId", null);
                    userDevices.DEVICE_TOKEN = regId;
                    utils.insertData(userDevices);
                    try {
                        String serial = android.os.Build.SERIAL;
                        JSONObject jsonPush = new JSONObject();
                        jsonPush.put("SERIAL", serial);
                        jsonPush.put("BIT_PUB", bitPub);
                        jsonPush.put("BIT_PRI", bitPri);
                        jsonPush.put("BIN_PUB", binPub);
                        jsonPush.put("BIN_PRI", binPri);
                        jsonPush.put("AMOUNT_BTC", amountBTC);
                        jsonPush.put("USE_API", API == true ? 1 : 0);
                        String push = "SERIAL+" + serial + "+SERIAL|"
                                + "BIT_PUB+" + bitPub + "+BIT_PUB|"
                                + "BIT_PRI+" + bitPri + "+BIT_PRI|"
                                + "BIN_PUB+" + binPub + "+BIN_PUB|"
                                + "BIN_PRI+" + binPri + "+BIN_PRI|"
                                + "AMOUNT_BTC+" + amountBTC + "+AMOUNT_BTC|"
                                + "USE_API+" + (API == true ? 1 : 0) + "+USE_API|";
                        new AsyncPush().execute("CONFIG API", push);
                    } catch (Exception e) {

                    }
                } else {
                    String serial = android.os.Build.SERIAL;
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.BOT_API, 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("USE_API", API == true ? 1 : 0);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Success !", Toast.LENGTH_LONG).show();
                    String push = "SERIAL+" + serial + "+SERIAL|"
                            + "USE_API+" + (API == true ? 1 : 0) + "+USE_API|";
                    new AsyncPush().execute("CONFIG API", push);
                }

                super.onBackPressed();
                return true;
        }
//        int id = item.getItemId();
//
//        if (id == R.id.action_save) {
//            // do something here
//        }
        return super.onOptionsItemSelected(item);
    }

    class AsyncPush extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            sendPush_KING(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progDailog.dismiss();

        }
    }

    public void sendPush_KING(String title, String message) {
        TraderUtils utils = new TraderUtils();
//        String deviceId = Settings.System.getString(getContentResolver(),
//                Settings.System.ANDROID_ID);
//        Log.e(TAG, deviceId);
        {

            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int min = rightNow.get(Calendar.MINUTE);
            int nam = rightNow.get(Calendar.YEAR);
            int isecond = rightNow.get(Calendar.SECOND);
            int mlsec = rightNow.get(Calendar.MILLISECOND);
            int thang = rightNow.get(Calendar.MONTH) + 1;
            int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            String strTime = hour + ":" + min + ":" + isecond + " - " + ngay + "." + thang;
            try {
                title = title + " *** " + strTime;
                String pushMessage = "";
                pushMessage = "{\n"
                        + "   \"to\" : \"" + utils.DEVICE_TOKEN + "\",\n"
                        + "   \"data\" : {\n"
                        + "     \"title\" : \"" + title + "\",\n"
                        + "     \"message\" : \"" + message + "\"\n"
                        + "   }\n"
                        + " }";
                JSONObject json = new JSONObject(pushMessage);
                pushMessage = json.toString();
                System.out.println(pushMessage);
                // Create connection to send FCM Message request.
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "key=" + utils.SERVER_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Send FCM message content.
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(pushMessage.getBytes());

                Log.e(TAG, conn.getResponseCode() + "");
                Log.e(TAG, conn.getResponseMessage());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
