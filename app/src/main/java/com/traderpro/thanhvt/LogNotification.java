package com.traderpro.thanhvt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LogNotification extends Fragment {

    // constant
    String TAG = "LogNotification";
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    ListView listView;
    CustomNotificationAdapter customAdapter;
    ArrayList<NotificationEntity> lstNotiEntity;
    ArrayList<NotificationEntity> lstNotiBinance;
    ArrayList<NotificationEntity> lstNotiBittrex;
    CheckBox cbBinance, cbBittrex;
    Button btnTrc, btnSau;
    EditText edTime;
    Calendar c;
    SimpleDateFormat df;
    String formattedDate;
    public BittrexData bittrexData = new BittrexData();
    ProgressDialog progDailog;

    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    private TelephonyManager mTelephonyManager;

    String strDevice = "";
    Executor threadPoolExecutor;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity().checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            getDeviceImei();
        }

        View rootView = inflater.inflate(R.layout.layoutnoti, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvTradeHistory);

        cbBinance = (CheckBox) rootView.findViewById(R.id.cbBinance);
        cbBittrex = (CheckBox) rootView.findViewById(R.id.cbBittrex);

        btnTrc = (Button) rootView.findViewById(R.id.btnTrc);
        btnSau = (Button) rootView.findViewById(R.id.btnSau);
        edTime = (EditText) rootView.findViewById(R.id.edTime);

        c = Calendar.getInstance();

        System.out.println("Current time => " + c.getTime());

        df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        edTime.setText(formattedDate);

        bittrexData = new BittrexData();

        lstNotiEntity = new ArrayList<>();
        lstNotiBinance = new ArrayList<>();
        lstNotiBittrex = new ArrayList<>();

        int corePoolSize = 80;
        int maximumPoolSize = 100;
        int keepAliveTime = 10;

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, sPoolWorkQueue);

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
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aDataRow = aDataRow.replace("| ", "");
                NotificationEntity e = new NotificationEntity();
                e = parseFile(aDataRow, nam, thang, ngay);
                lstNotiEntity.add(e);
                if (e.strExchange.equals("Bittrex")) lstNotiBittrex.add(e);
                if (e.strExchange.equals("Binance")) lstNotiBinance.add(e);

                cbBinance.setText("Binance (" + lstNotiBinance.size() + ")");
                cbBittrex.setText("Bittrex (" + lstNotiBittrex.size() + ")");
            }
            myReader.close();


            customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notificustom, lstNotiEntity, bittrexData, nam, thang, ngay);
            customAdapter.notifyDataSetChanged();
            listView.setAdapter(customAdapter);
        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
            e.printStackTrace();
        }

        cbBittrex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lstNotiEntity.addAll(lstNotiBittrex);
                } else {
                    lstNotiEntity.removeAll(lstNotiBittrex);
                }
                if (customAdapter != null) {
                    customAdapter.notifyDataSetChanged();
                }

            }
        });

        cbBinance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lstNotiEntity.addAll(lstNotiBinance);
                } else {
                    lstNotiEntity.removeAll(lstNotiBinance);
                }

                if (customAdapter != null) {
                    customAdapter.notifyDataSetChanged();
                }
            }
        });

        btnTrc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cbBinance.setText("Binance (" + 0 + ")");
                cbBittrex.setText("Bittrex (" + 0 + ")");
                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());

                Log.v("PREVIOUS DATE : ", formattedDate);
                edTime.setText(formattedDate);

                getFile(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
            }
        });

        btnSau.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cbBinance.setText("Binance (" + 0 + ")");
                cbBittrex.setText("Bittrex (" + 0 + ")");
                c.add(Calendar.DATE, 1);
                formattedDate = df.format(c.getTime());

                Log.v("NEXT DATE : ", formattedDate);
                edTime.setText(formattedDate);

                getFile(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
            }
        });

//        Calendar c = Calendar.getInstance();
//        c.set(2018, 6, 10, 10, 25, 25);
//        getGiaMaxBinance("NEO", c.getTimeInMillis() + "");
        return rootView;
    }

    public void getFile(int nam, int thang, int ngay) {
        try {
            lstNotiEntity = new ArrayList<>();
            lstNotiBinance = new ArrayList<>();
            lstNotiBittrex = new ArrayList<>();
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
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aDataRow = aDataRow.replace("| ", "");
                NotificationEntity e = new NotificationEntity();
                e = parseFile(aDataRow, nam, thang, ngay);
                if (e != null) {
                    lstNotiEntity.add(e);
                    if (e.strExchange.equals("Bittrex")) lstNotiBittrex.add(e);
                    if (e.strExchange.equals("Binance")) lstNotiBinance.add(e);
                }

                cbBinance.setText("Binance (" + lstNotiBinance.size() + ")");
                cbBittrex.setText("Bittrex (" + lstNotiBittrex.size() + ")");
            }
            myReader.close();
            customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notificustom, lstNotiEntity, bittrexData, nam, thang, ngay);
            customAdapter.notifyDataSetChanged();
            listView.setAdapter(customAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getFile ex " + e.getMessage());
        }
    }

    public NotificationEntity parseFile(String strIn, int nam, int thang, int ngay) {
        try {
            NotificationEntity e = new NotificationEntity();
            String tmp[] = strIn.split(" ");
            e.strExchange = tmp[0].trim();
            e.strCoin = tmp[1].trim();
            e.strGia = tmp[2].trim();
            e.strVol = tmp[3].trim();
            e.strVolTB = tmp[4].trim();
            e.strTime = tmp[5].trim();
            e.strCase = tmp.length > 6 ? tmp[6].trim() : "0";
            e.strBuySell = tmp.length > 7 ? tmp[7].trim() : "";
            e.strTakerMaker = tmp.length > 8 ? tmp[8].trim() : "";
            e.strGiaMax = 0D;
//            Log.e("start Get max price", "1");
            String[] mTime = e.strTime.split(":");
            Calendar c = Calendar.getInstance();
            c.set(nam, thang - 1, ngay, Integer.parseInt(mTime[0]), Integer.parseInt(mTime[1]));
//            new ExchangeGetPrice(e).execute(e.strCoin, c.getTimeInMillis() + "");
//            new ExchangeGetPrice(e).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, e.strCoin, c.getTimeInMillis() + "");
            new ExchangeGetPrice(e).executeOnExecutor(threadPoolExecutor, e.strCoin, c.getTimeInMillis() + "");
            return e;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "parseFile ex " + e.getMessage());
            return null;
        }

    }

    class ExchangeGetPrice extends AsyncTask<String, String, String> {

        NotificationEntity mEn;

        public ExchangeGetPrice(NotificationEntity mEn) {
            this.mEn = mEn;
        }

        @Override
        protected String doInBackground(String... params) {
            if (mEn.strExchange.equals("Binance")) {
                mEn.strGiaMax = getGiaMaxBinance(params[0], params[1]);
                mEn.strGiaMin = getGiaMinBinance(params[0], params[1]);
            } else if (mEn.strExchange.equals("Bittrex")) {
//                mEn.strGiaMax = getGiaMaxBittrex(params[0], params[1]);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progDailog = new ProgressDialog(getActivity());
            //progDailog.setMessage("Loading...");
            //progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //progDailog.setCancelable(true);
            //progDailog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progDailog.dismiss();
            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notification, lstNotiEntity, bittrexData);
                        if (customAdapter != null) {
                            customAdapter.notifyDataSetChanged();
                        }
//                        listView.setAdapter(customAdapter);

                    }
                });
        }
    }

    public Double subGetGiaMax(JSONArray arr) {
        try {
            Double giaMax = 0D;
            for (int i = 0; i < arr.length(); i++) {
                JSONArray arrItem = arr.getJSONArray(i);
                Double giaTime = Double.parseDouble(arrItem.getString(2));
                if (giaTime > giaMax) {
                    giaMax = giaTime;
                }
            }
            return giaMax;
        } catch (Exception e) {

        }
        return 0D;
    }

    public Double subGetGiaMin(JSONArray arr) {
        try {
            Double giaMin = 100D;
            for (int i = 0; i < arr.length(); i++) {
                JSONArray arrItem = arr.getJSONArray(i);
                Double giaTime = Double.parseDouble(arrItem.getString(2));
                if (giaTime < giaMin) {
                    giaMin = giaTime;
                }
            }
            return giaMin;
        } catch (Exception e) {

        }
        return 0D;
    }

    public Double getGiaMaxBittrex(String strCoin, String strTime) {
        Double giaMax = 0D;
        try {
            long timeNow = System.currentTimeMillis() - 7 * 60 * 60 * 1000;
            long startTime = Long.parseLong(strTime) - 7 * 60 * 60 * 1000;
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Log.e(TAG, "111");
            if (timeNow - startTime < 12 * 60 * 60 * 1000) {
                bittrexData.set(BittrexAPI.getMarketHistory("BTC-" + strCoin, "fivemin"));
                Log.e(TAG, "222");
                for (int b = 0; b < bittrexData.size(); b++) {
                    String strTimeItem = bittrexData.getObject().get(b).get(5);
                    Date d = input.parse(strTimeItem);
                    if (d.getTime() > startTime) {
                        Log.e(TAG, "333");
                        Double giaTime = Double.parseDouble(bittrexData.getObject().get(b).get(1));
                        if (giaTime > giaMax) {
                            giaMax = giaTime;
                        }
                    }
                }
                Log.e(TAG, "444");
            } else {
                Log.e(TAG, "555");
                bittrexData.set(BittrexAPI.getMarketHistory("BTC-" + strCoin, "hour"));
                Log.e(TAG, "666");
                for (int b = 0; b < bittrexData.size(); b++) {
                    String strTimeItem = bittrexData.getObject().get(b).get(5);
                    Date d = input.parse(strTimeItem);
                    if (d.getTime() > startTime) {
                        Double giaTime = Double.parseDouble(bittrexData.getObject().get(b).get(1));
                        if (giaTime > giaMax) {
                            giaMax = giaTime;
                        }
                    }
                }
                Log.e(TAG, "777");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.e("Gia MAX Bittrex", strCoin + ": " + String.format("%.8f", giaMax));
        return giaMax;
    }

    public Double getGiaMaxBinance(String strCoin, String strTime) {
        Double giaMax = 0D;
        Double giaMin = 0D;
        try {
            long timeNow = System.currentTimeMillis();
            long startTime = Long.parseLong(strTime);
            // Neu gio hien tai moi hon gio bat dau mua chua toi 60', thi query theo 3' de lay ra gia tri max
            if (timeNow - startTime < 60 * 60 * 1000) {
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                giaMax = subGetGiaMax(jsonArr);
                giaMin = subGetGiaMin(jsonArr);
            } else if (timeNow - startTime < 24 * 60 * 60 * 1000) {
                // 1H dau theo 3m
                // 23H theo 1H
                // 1H cuoi theo 3m
                Calendar rightNow = Calendar.getInstance();
                int min = rightNow.get(Calendar.MINUTE);
                rightNow.add(Calendar.MINUTE, -min);
//                rightNow.add(Calendar.HOUR_OF_DAY, -7);
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + rightNow.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                Double giaMax1 = subGetGiaMax(jsonArr);
                Double giaMin1 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + strTime + "&endTime=" + rightNow.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax2 = subGetGiaMax(jsonArr);
                Double giaMin2 = subGetGiaMin(jsonArr);
                // Cuoi gio
                Calendar endHour = Calendar.getInstance();
                endHour.setTimeInMillis(startTime);
                min = endHour.get(Calendar.MINUTE);
                endHour.add(Calendar.MINUTE, 60 - min);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime + "&endTime=" + endHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax3 = subGetGiaMax(jsonArr);
                Double giaMin3 = subGetGiaMin(jsonArr);
//                giaMax = giaMax1 > giaMax2 ? giaMax1 : giaMax2;
                giaMax = Math.max(Math.max(giaMax1, giaMax2), giaMax3);
                giaMin = Math.min(Math.min(giaMin1, giaMin2), giaMin3);
            } else {
                // Cac ngay theo 1D
                // 1D cuoi theo 1H
                // 1H cuoi theo 3m

                // Dau gio
                Calendar startHour = Calendar.getInstance();
                int hour = startHour.get(Calendar.HOUR_OF_DAY);
                int min = startHour.get(Calendar.MINUTE);
                int nam = startHour.get(Calendar.YEAR);
                int isecond = startHour.get(Calendar.SECOND);
                int mlsec = startHour.get(Calendar.MILLISECOND);
                int thang = startHour.get(Calendar.MONTH) + 1;
                int ngay = startHour.get(Calendar.DAY_OF_MONTH);
                startHour.add(Calendar.MINUTE, -min);
//                startHour.add(Calendar.HOUR_OF_DAY, -7);

                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startHour.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                Double giaMax1 = subGetGiaMax(jsonArr);
                Double giaMin1 = subGetGiaMin(jsonArr);

                // Dau ngay
                Calendar startDay = Calendar.getInstance();
                hour = startDay.get(Calendar.HOUR_OF_DAY);
                min = startDay.get(Calendar.MINUTE);
                nam = startDay.get(Calendar.YEAR);
                isecond = startDay.get(Calendar.SECOND);
                mlsec = startDay.get(Calendar.MILLISECOND);
                thang = startDay.get(Calendar.MONTH) + 1;
                ngay = startDay.get(Calendar.DAY_OF_MONTH);
                startDay.add(Calendar.MINUTE, -min);
                startDay.add(Calendar.HOUR_OF_DAY, -hour);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + startDay.getTimeInMillis() + "&endTime=" + startHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax2 = subGetGiaMax(jsonArr);
                Double giaMin2 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1d&startTime=" + startTime + "&endTime=" + startDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax3 = subGetGiaMax(jsonArr);
                Double giaMin3 = subGetGiaMin(jsonArr);

                // Cuoi ngay
                Calendar endDay = Calendar.getInstance();
                endDay.setTimeInMillis(startTime);
                hour = endDay.get(Calendar.HOUR_OF_DAY);
                endDay.add(Calendar.HOUR_OF_DAY, 24 - hour);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startTime + "&endTime=" + endDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax4 = subGetGiaMax(jsonArr);
                Double giaMin4 = subGetGiaMin(jsonArr);

                giaMax = Math.max(Math.max(Math.max(giaMax1, giaMax2), giaMax3), giaMax4);
                giaMin = Math.min(Math.min(Math.min(giaMin1, giaMin2), giaMin3), giaMin4);
//                giaMax = Math.max(Math.max(giaMax1, giaMax2), giaMax3);
//                giaMax = giaMax1 > giaMax2 ? (giaMax1 > giaMax3 ? giaMax1 : giaMax3) : giaMax2;
//                int max =  Math.max(Math.max(x,y),z);
//                           Math.min(Math.min(a, b), c);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
//        Log.e("Gia MAX Binance", strCoin + ": " + String.format("%.8f", giaMax));
        return giaMax;
    }

    public Double getGiaMinBinance(String strCoin, String strTime) {
        Double giaMax = 0D;
        Double giaMin = 0D;
        try {
            long timeNow = System.currentTimeMillis();
            long startTime = Long.parseLong(strTime);
            // Neu gio hien tai moi hon gio bat dau mua chua toi 60', thi query theo 3' de lay ra gia tri max
            if (timeNow - startTime < 60 * 60 * 1000) {
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                giaMax = subGetGiaMax(jsonArr);
                giaMin = subGetGiaMin(jsonArr);
            } else if (timeNow - startTime < 24 * 60 * 60 * 1000) {
                // 1H dau theo 3m
                // 23H theo 1H
                // 1H cuoi theo 3m
                Calendar rightNow = Calendar.getInstance();
                int min = rightNow.get(Calendar.MINUTE);
                rightNow.add(Calendar.MINUTE, -min);
//                rightNow.add(Calendar.HOUR_OF_DAY, -7);
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + rightNow.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                Double giaMax1 = subGetGiaMax(jsonArr);
                Double giaMin1 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + strTime + "&endTime=" + rightNow.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax2 = subGetGiaMax(jsonArr);
                Double giaMin2 = subGetGiaMin(jsonArr);
                // Cuoi gio
                Calendar endHour = Calendar.getInstance();
                endHour.setTimeInMillis(startTime);
                min = endHour.get(Calendar.MINUTE);
                endHour.add(Calendar.MINUTE, 60 - min);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime + "&endTime=" + endHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax3 = subGetGiaMax(jsonArr);
                Double giaMin3 = subGetGiaMin(jsonArr);
//                giaMax = giaMax1 > giaMax2 ? giaMax1 : giaMax2;
                giaMax = Math.max(Math.max(giaMax1, giaMax2), giaMax3);
                giaMin = Math.min(Math.min(giaMin1, giaMin2), giaMin3);
            } else {
                // Cac ngay theo 1D
                // 1D cuoi theo 1H
                // 1H cuoi theo 3m

                // Dau gio
                Calendar startHour = Calendar.getInstance();
                int hour = startHour.get(Calendar.HOUR_OF_DAY);
                int min = startHour.get(Calendar.MINUTE);
                int nam = startHour.get(Calendar.YEAR);
                int isecond = startHour.get(Calendar.SECOND);
                int mlsec = startHour.get(Calendar.MILLISECOND);
                int thang = startHour.get(Calendar.MONTH) + 1;
                int ngay = startHour.get(Calendar.DAY_OF_MONTH);
                startHour.add(Calendar.MINUTE, -min);
//                startHour.add(Calendar.HOUR_OF_DAY, -7);

                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startHour.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                Double giaMax1 = subGetGiaMax(jsonArr);
                Double giaMin1 = subGetGiaMin(jsonArr);

                // Dau ngay
                Calendar startDay = Calendar.getInstance();
                hour = startDay.get(Calendar.HOUR_OF_DAY);
                min = startDay.get(Calendar.MINUTE);
                nam = startDay.get(Calendar.YEAR);
                isecond = startDay.get(Calendar.SECOND);
                mlsec = startDay.get(Calendar.MILLISECOND);
                thang = startDay.get(Calendar.MONTH) + 1;
                ngay = startDay.get(Calendar.DAY_OF_MONTH);
                startDay.add(Calendar.MINUTE, -min);
                startDay.add(Calendar.HOUR_OF_DAY, -hour);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + startDay.getTimeInMillis() + "&endTime=" + startHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax2 = subGetGiaMax(jsonArr);
                Double giaMin2 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1d&startTime=" + startTime + "&endTime=" + startDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax3 = subGetGiaMax(jsonArr);
                Double giaMin3 = subGetGiaMin(jsonArr);

                // Cuoi ngay
                Calendar endDay = Calendar.getInstance();
                endDay.setTimeInMillis(startTime);
                hour = endDay.get(Calendar.HOUR_OF_DAY);
                endDay.add(Calendar.HOUR_OF_DAY, 24 - hour);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startTime + "&endTime=" + endDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax4 = subGetGiaMax(jsonArr);
                Double giaMin4 = subGetGiaMin(jsonArr);

                giaMax = Math.max(Math.max(Math.max(giaMax1, giaMax2), giaMax3), giaMax4);
                giaMin = Math.min(Math.min(Math.min(giaMin1, giaMin2), giaMin3), giaMin4);
//                giaMax = Math.max(Math.max(giaMax1, giaMax2), giaMax3);
//                giaMax = giaMax1 > giaMax2 ? (giaMax1 > giaMax3 ? giaMax1 : giaMax3) : giaMax2;
//                int max =  Math.max(Math.max(x,y),z);
//                           Math.min(Math.min(a, b), c);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
//        Log.e("Gia MIN Binance", strCoin + ": " + String.format("%.8f", giaMin));
        return giaMin;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return jsonText;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            is.close();
        }
        return "";
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getSupportMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        //inflater.inflate(R.menu.menu_log, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent m = new Intent(getContext(), SettingsPrefActivity.class);
            m.putExtra("KEY", "");
            startActivity(m);
            return true;
        } else if (id == R.id.action_report) {
            Double maxGia = this.lstNotiEntity.get(this.lstNotiEntity.size() - 1).strGiaMax;
            int tinhToan = 0;
            for (NotificationEntity no : this.lstNotiEntity) {
                if (no.strGiaMax > 0) {
                    tinhToan++;
                }
            }

//            if (maxGia <= 0) {
//                Toast.makeText(getContext(), "Please wait while the data is calculated till the last item", Toast.LENGTH_LONG).show();
//            } else
            {

                Log.e(TAG, "action Report");
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", this.lstNotiEntity);
                bundle.putString("TIME", c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                bundle.putString("TINHTOAN", tinhToan + "");
                Intent intent = new Intent(getContext(), ReportActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0) {
            if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceImei();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceImei() {
        try {

            mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephonyManager != null) {
                strDevice = mTelephonyManager.getDeviceId();
            }

            String deviceId = Settings.System.getString(getActivity().getContentResolver(),
                    Settings.System.ANDROID_ID);
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            String serialNumber = (String) get.invoke(c, "sys.serialnumber", "Error");
            if (serialNumber.equals("Error")) {
                serialNumber = (String) get.invoke(c, "ril.serialnumber", "Error");
            }

            Log.e("msg", "DeviceImei " + strDevice + " " + deviceId + " " + serialNumber + " " + android.os.Build.SERIAL);
            // UUID deviceId 2cf5e2f54f2700ab
            // strDevice 359523061328090
            // 359523061328090 2cf5e2f54f2700ab R58G40N3ABY 03157df3526e910a
        } catch (Exception e) {

        }
    }

//    public int getLine() {
//        return Thread.currentThread().getStackTrace()[2].getLineNumber();
//    }
}