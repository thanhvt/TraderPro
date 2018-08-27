package com.traderpro.thanhvt;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class AsyncPush {


    public AsyncPush(){

    }

    public static class TraderPush extends AsyncTask<String, String, String> {

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

    public static void sendPush_KING(String title, String message) {
        String TAG = "AsyncPush";
//        String deviceId = Settings.System.getString(getContentResolver(),
//                Settings.System.ANDROID_ID);
//        Log.e(TAG, deviceId);
        TraderUtils utils = new TraderUtils();
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

