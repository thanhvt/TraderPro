package com.traderpro.thanhvt;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DetectSignalService extends Service {

    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000 * 6 * 10; // 60 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    List<String> lstCoin = new ArrayList<>();
    List<String> lstTop = new ArrayList<>();
    BittrexData data;

    public Activity mActivity = null;

    public DetectSignalService() {

        String strCoin[] = {
                "LTC", "DOGE", "VTC", "PPC", "RDD", "NXT", "DASH", "POT", "BLK", "EMC2", "AUR", "EFL", "GLD", "SLR",
                "PTC", "GRS", "NLG", "MONA", "THC", "VRC", "XMR", "KORE", "XDN", "NAV",
                "XST", "VIA", "PINK", "IOC", "CANN", "SYS", "NEOS", "DGB", "BURST", "SWIFT", "DOPE", "ABY",
                "BYC", "XMG", "BAY", "XRP", "NXS", "XCP", "GEO", "FLDC", "GRC", "NBT", "XEM", "OK",
                "AEON", "TX", "EXP", "XLM", "EMC", "SLS", "RADS", "DCR", "BSD", "XVG", "PIVX",
                "MEME", "STEEM", "2GIVE", "LSK", "WAVES", "LBC", "ETC", "STRAT", "UNB", "SYNX", "REP",
                "SHIFT", "ARDR", "XZC", "NEO", "ZEC", "ZCL", "IOP", "UBQ", "KMD", "ION", "LMC", "CRW", "ARK", "DYN",
                "TKS", "MUSIC", "GNT", "NXC", "LGD", "WINGS", "RLC", "LUN", "HMQ", "ANT", "SC", "BAT", "ZEN", "QRL",
                "CFI", "SNT", "XEL", "MCO", "ADT", "PAY", "STORJ", "ADX", "OMG", "CVC", "QTUM", "BCC", "DNT", "ADA",
                "MANA", "SALT", "TIX", "RCN", "VIB", "MER", "POWR", "BTG", "ENG", "UKG", "IGNIS", "SRN", "ZRX", "VEE",
                "BCPT", "TRX", "LRC", "UP", "DMT", "POLY", "PRO"
        };

        String strTop[] = {
                "LTC", "DOGE", "VTC", "PPC", "DASH",
                "XMR", "KORE", "NAV",
                "VIA", "IOC", "CANN", "SYS", "DGB", "BURST", "DOPE",
                "XMG", "BAY", "XRP", "XEM", "OK",
                "AEON", "XLM", "RADS", "DCR", "XVG", "PIVX",
                "STEEM", "LSK", "WAVES", "LBC", "ETC", "STRAT", "REP",
                "XZC", "NEO", "ZEC", "UBQ", "KMD", "ION", "LMC",
                "GNT", "NXC", "WINGS", "LUN", "SC", "BAT", "ZEN", "QRL",
                "SNT", "XEL", "PAY", "STORJ", "ADX", "OMG", "CVC", "QTUM", "BCC", "DNT", "ADA",
                "MANA", "SALT", "TIX", "RCN", "VIB", "POWR", "BTG", "ENG", "UKG", "VEE",
        };
        lstCoin = Arrays.asList(strCoin);
        lstTop = Arrays.asList(strTop);
        data = new BittrexData();
        BittrexAPI.setAPIKeys("1", "1");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service này là loại không giàng buộc (Un bounded)
        // Vì vậy method này ko bao giờ được gọi.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        if (mTimer != null) {
//            mTimer.cancel();
//        } else {
//            // recreate new
//            mTimer = new Timer();
//        }
//        // schedule task
//        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Start the Treasure Hunt - Lambo Lamboo and Lambooo", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
//                    Toast.makeText(getApplicationContext(), getDateTime(),
//                            Toast.LENGTH_SHORT).show();
//                    String strContent = "Giá hiện tại: "
//                            + "<br/>" + "Vol hiện tại: "
//                            + "<br/>" + "Vol 1H trước: " ;
//                    taoThongBao("Vãi đek, ăn không đại ca ", strContent);
//                    if(isConnectingToInternet())
//                        new ExchangeCoin().execute("");

                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

    }

    public boolean kiemTraPump() {
        boolean ok = true;
        return ok;
    }

    class ExchangeCoin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            long startTong = System.currentTimeMillis();

            String strNoiDungGhiFile = "";
            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int min = rightNow.get(Calendar.MINUTE);
            int nam = rightNow.get(Calendar.YEAR);
            int isecond = rightNow.get(Calendar.SECOND);
            int mlsec = rightNow.get(Calendar.MILLISECOND);
            int thang = rightNow.get(Calendar.MONTH) + 1;
            int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            strNoiDungGhiFile += "\n" + "---------- **************** ----------";
            strNoiDungGhiFile += "\n" + "---------- Bắt đầu lượt mới ----------";
            strNoiDungGhiFile += "\n" + "---------- ################ ----------";
            hour = rightNow.get(Calendar.HOUR_OF_DAY);
            min = rightNow.get(Calendar.MINUTE);
            isecond = rightNow.get(Calendar.SECOND);
            mlsec = rightNow.get(Calendar.MILLISECOND);
            nam = rightNow.get(Calendar.YEAR);
            thang = rightNow.get(Calendar.MONTH) + 1;
            ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            strNoiDungGhiFile += "\n" + "Time S: " + hour + ":" + min + ":" + isecond + "." + mlsec + " - " + ngay + ":" + thang + ":" + nam;


            for (int i = 0; i < lstCoin.size(); i++) {
                long start = System.currentTimeMillis();

                Log.e("Detect", "\n");
                Log.e("Detect", "Bat dau voi Coin " + (i + 1) + ": " + lstCoin.get(i));
                strNoiDungGhiFile += "\n\n" + "$$$$$$$$$$$$$$$$ Coin " + (i + 1) + " " + lstCoin.get(i) + " $$$$$$$$$$$$$$$$ ";
                rightNow = Calendar.getInstance();
                hour = rightNow.get(Calendar.HOUR_OF_DAY);
                min = rightNow.get(Calendar.MINUTE);
                isecond = rightNow.get(Calendar.SECOND);
                mlsec = rightNow.get(Calendar.MILLISECOND);
                nam = rightNow.get(Calendar.YEAR);
                thang = rightNow.get(Calendar.MONTH) + 1;
                ngay = rightNow.get(Calendar.DAY_OF_MONTH);
                strNoiDungGhiFile += "\n" + "Time: " + hour + ":" + min + ":" + isecond + "." + mlsec + " - " + ngay + ":" + thang + ":" + nam;


                int dieuKien1_VOLTB = 0;
                int dieuKien2_VOL1H = 0;
                int dieuKien3_VOL2H = 0;
                int dieuKien4_Gia5Phut = 0;
                int dieuKien5_Gia30Phut = 0;
                int dieuKien6_Gia1HTrc = 0;
                int dieuKien7_Gia4HTrc = 0;
                int dieuKien8_Gia12HTrc = 0;
                int dieuKien9_Gia1Day = 0;

                double GiaHienTai = 0;
                double Gia5Phut = 0;
                double Gia30Phut = 0;
                double Gia1HTrc = 0;
                double Gia4HTrc = 0;
                double Gia12HTrc = 0;
                double Gia1Day = 0;

                Double VOLHT = 0D;
                double Vol1HTrc = 0;
                double Vol2HTrc = 0;

                float tyLeGap = 0;

                data.set(BittrexAPI.layLichSuGia("BTC-" + lstCoin.get(i)));
                long elapsedTime = System.currentTimeMillis() - start;
                try {
                    elapsedTime = System.currentTimeMillis() - start;
                    if (elapsedTime > 1000) {

                    } else {
                        TimeUnit.MILLISECONDS.sleep(1050 - elapsedTime);
                    }
                } catch (InterruptedException e) {
                }
                Double sumBaseVol = 0d;
                for (int b = 0; b < data.size(); b++) {
                    sumBaseVol += Double.parseDouble(data.get(b, "BV"));
                }
                Double avgBaseVol = sumBaseVol / data.size();
                Log.e("Detect", "avgBaseVol " + lstCoin.get(i) + ": " + avgBaseVol);
                strNoiDungGhiFile += "\n" + "VOL TB: " + avgBaseVol;
                if (data.size() > 0) {
                    int b = data.size() - 1;
                    {
                        VOLHT = Double.parseDouble(data.get(b, "BV"));
                        GiaHienTai = Double.parseDouble(data.getObject()
                                .get(b)
                                .get(3));
                        Log.e("Detect", lstCoin.get(i) + " VOLHT " + String.format("%.10f", VOLHT));
                        strNoiDungGhiFile += "\n" + "VOL HT: " + String.format("%.10f", VOLHT);
                        rightNow = Calendar.getInstance();
                        hour = rightNow.get(Calendar.HOUR_OF_DAY);
                        min = rightNow.get(Calendar.MINUTE);

                        if (min <= 15) {
                            if (VOLHT > avgBaseVol / 3) {
                                tyLeGap = (float) (VOLHT / avgBaseVol);
                                dieuKien1_VOLTB = 1;
                            }
                        }
                        if (15 < min && min <= 30) {
                            if (VOLHT > avgBaseVol / 2) {
                                tyLeGap = (float) (VOLHT / avgBaseVol);
                                dieuKien1_VOLTB = 1;
                            }
                        }
                        if (30 < min && min <= 45) {
                            if (VOLHT > avgBaseVol) {
                                tyLeGap = (float) (VOLHT / avgBaseVol);
                                dieuKien1_VOLTB = 1;
                            }
                        }
                        if (45 < min) {
                            if (VOLHT > avgBaseVol * 1.5) {
                                tyLeGap = (float) (VOLHT / avgBaseVol);
                                dieuKien1_VOLTB = 1;
                            }
                        }
                    }
                }

                // Lay ra cac gia tri
                if (data.size() - 2 > 0) {
                    int i1 = data.size() - 2;
                    Vol1HTrc = Double.parseDouble(data.get(i1, "BV"));
                    Gia1HTrc = Double.parseDouble(data.getObject()
                            .get(i1)
                            .get(3));
                }
                if (data.size() - 3 > 0) {
                    int i2 = data.size() - 3;
                    Vol2HTrc = Double.parseDouble(data.get(i2, "BV"));

                }
                if (data.size() - 5 > 0) {
                    int i4 = data.size() - 5;
                    Gia4HTrc = Double.parseDouble(data.getObject()
                            .get(i4)
                            .get(3));
                }
                if (data.size() - 13 > 0) {
                    int i13 = data.size() - 13;
                    Gia12HTrc = Double.parseDouble(data.getObject()
                            .get(i13)
                            .get(3));
                }
                if (data.size() - 25 > 0) {
                    int i25 = data.size() - 25;
                    Gia1Day = Double.parseDouble(data.getObject()
                            .get(i25)
                            .get(3));
                }
                ///////////////////////////////////////////////////////////////
                Log.e("Detect", lstCoin.get(i) + " GiaHienTai " + String.format("%.10f", GiaHienTai));
                Log.e("Detect", lstCoin.get(i) + " Gia1HTrc " + String.format("%.10f", Gia1HTrc));
                Log.e("Detect", lstCoin.get(i) + " Gia4HTrc " + String.format("%.10f", Gia4HTrc));
                Log.e("Detect", lstCoin.get(i) + " Gia12HTrc " + String.format("%.10f", Gia12HTrc));
                Log.e("Detect", lstCoin.get(i) + " Gia1Day " + String.format("%.10f", Gia1Day));
                Log.e("Detect", lstCoin.get(i) + " Vol1HTrc " + String.format("%.10f", Vol1HTrc));
                Log.e("Detect", lstCoin.get(i) + " Vol2HTrc " + String.format("%.10f", Vol2HTrc));
                strNoiDungGhiFile += "\n" + "VOL 1H: " + String.format("%.10f", Vol1HTrc);
                strNoiDungGhiFile += "\n" + "VOL 2H: " + String.format("%.10f", Vol2HTrc);
                strNoiDungGhiFile += "\n" + "GIA HT: " + String.format("%.10f", GiaHienTai);
                strNoiDungGhiFile += "\n" + "GIA 1H: " + String.format("%.10f", Gia1HTrc);
                strNoiDungGhiFile += "\n" + "GIA 4H: " + String.format("%.10f", Gia4HTrc);
                strNoiDungGhiFile += "\n" + "GIA 12H: " + String.format("%.10f", Gia12HTrc);
                strNoiDungGhiFile += "\n" + "GIA 1D: " + String.format("%.10f", Gia1Day);

                if (dieuKien1_VOLTB == 0) {
                    Log.e("Detect", "Quit 1 vol ko du " + lstCoin.get(i));
                    strNoiDungGhiFile += "\n" + "QUIT I: VOL KHONG DU --------------->";
                    continue;
                }

                // Kinh thua cac the loai dieu kien
                rightNow = Calendar.getInstance();
                hour = rightNow.get(Calendar.HOUR_OF_DAY);
                min = rightNow.get(Calendar.MINUTE);
                if ((min <= 15 && VOLHT * 4 < Vol1HTrc && VOLHT * 4 < Vol2HTrc)
                        || (15 < min && min <= 30 && VOLHT * 2 < Vol1HTrc && VOLHT * 2 < Vol2HTrc)
                        || (30 < min && min <= 45 && VOLHT * 1.2 < Vol1HTrc && VOLHT * 1.2 < Vol2HTrc)
                        || (45 < min && VOLHT < Vol1HTrc && VOLHT < Vol2HTrc)
                        ) {
                    Log.e("Detect", "Quit 2 vol dang xuong " + lstCoin.get(i));
                    strNoiDungGhiFile += "\n" + "QUIT II: VOL DANG XUONG " + lstCoin.get(i) + " --------------->";
                    continue;
                }

                if (VOLHT > Vol1HTrc) {
                    dieuKien2_VOL1H = 1;
                }
                if (VOLHT > Vol2HTrc) {
                    dieuKien3_VOL2H = 1;
                }
                if (avgBaseVol <= Vol1HTrc && Vol1HTrc <= VOLHT) {
                    dieuKien2_VOL1H = 1;
                }
                if (avgBaseVol <= Vol2HTrc && Vol2HTrc <= VOLHT) {
                    dieuKien3_VOL2H = 1;
                }

                start = System.currentTimeMillis();
                data.set(BittrexAPI.getLatestTick("BTC-" + lstCoin.get(i), "fiveMin"));
                if (data.getObject().size() > 0) {
                    Gia5Phut = Double.parseDouble(data.getObject()
                            .get(0)
                            .get(3));
                }
                Log.e("Detect", lstCoin.get(i) + " Gia5Phut " + String.format("%.10f", Gia5Phut));
                strNoiDungGhiFile += "\n" + "GIA 5' " + String.format("%.10f", Gia5Phut);
                try {
                    elapsedTime = System.currentTimeMillis() - start;
                    if (elapsedTime > 1000) {

                    } else {
                        TimeUnit.MILLISECONDS.sleep(1050 - elapsedTime);
                    }
                } catch (InterruptedException e) {
                }

                start = System.currentTimeMillis();
                data.set(BittrexAPI.getLatestTick("BTC-" + lstCoin.get(i), "thirtyMin"));
                if (data.getObject().size() > 0) {
                    Gia30Phut = Double.parseDouble(data.getObject()
                            .get(0)
                            .get(3));
                }
                Log.e("Detect", lstCoin.get(i) + " Gia30Phut " + String.format("%.10f", Gia30Phut));
                strNoiDungGhiFile += "\n" + "GIA 30' " + String.format("%.10f", Gia30Phut);
                try {
                    elapsedTime = System.currentTimeMillis() - start;
                    if (elapsedTime > 1000) {

                    } else {
                        TimeUnit.MILLISECONDS.sleep(1050 - elapsedTime);
                    }
                } catch (InterruptedException e) {
                }

                if (GiaHienTai < Gia5Phut * 0.96 || GiaHienTai < Gia30Phut * 0.94 || GiaHienTai < Gia1HTrc * 0.92) {
                    String strContent = "Giá hiện tại: " + GiaHienTai
                            + "<br/>" + "Vol hiện tại: " + VOLHT
                            + "<br/>" + "Vol 1H trước: " + Vol1HTrc;
                    taoThongBao("Vãi đek, dump sâu " + lstCoin.get(i), strContent);
                }

                if (GiaHienTai < Gia1HTrc && GiaHienTai < Gia4HTrc & GiaHienTai < Gia12HTrc) {
                    Log.e("Detect", "Quit 3 gia dang xuong trong 12h " + lstCoin.get(i));
                    strNoiDungGhiFile += "\n" + "QUIT III: GIA DANG XUONG TRONG 12H --------------->";
                    // Nhung khoan da de xem gia no xuong bat ngo k, dump nhanh, > 8% 1H là giat minh roi. Chu y bat day nen xuong, hoac day nen len
                    continue;
                }

                // Ket thuc kinh thua cac the loai dieu kien
                // Bat cac the loai pump
                // 1. Chac cu nhat
                if (GiaHienTai > Gia5Phut && GiaHienTai > Gia30Phut && GiaHienTai > Gia1HTrc && GiaHienTai > Gia4HTrc
                        && GiaHienTai > Gia12HTrc && GiaHienTai > Gia1Day && dieuKien2_VOL1H == 1 && dieuKien3_VOL2H == 1) {
                    // Chac cham pump
                    Log.e("Detect", "OK OK OK");
                    String strContent = "Giá hiện tại: " + GiaHienTai
                            + "<br/>" + "Vol hiện tại: " + VOLHT
                            + "<br/>" + "Vol 1H trước: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 1 ------->";
                    taoThongBao("Vãi đek, ăn không đại ca " + lstCoin.get(i), strContent);
                } else if (GiaHienTai > Gia5Phut && GiaHienTai > Gia30Phut && dieuKien2_VOL1H == 1 && dieuKien3_VOL2H == 1) {
                    // Chac cham pump
                    Log.e("Detect", "OK OK OK");
                    String strContent = "Giá hiện tại: " + GiaHienTai
                            + "<br/>" + "Vol hiện tại: " + VOLHT
                            + "<br/>" + "Vol 1H trước: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 2 ------->";
                    taoThongBao("Vãi sếp, ăn k ??? " + lstCoin.get(i), strContent);
                } else if (VOLHT > avgBaseVol * 20 && GiaHienTai > Gia1HTrc * 1.2) {
                    Log.e("Detect", "OK OK OK");
                    String strContent = "Giá hiện tại: " + GiaHienTai
                            + "<br/>" + "Vol hiện tại: " + VOLHT
                            + "<br/>" + "Vol 1H trước: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 3 ------->";
                    taoThongBao("Vãi sếp, case 3 ăn k ??? " + lstCoin.get(i), strContent);
                } else if (Vol1HTrc > avgBaseVol * 2 && Vol2HTrc > avgBaseVol * 2 && GiaHienTai > Gia4HTrc) {
                    Log.e("Detect", "OK OK OK");
                    String strContent = "Giá hiện tại: " + GiaHienTai
                            + "<br/>" + "Vol hiện tại: " + VOLHT
                            + "<br/>" + "Vol 1H trước: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 4 ------->";
                    taoThongBao("Vãi sếp, case 4 ăn k ??? " + lstCoin.get(i), strContent);
                } else if (VOLHT > avgBaseVol && Vol1HTrc > avgBaseVol && Vol2HTrc > avgBaseVol && Vol1HTrc > Vol2HTrc
                        && GiaHienTai > Gia1HTrc && GiaHienTai > Gia4HTrc) {
                    Log.e("Detect", "OK OK OK");
                    String strContent = "Giá hiện tại: " + GiaHienTai
                            + "<br/>" + "Vol hiện tại: " + VOLHT
                            + "<br/>" + "Vol 1H trước: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 5 ------->";
                    taoThongBao("Vãi sếp, cas 5 ăn k ??? " + lstCoin.get(i), strContent);
                } else if (VOLHT > avgBaseVol && Vol1HTrc > avgBaseVol && Vol2HTrc > avgBaseVol && Vol1HTrc > Vol2HTrc
                        && lstTop.contains(lstCoin.get(i))) {
                    System.out.println("Detect " + "OK OK OK");
                    String strContent = "Giá HT: " + GiaHienTai
                            + "<br/>" + "Vol HT: " + VOLHT
                            + "<br/>" + "Vol 1H trc: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 7 ------->";
                    taoThongBao("Vãi coin top, case 7 ăn k ??? " + lstCoin.get(i), strContent);
                } else if (VOLHT > avgBaseVol * 2 && Vol1HTrc > avgBaseVol * 2 && Vol2HTrc > avgBaseVol * 2
                        && lstTop.contains(lstCoin.get(i))) {
                    System.out.println("Detect " + "OK OK OK");
                    String strContent = "Giá HT: " + GiaHienTai
                            + "<br/>" + "Vol HT: " + VOLHT
                            + "<br/>" + "Vol 1H trc: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 9 ------->";
                    taoThongBao("Vol x2, case 9 ăn k ??? " + lstCoin.get(i), strContent);
                } else if (VOLHT > avgBaseVol && Vol1HTrc > avgBaseVol && Vol2HTrc > avgBaseVol && Vol1HTrc > Vol2HTrc
                        && VOLHT > 50) {
                    System.out.println("Detect " + "OK OK OK");
                    String strContent = "Giá HT: " + GiaHienTai
                            + "<br/>" + "Vol HT: " + VOLHT
                            + "<br/>" + "Vol 1H trc: " + Vol1HTrc;
                    strNoiDungGhiFile += "\n" + "---------- " + lstCoin.get(i) + " OK OK OK 8 ------->";
                    taoThongBao("Vol vãi lớn, case 8 ăn k ??? " + lstCoin.get(i), strContent);
                }
//                else if ((VOLHT * 20 < Vol1HTrc && VOLHT * 20 > avgBaseVol)
//                        || (VOLHT > Vol1HTrc * 20 && Vol1HTrc * 20 > avgBaseVol)
//                        || (Vol1HTrc * 20 < Vol2HTrc && Vol1HTrc * 20 > avgBaseVol)
//                        || (Vol1HTrc > Vol2HTrc * 20 && Vol2HTrc * 20 > avgBaseVol)) {
//                    Log.e("Detect", "OK OK OK");
//                    String strContent = "Giá hiện tại: " + GiaHienTai
//                            + "<br/>" + "Vol hiện tại: " + VOLHT
//                            + "<br/>" + "Vol 1H trước: " + Vol1HTrc;
//                    strNoiDungGhiFile += "\n" + "---------- OK OK OK 6 ------->";
//                    taoThongBao("Điên mia rồi, case 6  ??? " + lstCoin.get(i), strContent);
//                }
                else {
                    Log.e("Detect", "Quit FINAL " + lstCoin.get(i));
                    strNoiDungGhiFile += "\n" + "-------------- QUIT FINAL ------->";
                }
                elapsedTime = System.currentTimeMillis() - startTong;
                Log.e("Detect", "Thoi gian thuc hien: " + elapsedTime / 1000F);
            }
            rightNow = Calendar.getInstance();
            hour = rightNow.get(Calendar.HOUR_OF_DAY);
            min = rightNow.get(Calendar.MINUTE);
            isecond = rightNow.get(Calendar.SECOND);
            mlsec = rightNow.get(Calendar.MILLISECOND);
            nam = rightNow.get(Calendar.YEAR);
            thang = rightNow.get(Calendar.MONTH) + 1;
            ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            strNoiDungGhiFile += "\n" + "Time E: " + hour + ":" + min + ":" + isecond + "." + mlsec + " - " + ngay + ":" + thang + ":" + nam;
            ghiFile(strNoiDungGhiFile);
            long elapsedTimeTong = System.currentTimeMillis() - startTong;
            Log.e("Detect", "Thoi gian thuc hien tong cong: " + elapsedTimeTong / 1000F);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
//            for (int b = 0; b < data.size(); b++) {
//                if (data.get(b, "MarketName").startsWith("BTC")) {
//                    lstCoinB.add(data.get(b, "MarketName"));
//                    Log.e("Detect", data.get(b, "MarketName"));
//                }
//            }
        }

    }

    // Checking for all possible internet providers
    public boolean isConnectingToInternet() {

        ConnectivityManager connectivity =
                (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void taoThongBao(String strTieuDe, String strContent) {
        String GROUP_KEY_DETECT_SIGNAL = "com.traderpro.thanhvt.DETECT_SIGNAL";
        Intent intent = new Intent(this, BidAskActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
//        Notification n = new Notification.Builder(this)
//                .setContentTitle("New mail from " + "test@gmail.com")
//                .setContentText("Subject")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pIntent)
//                .setAutoCancel(true)
//                .setGroup(GROUP_KEY_DETECT_SIGNAL)
//                .addAction(R.mipmap.ic_launcher, "Call", pIntent)
//                .addAction(R.mipmap.ic_launcher, "More", pIntent)
//                .addAction(R.mipmap.ic_launcher, "And more", pIntent).build();

//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        String filename = "mario_coin.mp3";
//        String path = "/mnt/sdcard/" + filename;
//        File f = new File(path);  //
//        Uri imageUri = Uri.fromFile(f);
        try {
//            RingtoneManager.get
//            RingtoneManager.setActualDefaultRingtoneUri(
//                    this, RingtoneManager.TYPE_RINGTONE,
//                    imageUri);
        } catch (Throwable t) {

        }
        long[] vibrate = {0, 100, 200, 300};

        Notification n = new NotificationCompat.Builder(this, GROUP_KEY_DETECT_SIGNAL)
                .setContentTitle("Sếp, khả năng con này pump !!!")
                .setContentText(strTieuDe)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY_DETECT_SIGNAL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(strContent, Html.FROM_HTML_MODE_COMPACT)))
                .setSound(Uri.parse("android.resource://"
                        + getPackageName() + "/" + R.raw.coco_cola))
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setVibrate(vibrate)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(R.mipmap.ic_launcher, "Mua 100k", pIntent)
                .addAction(R.mipmap.ic_launcher, "Làm 1tr", pIntent)
                .addAction(R.mipmap.ic_launcher, "Quất 3tr", pIntent).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, n);
    }

    public void ghiFile(String strNoiDung) {
        // write on SD card file data in the text box
        try {
            Calendar rightNow = Calendar.getInstance();
            int nam = rightNow.get(Calendar.YEAR);
            int thang = rightNow.get(Calendar.MONTH) + 1;
            int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int min = rightNow.get(Calendar.MINUTE);

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderBittrex");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }

            File myFile = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "TraderBittrex" + File.separator + nam + thang + ngay + "_log.txt");
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

    // Hủy bỏ dịch vụ.
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
