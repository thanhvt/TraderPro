package com.traderpro.GCM;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.traderpro.thanhvt.R;
import com.traderpro.thanhvt.TraderUtils;
import com.traderpro.thanhvt.UserDevice;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.content.Context.CLIPBOARD_SERVICE;

//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by Ravi on 31/03/15.
 */
public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;
    int count = 1;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;

    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        //intent = new Intent(mContext, BidAskActivity.class);
        //intent.putExtra("NotiClick",true);
        //PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        final Uri alarmSound = Uri.parse("android.resource://"
                + mContext.getPackageName() + "/" + R.raw.one1);
        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                } else {
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent);
            //SharedPreferences pref2 = mContext.getSharedPreferences(Config.SOUND, 0);
            //String strSound = pref2.getString("SOUND", "ON");
            //if (strSound.equals("ON")) {
            //playNotificationSound();
            //}

        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {

        try {
            int[] s = {R.raw.one1, R.raw.one2, R.raw.one3};
            int m = new Random().nextInt(3);
            Uri alarmSound = Uri.parse("android.resource://"
                    + mContext.getPackageName() + "/" + s[m]);

            final String chanelID = "com.traderpro.thanhvt";
            SharedPreferences pref2 = mContext.getSharedPreferences(Config.SOUND, 0);
            String strSound = pref2.getString("SOUND", "ON");
            SharedPreferences pref3 = mContext.getSharedPreferences(Config.VIBRATE, 0);
            String strVibrate = pref3.getString("VIBRATE", "ON");
            Log.e("SOUND:", strSound);
            Log.e("VIBRATE:", strVibrate);
            int VibrateIndex = -1;
            if (strVibrate.equals("ON")) {
                VibrateIndex = Notification.DEFAULT_VIBRATE;
            } else {
                VibrateIndex = Notification.DEFAULT_LIGHTS;
            }
            if (message.contains("VOL HT")) {
                // Parse message
                String strCoin = "";
                int levelRing = 0;

                String strExchange = "";
                String strGia = "";
                String strTime = "";
                String strVolHT = "";
                String strVolTB = "";
                String strCase = "";
                String strBuySell = "";
                String strTM = "";
                String strVol1H = "";
                String strGia1H = "";
                String strGiaOP = "";
                String strGia30P = "";
                String strID = System.currentTimeMillis() + "";
                try {

                    if (title.contains("***")) {
                        strCoin = title.substring(title.lastIndexOf("|") + 2, title.indexOf("***") - 1);
                        strCoin = strCoin.trim();

                        strTime = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();

                        strCase = title.contains("|") ? title.substring(title.indexOf("|") + 1, title.lastIndexOf("|")).trim() : "";
                        if (strCase.equals("12")) {
                            int[] x = {R.raw.bass_dj, R.raw.bass_i_love_u, R.raw.bass_message, R.raw.opinion, R.raw.wakewin};
                            int n = new Random().nextInt(5);
                            alarmSound = Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + x[n]);
                        }
                    }
                    strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
                    strGia = message.contains("PRI HT: ") ? message.substring(message.indexOf("PRI HT: ") + 12, message.indexOf("PRI HT: ") + 22) : "";
                    strVolHT = message.contains("VOL HT: ") ? message.substring(message.indexOf("VOL HT: ") + 12, message.indexOf("VOL HT: ") + 17) : "";
                    strVolTB = message.contains("VOL TB: ") ? message.substring(message.indexOf("VOL TB: ") + 12, message.indexOf("VOL TB: ") + 17) : "";
                    if (message.contains("Buyer/Seller")) {
                        strBuySell = message.substring(message.indexOf("Buyer/Seller") + 18, message.lastIndexOf("Number") - 8);
                    }
                    if (message.contains("Taker/Maker")) {
                        strTM = message.substring(message.indexOf("Taker/Maker") + 17, message.lastIndexOf("<br/>") - 6);
                    }
                    try {
                        strVol1H = message.contains("VOL 1H: ") ? message.substring(message.indexOf("VOL 1H: ") + 12, message.indexOf("VOL 1H: ") + 17) : "";
                        strGia1H = message.contains("PRI 1H: ") ? message.substring(message.indexOf("PRI 1H: ") + 12, message.indexOf("PRI 1H: ") + 22) : "";
                        strGiaOP = message.contains("PRI OP: ") ? message.substring(message.indexOf("PRI OP: ") + 12, message.indexOf("PRI OP: ") + 22) : "";
                        strGia30P = message.contains("PRI 30P: ") ? message.substring(message.indexOf("PRI 30P: ") + 13, message.indexOf("PRI 30P: ") + 23) : "";
                    } catch (Exception e) {

                    }

                    int idCoin = 1;
                    JSONArray arr = new JSONArray(readJSONFromAsset());
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String strSymbol = obj.getString("symbol");
                        if (strSymbol.equalsIgnoreCase(strCoin)) idCoin = obj.getInt("id");
                    }

                    String strSave = strExchange + " " + strCoin + " " + strGia.trim() + " " + strVolHT.trim() + " " + strVolTB.trim()
                            + " " + strTime.trim() + " " + strCase + " " + strBuySell + " " + strTM
                            + " " + strVol1H.trim() + " " + strGia1H + " " + strGiaOP + " " + strGia30P
                            + " " + idCoin + " " + strID;
                    strSave = strSave.replace("<br>", "");
                    strSave = strSave.replace("<br/>", "");
                    strSave = strSave.replace("<br", "");
                    strSave = strSave.replace("br>", "");
                    strSave = strSave.replace("break", "");
                    ghiFileLog(strSave);

                    Double dbVolAvg = Double.parseDouble(strVolTB);
                    Double dbVolNow = Double.parseDouble(strVolHT);

                    if (dbVolNow / dbVolAvg >= 5 && dbVolNow / dbVolAvg < 10) levelRing = 1;
                    else if (dbVolNow / dbVolAvg >= 10 && dbVolNow / dbVolAvg < 12) levelRing = 2;
                    else if (dbVolNow / dbVolAvg >= 12 && dbVolNow / dbVolAvg < 15) levelRing = 3;
                    else if (dbVolNow / dbVolAvg >= 15 && dbVolNow / dbVolAvg < 20) levelRing = 4;
                    else if (dbVolNow / dbVolAvg >= 20 && dbVolNow / dbVolAvg < 30) levelRing = 5;
                    else if (dbVolNow / dbVolAvg >= 30 && dbVolNow / dbVolAvg < 50) levelRing = 6;
                    else if (dbVolNow / dbVolAvg >= 50 && dbVolNow / dbVolAvg < 80) levelRing = 7;
                    else if (dbVolNow / dbVolAvg >= 80) levelRing = 8;
                } catch (Exception e) {
                    Log.e("parseMsg", e.getMessage());
                    e.printStackTrace();
                }

                AudioManager am =
                        (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                Ringtone r;
                Uri mSound;
                int id = 0;
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification();
                MediaPlayer mp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = notificationManager.getNotificationChannel(chanelID);
                    if (mChannel == null) {
                        mChannel = new NotificationChannel(chanelID, title, importance);
                        mChannel.enableVibration(true);
                        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        notificationManager.createNotificationChannel(mChannel);
                    }
                    mBuilder = new NotificationCompat.Builder(mContext, chanelID);
                }


                //
                if (levelRing > 1) {
                    switch (levelRing) {
                        case 1:
                            break;
                        case 2:
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.one1));
                            r.play();
                            break;
                        case 3:
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.one2));
                            r.play();
                            TimeUnit.SECONDS.sleep(1);
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.one2));
                            r.play();
                            TimeUnit.SECONDS.sleep(1);
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.one2));
                            r.play();
                            break;
                        case 4:

                            am.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            mSound = Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.bass_dj);
                            inboxStyle = new NotificationCompat.InboxStyle();

                            inboxStyle.addLine(message);

                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                    .setAutoCancel(true)
                                    .setContentTitle("Boss $$$ " + strCoin)
                                    .setContentText("PUMP PUMP PUMP PUMP")
                                    .setContentIntent(resultPendingIntent)
                                    .setSound(mSound)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(fromHtml(message)))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setDefaults(VibrateIndex)
                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                                    .build();


                            id = (int) System.currentTimeMillis();
                            notificationManager.notify(id, notification);

                            break;
                        case 5:
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.opinion));
                            r.play();
                            mSound = Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.bass_i_love_u);
                            am.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            am.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            am.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            inboxStyle = new NotificationCompat.InboxStyle();

                            inboxStyle.addLine(message);

                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                    .setAutoCancel(true)
                                    .setContentTitle("Boss $$$ " + strCoin)
                                    .setContentText("PUMP PUMP PUMP PUMP PUMP")
                                    .setContentIntent(resultPendingIntent)
                                    .setSound(mSound)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(fromHtml(message)))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setDefaults(VibrateIndex)
                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                                    .build();

                            id = (int) System.currentTimeMillis();
                            notificationManager.notify(id, notification);


                            break;
                        case 6:
                            am.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_RING,
                                    am.getStreamMaxVolume(AudioManager.STREAM_RING),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_SYSTEM,
                                    am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_NOTIFICATION,
                                    am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),
                                    0);
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.bass_i_love_u));
                            mSound = Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.wakewin);

                            mp = MediaPlayer.create(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.bass_dj));
                            count = 1;
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (count < 10) {
                                        mp.start();
                                        count++;
                                    }
                                }
                            });
                            mp.start();

                            inboxStyle = new NotificationCompat.InboxStyle();
                            inboxStyle.addLine(message);
                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                    .setAutoCancel(true)
                                    .setContentTitle("Boss $$$ " + strCoin)
                                    .setContentText("PUMP PUMP PUMP PUMP PUMP PUMP ")
                                    .setContentIntent(resultPendingIntent)
                                    .setSound(mSound)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(fromHtml(message)))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setDefaults(VibrateIndex)
                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                                    .build();

                            id = (int) System.currentTimeMillis();
                            notificationManager.notify(id, notification);
                            break;
                        case 7:
                            am.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_RING,
                                    am.getStreamMaxVolume(AudioManager.STREAM_RING),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_SYSTEM,
                                    am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_NOTIFICATION,
                                    am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),
                                    0);
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.bass_message));
                            mSound = Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.bass_i_love_u);

                            mp = MediaPlayer.create(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.bass_dj));
                            count = 1;
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (count < 20) {
                                        mp.start();
                                        count++;
                                    }
                                }
                            });
                            mp.start();

                            inboxStyle = new NotificationCompat.InboxStyle();
                            inboxStyle.addLine(message);
                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                    .setAutoCancel(true)
                                    .setContentTitle("Boss $$$ " + strCoin)
                                    .setContentText("PUMP PUMP PUMP PUMP PUMP PUMP PUMP")
                                    .setContentIntent(resultPendingIntent)
                                    .setSound(mSound)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(fromHtml(message)))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setDefaults(VibrateIndex)
                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                                    .build();

                            id = (int) System.currentTimeMillis();
                            notificationManager.notify(id, notification);
                            break;
                        case 8:
                            am.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_RING,
                                    am.getStreamMaxVolume(AudioManager.STREAM_RING),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_SYSTEM,
                                    am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                                    0);
                            am.setStreamVolume(
                                    AudioManager.STREAM_NOTIFICATION,
                                    am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),
                                    0);
                            r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.mewin));
                            mSound = Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.gowin);

                            mp = MediaPlayer.create(mContext, Uri.parse("android.resource://"
                                    + mContext.getPackageName() + "/" + R.raw.gowin3));
                            count = 1;
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (count < 100) {
                                        mp.start();
                                        count++;
                                    }
                                }
                            });
                            mp.start();

                            inboxStyle = new NotificationCompat.InboxStyle();
                            inboxStyle.addLine(message);
                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                    .setAutoCancel(true)
                                    .setContentTitle("Boss $$$ " + strCoin)
                                    .setContentText("PUMP ................")
                                    .setContentIntent(resultPendingIntent)
                                    .setSound(mSound)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(fromHtml(message)))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setDefaults(VibrateIndex)
                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                                    .build();

                            id = (int) System.currentTimeMillis();
                            notificationManager.notify(id, notification);
                            break;
                        default:
                            break;
                    }
                }

                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setAction("com.action.buy");
                intent.putExtra("BUYSELL", "SIMPLE");
                intent.putExtra("COIN", strCoin);
                intent.putExtra("Exchange", "Binance");
                intent.putExtra("PRICE", strGia);
                intent.putExtra("INTENT", title);
                intent.putExtra("MESSAGE", message);
                intent.putExtra("ID", strID);

                PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // End parse
                inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);
                notification = mBuilder.setSmallIcon(icon).setTicker(title)
                        .addAction(R.drawable.moneybag, "BUY", pIntent)
                        .setAutoCancel(true)
                        .setContentTitle("Boss $$$ " + strCoin)
                        .setContentText(title)
                        .setContentIntent(resultPendingIntent)
                        .setSound(strSound.equals("ON") && (strCase.equals("1") || strCase.equals("2") || strCase.equals("12"))
                                ? alarmSound : null)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(fromHtml(message)))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(VibrateIndex)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.moneybag))
                        .build();


                id = (int) System.currentTimeMillis();
                notificationManager.notify(id, notification);
            } else if (title.contains("warning")) {
                if (strSound.equals("ON")) {
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                    inboxStyle.addLine(message);

                    Notification notification;
                    notification = mBuilder.setSmallIcon(icon).setTicker(title)
                            .setAutoCancel(true)
                            .setContentTitle("SOS")
                            .setContentText(title)
                            .setContentIntent(resultPendingIntent)
                            .setSound(alarmSound)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(fromHtml(message)))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setDefaults(VibrateIndex)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .build();

                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    int id = (int) System.currentTimeMillis();
                    notificationManager.notify(id, notification);
                } else {
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                    inboxStyle.addLine(message);

                    Notification notification;
                    notification = mBuilder.setSmallIcon(icon).setTicker(title)
                            .setAutoCancel(true)
                            .setContentTitle("SOS")
                            .setContentText(title)
                            .setContentIntent(resultPendingIntent)
                            .setSound(null)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(fromHtml(message)))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setDefaults(VibrateIndex)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .build();

                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    int id = (int) System.currentTimeMillis();
                    notificationManager.notify(id, notification);
                }

            } else if (title.contains("REGISTER")) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(message);

                Notification notification;
                notification = mBuilder.setSmallIcon(icon).setTicker(title)
                        .setAutoCancel(true)
                        .setContentTitle("BOSS")
                        .setContentText(title)
                        .setContentIntent(resultPendingIntent)
                        .setSound(alarmSound)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(fromHtml(message)))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(VibrateIndex)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .build();

                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                int id = (int) System.currentTimeMillis();
                notificationManager.notify(id, notification);


                if (title.contains("NEW")) {
                    ArrayList<UserDevice> lstUser = readLstUser(mContext);
/*
                    String strMess = getManufacturer() + " | " + getModel() + " | " + "Android" + " | " + getSerialNumber()
                            + " | " + getUuid() + " | " + getOSVersion() + " | " + regId;
 */
                    String[] temp = message.split("\\|");
                    UserDevice u = new UserDevice();
                    u.NHASX = temp[0].trim();
                    u.TENTB = temp[1].trim();
                    u.OS = temp[2].trim();
                    u.SERIAL = temp[3].trim();
                    u.UUID = temp[4].trim();
                    u.VERSION = temp[5].trim();
                    u.DEVICE_TOKEN = temp[6].trim();
                    boolean exists = false;
                    for (UserDevice us : lstUser) {
                        if (us.UUID == u.UUID) exists = true;
                        break;
                    }
                    insertExcelFile(mContext, lstUser, u);
                    // Push backAnalytics
                    String strMess = "This is an automatic message sent from the system.\n" +
                            "Be ready to receive our signals which is analyzed and carefully selected by the experts and the best tools in the world.";
                    new AsyncPush().execute("Welcome PIS TRADER", strMess, u.DEVICE_TOKEN);

                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(u.DEVICE_TOKEN);

                } else {
                    ArrayList<UserDevice> lstUser = readLstUser(mContext);
                    String[] temp = message.split("\\|");
                    UserDevice u = new UserDevice();
                    u.NHASX = temp[0].trim();
                    u.TENTB = temp[1].trim();
                    u.OS = temp[2].trim();
                    u.SERIAL = temp[3].trim();
                    u.UUID = temp[4].trim();
                    u.VERSION = temp[5].trim();
                    u.DEVICE_TOKEN = temp[6].trim();
                    boolean exists = false;
                    for (UserDevice us : lstUser) {
                        if (us.UUID == u.UUID) exists = true;
                        break;
                    }
                    insertExcelFile(mContext, lstUser, u);
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(u.DEVICE_TOKEN);
                }
            } else if (title.contains("BUYYY")) {
                String strTime = "";
                String strCoin = "";
                String strGiaMua = "GIA_MUA";
                String strGiaBan = "GIA_BAN";
                String strTimeBan = "TIME_BAN";
                String strProfit = "PROFIT";
                String strExchange = "";
                String strID = "";
                if (title.contains("***")) {
                    strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
                    strCoin = title.substring(title.lastIndexOf("BNB") + 3, title.indexOf("BUYYY") - 1);
                    strCoin = strCoin.trim();

                    strTime = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();

                }
                strID = message.substring(message.indexOf("ID: ") + 4);
                strGiaMua = message.substring(message.indexOf("PRICE: ") + 7, message.indexOf("|") - 1).trim();
                String content = strTime + "|" + strCoin + "|" + strGiaMua + "|" + strGiaBan
                        + "|" + strTimeBan + "|" + strProfit + "|" + "Binance" + "|" + strID;
                ghiFileBot(content);
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setAction("com.action.buy");
                intent.putExtra("BUYSELL", "BUY");
                intent.putExtra("COIN", strCoin);
                intent.putExtra("Exchange", strExchange);
                intent.putExtra("PRICE", strGiaMua);
                intent.putExtra("INTENT", title);
                intent.putExtra("MESSAGE", message);
                intent.putExtra("ID", strID);

                PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                MediaPlayer mp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = notificationManager.getNotificationChannel(chanelID);
                    if (mChannel == null) {
                        mChannel = new NotificationChannel(chanelID, title, importance);
                        mChannel.enableVibration(true);
                        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        notificationManager.createNotificationChannel(mChannel);
                    }
                    mBuilder = new NotificationCompat.Builder(mContext, chanelID);
                }
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);

                Notification notification;
                if (strExchange.equals("Binance")) {
                    notification = mBuilder.setSmallIcon(icon).setTicker(title)
                            .addAction(R.drawable.buycoin, "BUY", pIntent) // #0
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentText(title)
                            .setSound(strSound.equals("ON") ? alarmSound : null)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(fromHtml(message)))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setDefaults(VibrateIndex)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.buycoin))
                            .build();
                } else {
                    notification = mBuilder.setSmallIcon(icon).setTicker(title)
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentText(title)
                            .setSound(strSound.equals("ON") ? alarmSound : null)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(fromHtml(message)))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setDefaults(VibrateIndex)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.buycoin))
                            .build();
                }


                int id = (int) System.currentTimeMillis();
                notificationManager.notify(id, notification);

            } else if (title.contains("StopLoss") || title.contains("TakeProfit")) {
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                MediaPlayer mp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = notificationManager.getNotificationChannel(chanelID);
                    if (mChannel == null) {
                        mChannel = new NotificationChannel(chanelID, title, importance);
                        mChannel.enableVibration(true);
                        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        notificationManager.createNotificationChannel(mChannel);
                    }
                    mBuilder = new NotificationCompat.Builder(mContext, chanelID);
                }
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(message);

                Notification notification;
                String strTime = "";
                String strCoin = "";
                String strGiaMua = "GIA_MUA";
                String strGiaBan = "GIA_BAN";
                String strTimeBan = "TIME_BAN";
                String strProfit = "PROFIT";
                String strID = "";
                Uri mSound;
                //Intent intent = new Intent(mContext, BidAskActivity.class);
                //intent.putExtra("NotiClick",true);
                //PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //Log.e("CLASS: ",mContext.getClass().toString());
                //BidAskActivity.finish();
                //intent.setFlags(Intent.Fl)

                //check sound
//                if (strSound.equals("ON")) {
                if (title.contains("StopLoss")) {
                    mSound = Uri.parse("android.resource://"
                            + mContext.getPackageName() + "/" + R.raw.pdown);
                    strCoin = title.substring(title.lastIndexOf("StopLoss") + 8, title.indexOf("***") - 1);
                    strCoin = strCoin.trim();
                    strGiaBan = message.substring(message.indexOf(":") + 6, message.indexOf(":") + 16);
                    strGiaBan = strGiaBan.trim();
                    strTimeBan = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
                    strProfit = "-" + message.substring(message.lastIndexOf("LOSS: ") + 6, message.lastIndexOf("%") + 1).trim();
                    strID = message.substring(message.indexOf("ID: ") + 8);
                    strGiaMua = message.substring(message.indexOf("Buy:") + 9, message.indexOf("Buy") + 19).trim();
                    String strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    intent.setAction("com.action.buy");
                    intent.putExtra("BUYSELL", "SELL");
                    intent.putExtra("COIN", strCoin);
                    intent.putExtra("Exchange", strExchange);
                    intent.putExtra("PRICE", strGiaBan);
                    intent.putExtra("INTENT", title);
                    intent.putExtra("MESSAGE", message);
                    intent.putExtra("ID", strID);

                    ///mContext.sendBroadcast(intent);
                    PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (strExchange.equals("Binance")) {
                        notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                .addAction(R.drawable.sellcoin, "SELL LOST", pIntent)
                                .setAutoCancel(true)
                                .setContentTitle("Bán dừng lỗ ngay")
                                .setContentText(title)
//                                .setContentIntent(pIntent)
                                .setSound(strSound.equals("ON") ? mSound : null)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(fromHtml(message)))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setDefaults(VibrateIndex)
                                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.losscoin))
                                .build();
                    } else {
                        notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                .setAutoCancel(true)
                                .setContentTitle("Bán dừng lỗ ngay")
                                .setContentText(title)
//                                .setContentIntent(pIntent)
                                .setSound(strSound.equals("ON") ? mSound : null)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(fromHtml(message)))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setDefaults(VibrateIndex)
                                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.losscoin))
                                .build();
                    }

                } else {
                    mSound = Uri.parse("android.resource://"
                            + mContext.getPackageName() + "/" + R.raw.pup);
                    strCoin = title.substring(title.lastIndexOf("TakeProfit") + 10, title.indexOf("***") - 1);
                    strCoin = strCoin.trim();
                    strGiaBan = message.substring(message.indexOf(":") + 6, message.indexOf(":") + 16);
                    strGiaBan = strGiaBan.trim();
                    strTimeBan = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
                    strProfit = "+" + message.substring(message.lastIndexOf("PROFIT: ") + 8, message.lastIndexOf("%") + 1).trim();
                    strID = message.substring(message.indexOf("ID: ") + 8);
                    strGiaMua = message.substring(message.indexOf("Buy:") + 9, message.indexOf("Buy") + 19).trim();
                    String strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    intent.setAction("com.action.buy");
                    intent.putExtra("BUYSELL", "SELL");
                    intent.putExtra("COIN", strCoin);
                    intent.putExtra("Exchange", strExchange);
                    intent.putExtra("PRICE", strGiaBan);
                    intent.putExtra("INTENT", title);
                    intent.putExtra("MESSAGE", message);
                    intent.putExtra("ID", strID);

                    ///mContext.sendBroadcast(intent);
                    PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (strExchange.equals("Binance")) {
                        notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                .addAction(R.drawable.sellcoin, "SELL WIN", pIntent)
                                .setAutoCancel(true)
                                .setContentTitle("Bán chốt lời ngay")
                                .setContentText(title)
//                                .setContentIntent(pIntent)
                                .setSound(strSound.equals("ON") ? mSound : null)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(fromHtml(message)))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setDefaults(VibrateIndex)
                                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sellcoin))
                                .build();
                    } else {
                        notification = mBuilder.setSmallIcon(icon).setTicker(title)
                                .setAutoCancel(true)
                                .setContentTitle("Bán chốt lời ngay")
                                .setContentText(title)
//                                .setContentIntent(pIntent)
                                .setSound(strSound.equals("ON") ? mSound : null)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(fromHtml(message)))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setDefaults(VibrateIndex)
                                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sellcoin))
                                .build();
                    }

                }

                int id = (int) System.currentTimeMillis();
                notificationManager.notify(id, notification);
//                }
//                else {
//                    if (title.contains("StopLoss")) {
//                        mSound = Uri.parse("android.resource://"
//                                + mContext.getPackageName() + "/" + R.raw.pdown);
//                        strCoin = title.substring(title.lastIndexOf("StopLoss") + 8, title.indexOf("***") - 1);
//                        strCoin = strCoin.trim();
//                        strGiaBan = message.substring(message.indexOf(":") + 6, message.indexOf(":") + 16);
//                        strGiaBan = strGiaBan.trim();
//                        strTimeBan = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
//                        strProfit = "-" + message.substring(message.lastIndexOf("LOSS: ") + 6, message.lastIndexOf("%") + 1).trim();
//                        strID = message.substring(message.indexOf("ID: ") + 8);
//                        strGiaMua = message.substring(message.indexOf("Buy:") + 9, message.indexOf("Buy") + 19).trim();
//                        String strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
//                        Intent intent = new Intent();
//                        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//                        intent.setAction("com.action.buy");
//                        intent.putExtra("BUYSELL", "SELL");
//                        intent.putExtra("COIN", strCoin);
//                        intent.putExtra("Exchange", strExchange);
//                        intent.putExtra("PRICE", strGiaMua);
//                        intent.putExtra("INTENT", title);
//                        intent.putExtra("MESSAGE", message);
//                        intent.putExtra("ID", strID);

//                        ///mContext.sendBroadcast(intent);
//                        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        if (strExchange.equals("Binance")) {
//                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
//                                    .addAction(R.mipmap.ic_launcher, "SELL LOST", pIntent)
//                                    .setAutoCancel(true)
//                                    .setContentTitle("Bán dừng lỗ ngay")
//                                    .setContentText(title)
//                                    .setContentIntent(pIntent)
//                                    .setSound(null)
//                                    .setStyle(new NotificationCompat.BigTextStyle()
//                                            .bigText(fromHtml(message)))
//                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                    .setDefaults(VibrateIndex)
//                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                                    .build();
//                        } else {
//                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
//                                    .setAutoCancel(true)
//                                    .setContentTitle("Bán dừng lỗ ngay")
//                                    .setContentText(title)
//                                    .setContentIntent(pIntent)
//                                    .setSound(null)
//                                    .setStyle(new NotificationCompat.BigTextStyle()
//                                            .bigText(fromHtml(message)))
//                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                    .setDefaults(VibrateIndex)
//                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                                    .build();
//                        }
//
//                    } else {
//                        mSound = Uri.parse("android.resource://"
//                                + mContext.getPackageName() + "/" + R.raw.pup);
//                        strCoin = title.substring(title.lastIndexOf("TakeProfit") + 10, title.indexOf("***") - 1);
//                        strCoin = strCoin.trim();
//                        strGiaBan = message.substring(message.indexOf(":") + 6, message.indexOf(":") + 16);
//                        strGiaBan = strGiaBan.trim();
//                        strTimeBan = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
//                        strProfit = "+" + message.substring(message.lastIndexOf("PROFIT: ") + 8, message.lastIndexOf("%") + 1).trim();
//                        strID = message.substring(message.indexOf("ID: ") + 8);
//                        strGiaMua = message.substring(message.indexOf("Buy:") + 9, message.indexOf("Buy") + 19).trim();
//                        String strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
//                        Intent intent = new Intent();
//                        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//                        intent.setAction("com.action.buy");
//                        intent.putExtra("BUYSELL", "SELL");
//                        intent.putExtra("COIN", strCoin);
//                        intent.putExtra("Exchange", strExchange);
//                        intent.putExtra("PRICE", strGiaMua);
//                        intent.putExtra("INTENT", title);
//                        intent.putExtra("MESSAGE", message);
//                        intent.putExtra("ID", strID);

//                        ///mContext.sendBroadcast(intent);
//                        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        if (strExchange.equals("Binance")) {
//                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
//                                    .addAction(R.mipmap.ic_launcher, "SELL WIN", pIntent)
//                                    .setAutoCancel(true)
//                                    .setContentTitle("Bán chốt lời ngay")
//                                    .setContentText(title)
//                                    .setContentIntent(pIntent)
//                                    .setSound(null)
//                                    .setStyle(new NotificationCompat.BigTextStyle()
//                                            .bigText(fromHtml(message)))
//                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                    .setDefaults(VibrateIndex)
//                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                                    .build();
//                        } else {
//                            notification = mBuilder.setSmallIcon(icon).setTicker(title)
//                                    .setAutoCancel(true)
//                                    .setContentTitle("Bán chốt lời ngay")
//                                    .setContentText(title)
//                                    .setContentIntent(pIntent)
//                                    .setSound(null)
//                                    .setStyle(new NotificationCompat.BigTextStyle()
//                                            .bigText(fromHtml(message)))
//                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                    .setDefaults(VibrateIndex)
//                                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                                    .build();
//                        }
//
//                    }
//
//                    int id = (int) System.currentTimeMillis();
//                    notificationManager.notify(id, notification);
//                }


                Calendar rightNow = Calendar.getInstance();
                int nam = rightNow.get(Calendar.YEAR);
                int thang = rightNow.get(Calendar.MONTH) + 1;
                int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                int min = rightNow.get(Calendar.MINUTE);

                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "TraderPro");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                List<String> lstObjectBuy = new ArrayList<>();
                File myFile = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_bot.txt");
                if (!myFile.exists()) {
                    myFile.createNewFile();
                }
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    lstObjectBuy.add(aDataRow);
                }
                myReader.close();

                for (int i = 0; i < lstObjectBuy.size(); i++) {
                    String itemBuy = lstObjectBuy.get(i);
                    if (!itemBuy.equalsIgnoreCase("")) {
                        String objs[] = itemBuy.split("\\|");
                        if (objs.length >= 8) {
                            if (objs[1].equals(strCoin) && objs[7].contains(strID)) {
                                itemBuy = itemBuy.replace(objs[3], strGiaBan);
                                itemBuy = itemBuy.replace(objs[4], strTimeBan);
                                itemBuy = itemBuy.replace(objs[5], strProfit);
                                lstObjectBuy.set(i, itemBuy);
                            }
                        }
                    }
                }
                FileOutputStream fOut = new FileOutputStream(myFile, false);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                for (int i = 0; i < lstObjectBuy.size(); i++) {
                    String itemBuy = lstObjectBuy.get(i);
                    if (!itemBuy.equals("")) {
                        myOutWriter.append("\n" + itemBuy);
                    }
                }
                myOutWriter.close();
                fOut.close();
            } else if (title.contains("CONFIG API")) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(message);

                Notification notification;
                notification = mBuilder.setSmallIcon(icon).setTicker(title)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(title)
                        .setContentIntent(resultPendingIntent)
                        .setSound(alarmSound)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(fromHtml(message)))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(VibrateIndex)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .build();

                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                int id = (int) System.currentTimeMillis();
                notificationManager.notify(id, notification);

                JSONObject jsonPush = new JSONObject();
                String[] spl = message.split("\\|");
                if (spl.length > 2) {
                    String serial = spl[0].split("\\+")[1];
                    String bitPub = spl[1].split("\\+")[1];
                    String bitPri = spl[2].split("\\+")[1];
                    String binPub = spl[3].split("\\+")[1];
                    String binPri = spl[4].split("\\+")[1];
                    String amount = spl[5].split("\\+")[1];
                    String useAPI = spl[6].split("\\+")[1];

                    jsonPush.put("SERIAL", serial);
                    jsonPush.put("BIT_PUB", bitPub);
                    jsonPush.put("BIT_PRI", bitPri);
                    jsonPush.put("BIN_PUB", binPub);
                    jsonPush.put("BIN_PRI", binPri);
                    jsonPush.put("AMOUNT_BTC", amount);
                    jsonPush.put("USE_API", useAPI);

                } else {
                    String serial = spl[0].split("\\+")[1];
                    String useAPI = spl[1].split("\\+")[1];
                    jsonPush.put("SERIAL", serial);
                    jsonPush.put("USE_API", useAPI);
                }
                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "TraderPro");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }

                File myFile = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "TraderPro" + File.separator + "CONFIG_API.txt");
                if (!myFile.exists()) {
                    myFile.createNewFile();
                }

                FileOutputStream fOut = new FileOutputStream(myFile, true);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("\n" + jsonPush.toString());
                myOutWriter.close();
                fOut.close();
            } else {
                if (strSound.equals("ON")) {
                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                    MediaPlayer mp;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel mChannel = notificationManager.getNotificationChannel(chanelID);
                        if (mChannel == null) {
                            mChannel = new NotificationChannel(chanelID, title, importance);
                            mChannel.enableVibration(true);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            notificationManager.createNotificationChannel(mChannel);
                        }
                        mBuilder = new NotificationCompat.Builder(mContext, chanelID);
                    }
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                    inboxStyle.addLine(message);

                    Notification notification;
                    notification = mBuilder.setSmallIcon(icon).setTicker(title)
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentText(title)
                            .setContentIntent(resultPendingIntent)
                            .setSound(alarmSound)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(fromHtml(message)))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setDefaults(VibrateIndex)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .build();

                    int id = (int) System.currentTimeMillis();
                    notificationManager.notify(id, notification);
                } else {
                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                    MediaPlayer mp;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel mChannel = notificationManager.getNotificationChannel(chanelID);
                        if (mChannel == null) {
                            mChannel = new NotificationChannel(chanelID, title, importance);
                            mChannel.enableVibration(true);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            notificationManager.createNotificationChannel(mChannel);
                        }
                        mBuilder = new NotificationCompat.Builder(mContext, chanelID);
                    }
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                    inboxStyle.addLine(message);

                    Notification notification;
                    notification = mBuilder.setSmallIcon(icon).setTicker(title)
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentText(title)
                            .setContentIntent(resultPendingIntent)
                            .setSound(null)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(fromHtml(message)))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setDefaults(VibrateIndex)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .build();

                    int id = (int) System.currentTimeMillis();
                    notificationManager.notify(id, notification);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        SharedPreferences pref2 = mContext.getSharedPreferences(Config.SOUND, 0);
        String strSound = pref2.getString("SOUND", "ON");
        SharedPreferences pref3 = mContext.getSharedPreferences(Config.VIBRATE, 0);
        String strVibrate = pref3.getString("VIBRATE", "ON");
        int VibrateIndex = -1;
        if (strVibrate.equals("ON")) {
            VibrateIndex = Notification.DEFAULT_VIBRATE;
        } else {
            VibrateIndex = Notification.DEFAULT_LIGHTS;
        }
        if (strSound.equals("ON")) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
            bigPictureStyle.bigPicture(bitmap);
            Notification notification;
            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(bigPictureStyle)
                    .setWhen(getTimeMilliSec(timeStamp))
                    .setDefaults(VibrateIndex)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
        } else {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
            bigPictureStyle.bigPicture(bitmap);
            Notification notification;
            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(null)
                    .setStyle(bigPictureStyle)
                    .setWhen(getTimeMilliSec(timeStamp))
                    .setDefaults(VibrateIndex)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
        }

    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            SharedPreferences pref2 = mContext.getSharedPreferences(Config.SOUND, 0);
            String strSound = pref2.getString("SOUND", "ON");

            Ringtone r = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://"
                    + mContext.getPackageName() + "/" + R.raw.one1));
            if (strSound.equals("ON")) {
                r.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean insertExcelFile(Context context, ArrayList<UserDevice> lst, UserDevice u) {
        boolean success = false;
        try {
            File file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderPro" + File.separator + "UserDevice.xls");

            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Row mRow = mySheet.createRow(1 + lst.size());
            Cell c = null;

            c = mRow.createCell(0);
            c.setCellValue(lst.size() + 1);

            c = mRow.createCell(1);
            c.setCellValue(u.NHASX);

            c = mRow.createCell(2);
            c.setCellValue(u.TENTB);

            c = mRow.createCell(3);
            c.setCellValue(u.OS);

            c = mRow.createCell(4);
            c.setCellValue(u.SERIAL);

            c = mRow.createCell(5);
            c.setCellValue(u.UUID);

            c = mRow.createCell(6);
            c.setCellValue(u.VERSION);

            c = mRow.createCell(7);
            c.setCellValue(u.DEVICE_TOKEN);

            c = mRow.createCell(8);
            c.setCellValue("0");

            c = mRow.createCell(9);
//            c.setCellValue(u.TENTB);

            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int min = rightNow.get(Calendar.MINUTE);
            int nam = rightNow.get(Calendar.YEAR);
            int isecond = rightNow.get(Calendar.SECOND);
            int mlsec = rightNow.get(Calendar.MILLISECOND);
            int thang = rightNow.get(Calendar.MONTH) + 1;
            int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            c = mRow.createCell(10);
            c.setCellValue(ngay + "/" + thang + "/" + nam);


//            Workbook wb = new HSSFWorkbook();
//
//            Cell c = null;
//
//            Sheet sheet1 = null;
//            sheet1 = wb.createSheet("myOrder");
//
//            Row row = sheet1.createRow(0);
//
//            c = row.createCell(0);
//            c.setCellValue("Item Number");
//
//            c = row.createCell(1);
//            c.setCellValue("Quantity");
//
//            c = row.createCell(2);
//            c.setCellValue("Price");
//
//            sheet1.setColumnWidth(0, (15 * 500));
//            sheet1.setColumnWidth(1, (15 * 500));
//            sheet1.setColumnWidth(2, (15 * 500));

            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
//                wb.write(os);
                myWorkBook.write(os);
                Log.w("FileUtils", "Writing file" + file);
                success = true;
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception ex) {
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }


        return success;
    }

    public ArrayList<UserDevice> readLstUser(Context context) {
        ArrayList<UserDevice> lstUserDevice = new ArrayList<UserDevice>();
        try {
            // Creating Input Stream

            File file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderPro" + File.separator + "UserDevice.xls");

            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();
            int rowIndex = 1;

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rowIndex == 1) {
                    rowIndex++;
                    continue;
                }
                Iterator cellIter = myRow.cellIterator();
                UserDevice userDevice = new UserDevice();

                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (myCell.getColumnIndex() == 1) {
                        userDevice.NHASX = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 2) {
                        userDevice.TENTB = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 3) {
                        userDevice.OS = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 4) {
                        userDevice.SERIAL = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 5) {
                        userDevice.UUID = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 6) {
                        userDevice.VERSION = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 7) {
                        userDevice.DEVICE_TOKEN = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 8) {
                        userDevice.ACTIVE = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 9) {
                        userDevice.TIME_ACTIVE = myCell.toString();
                    }
                    if (myCell.getColumnIndex() == 10) {
                        userDevice.CREATED_DATE = myCell.toString();
                    }
                }

                Log.e(TAG, "Cell Value: " + userDevice.NHASX + " " + userDevice.TENTB + " " + userDevice.OS
                        + " " + userDevice.SERIAL + " " + userDevice.UUID + " " + userDevice.DEVICE_TOKEN + " " + userDevice.CREATED_DATE);
                lstUserDevice.add(userDevice);
            }
        } catch (Exception e) {
            Log.e("NotificationUtils", e.getMessage());
            e.printStackTrace();
        }
        return lstUserDevice;
    }

    public void ghiFileLog(String strNoiDung) {
        // write on SD card file data in the text box
        try {
            Calendar rightNow = Calendar.getInstance();
            int nam = rightNow.get(Calendar.YEAR);
            int thang = rightNow.get(Calendar.MONTH) + 1;
            int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int min = rightNow.get(Calendar.MINUTE);

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderPro");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }

            File myFile = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_notification.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            // Doc trc
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            strNoiDung += "\n" + aBuffer;
            myReader.close();


            // Ghi sau
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(strNoiDung);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {

            Log.e("Err write log: ", e.getMessage());
        }
    }

    public void ghiFileBot(String strNoiDung) {
        // write on SD card file data in the text box
        try {
            Calendar rightNow = Calendar.getInstance();
            int nam = rightNow.get(Calendar.YEAR);
            int thang = rightNow.get(Calendar.MONTH) + 1;
            int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int min = rightNow.get(Calendar.MINUTE);

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderPro");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }

            File myFile = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_bot.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            FileOutputStream fOut = new FileOutputStream(myFile, true);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append("\n" + strNoiDung);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {

            Log.e("Err write log: ", e.getMessage());
        }
    }

    class AsyncPush extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            sendPush_KING(params[0], params[1], params[2]);
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

    public void sendPush_KING(String title, String message, String strDevice) {
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
//                title = title + " *** " + strTime;
                String pushMessage = "";
                pushMessage = "{\n"
                        + "   \"to\" : \"" + strDevice + "\",\n"
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

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("coindata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @SuppressWarnings("deprecation")
    public Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

}

