package com.traderpro.thanhvt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.traderpro.GCM.Config;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.Comparator;
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
    SwipeRefreshLayout mSwipeRefreshLayout;
    CustomNotificationAdapter customAdapter;
    ArrayList<NotificationEntity> lstNotiEntity;
    ArrayList<NotificationEntity> lstNotiBinance;
    ArrayList<NotificationEntity> lstNotiBittrex;
    CheckBox cbBinance, cbBittrex;
    Button btnTrc, btnSau;
    EditText edTime;
    TextView txtJoin;
    Calendar c;
    SimpleDateFormat df;
    String formattedDate;
    boolean isCheckBinance = true;
    boolean isCheckBittrex = true;
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

    private Spinner spinner_nav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPreferences pref = getActivity().getSharedPreferences(Config.TIME_REFRESH, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("TIME_REFRESH", System.currentTimeMillis());
        editor.commit();

//        spinner_nav = (Spinner) getActivity().findViewById(R.id.spinner_toolBar);
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("Top News");
//        list.add("Politics");
//        list.add("Business");
//        list.add("Sports");
//        list.add("Movies");

//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.drawer_drop_down,
//                    android.R.layout.simple_spinner_item);
//        spinner_nav.setPrompt("");
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                // this part is needed for hiding the original view
//                View view = super.getView(position, convertView, parent);
//                view.setVisibility(View.GONE);
//
//                return view;
//            }
//        };
//        spinner_nav.setAdapter(spinnerAdapter);
    }

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        listView = (ListView) rootView.findViewById(R.id.lvTradeHistory);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences pref = getActivity().getSharedPreferences(Config.TIME_REFRESH, 0);
                if (pref != null) {
                    long TIME_REFRESH = pref.getLong("TIME_REFRESH", 0);
                    if (System.currentTimeMillis() - TIME_REFRESH < 15 * 1000) {
                        Toast.makeText(getContext(), "Waiting 15 seconds to refresh !", Toast.LENGTH_LONG).show();

                    } else {
                        shuffle();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putLong("TIME_REFRESH", System.currentTimeMillis());
                        editor.commit();
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        cbBinance = (CheckBox) rootView.findViewById(R.id.cbBinance);
        cbBittrex = (CheckBox) rootView.findViewById(R.id.cbBittrex);

        btnTrc = (Button) rootView.findViewById(R.id.btnTrc);
        btnSau = (Button) rootView.findViewById(R.id.btnSau);
        edTime = (EditText) rootView.findViewById(R.id.edTime);

        txtJoin = (TextView) rootView.findViewById(R.id.txtJoin);
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
                if (!aDataRow.trim().isEmpty()) {

                    e = parseFile(aDataRow, nam, thang, ngay);
                    lstNotiEntity.add(e);
                    if (e.strExchange.equals("Bittrex")) lstNotiBittrex.add(e);
                    if (e.strExchange.equals("Binance")) lstNotiBinance.add(e);
                }
                cbBinance.setText("Binance (" + lstNotiBinance.size() + ")");
                cbBittrex.setText("Bittrex (" + lstNotiBittrex.size() + ")");
            }
            myReader.close();


            customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notification_n, lstNotiEntity, bittrexData, nam, thang, ngay);
            customAdapter.notifyDataSetChanged();
            listView.setAdapter(customAdapter);
        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
            e.printStackTrace();
        }

        cbBittrex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Calendar rightNow = Calendar.getInstance();
                int nam = rightNow.get(Calendar.YEAR);
                int thang = rightNow.get(Calendar.MONTH) + 1;
                int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
                if (isChecked) {
                    lstNotiEntity.addAll(lstNotiBittrex);
                    isCheckBittrex = true;
                    cbBittrex.setText("Bittrex (" + lstNotiBittrex.size() + ")");
                } else {
                    isCheckBittrex = false;
                    lstNotiEntity.removeAll(lstNotiBittrex);
                    cbBittrex.setText("Bittrex (" + 0 + ")");
                }
                if (customAdapter != null && lstNotiEntity.size() > 0) {
                    customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notification_n, lstNotiEntity, bittrexData, nam, thang, ngay);
                    customAdapter.notifyDataSetChanged();
                    listView.setAdapter(customAdapter);
                }

            }
        });

        cbBinance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Calendar rightNow = Calendar.getInstance();
                int nam = rightNow.get(Calendar.YEAR);
                int thang = rightNow.get(Calendar.MONTH) + 1;
                int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
                if (isChecked) {
                    isCheckBinance = true;
                    lstNotiEntity.addAll(lstNotiBinance);
                    cbBinance.setText("Binance (" + lstNotiBinance.size() + ")");

                } else {
                    isCheckBinance = false;
                    lstNotiEntity.removeAll(lstNotiBinance);
                    cbBinance.setText("Binance (" + 0 + ")");

                }

                if (customAdapter != null && lstNotiEntity.size() > 0) {
                    customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notification_n, lstNotiEntity, bittrexData, nam, thang, ngay);
                    customAdapter.notifyDataSetChanged();
                    listView.setAdapter(customAdapter);
                }
            }
        });

        btnTrc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences(Config.TIME_REFRESH, 0);
                if (pref != null) {
                    long TIME_REFRESH = pref.getLong("TIME_REFRESH", 0);
                    if (System.currentTimeMillis() - TIME_REFRESH < 15 * 1000) {
                        Toast.makeText(getContext(), "Waiting 15 seconds to action !", Toast.LENGTH_LONG).show();

                    } else {
                        cbBinance.setText("Binance (" + 0 + ")");
                        cbBittrex.setText("Bittrex (" + 0 + ")");
                        c.add(Calendar.DATE, -1);
                        formattedDate = df.format(c.getTime());

                        Log.v("PREVIOUS DATE : ", formattedDate);
                        edTime.setText(formattedDate);

                        getFile(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putLong("TIME_REFRESH", System.currentTimeMillis());
                        editor.commit();
                    }
                }
            }
        });

        btnSau.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences pref = getActivity().getSharedPreferences(Config.TIME_REFRESH, 0);
                if (pref != null) {
                    long TIME_REFRESH = pref.getLong("TIME_REFRESH", 0);
                    if (System.currentTimeMillis() - TIME_REFRESH < 15 * 1000) {
                        Toast.makeText(getContext(), "Waiting 15 seconds to action !", Toast.LENGTH_LONG).show();

                    } else {
                        cbBinance.setText("Binance (" + 0 + ")");
                        cbBittrex.setText("Bittrex (" + 0 + ")");
                        c.add(Calendar.DATE, 1);
                        formattedDate = df.format(c.getTime());

                        Log.v("NEXT DATE : ", formattedDate);
                        edTime.setText(formattedDate);

                        getFile(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putLong("TIME_REFRESH", System.currentTimeMillis());
                        editor.commit();
                    }
                }
            }
        });

        String content = "Join <a href=\"https://t.me/joinchat/HX6H_k2HnC6LHLCEU0hptw\"><b>PIS TRADER</b></a> on Telegram";
//        Spannable s = (Spannable) Html.fromHtml(content);
//        for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
//            s.setSpan(new UnderlineSpan() {
//                public void updateDrawState(TextPaint tp) {
//                    tp.setUnderlineText(false);
//                }
//            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
//        }
//        txtJoin.setText(s);
//        txtJoin.setMovementMethod(LinkMovementMethod.getInstance());
//        Utils.removeUnderlines((Spannable)txtJoin.getText());
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(content));
        spannedText = (Spannable)
                Html.fromHtml(content);
        txtJoin.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable processedText = removeUnderlines(spannedText);
        txtJoin.setText(processedText);

        return rootView;
    }

    public void shuffle() {
        try {
            int nam = c.get(Calendar.YEAR);
            int thang = c.get(Calendar.MONTH) + 1;
            int ngay = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);

            String strDate = edTime.getText().toString();
            Log.e(TAG, strDate);

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
            lstNotiEntity.clear();
            lstNotiBittrex.clear();
            lstNotiBinance.clear();
            while ((aDataRow = myReader.readLine()) != null) {
                aDataRow = aDataRow.replace("| ", "");
                if (!aDataRow.trim().isEmpty()) {
                    NotificationEntity e = new NotificationEntity();
                    e = parseFile(aDataRow, nam, thang, ngay);
                    lstNotiEntity.add(e);

                    if (e.strExchange.equals("Bittrex")) lstNotiBittrex.add(e);
                    if (e.strExchange.equals("Binance")) lstNotiBinance.add(e);
                }
                cbBinance.setText("Binance (" + lstNotiBinance.size() + ")");
                cbBittrex.setText("Bittrex (" + lstNotiBittrex.size() + ")");
            }
            if (isCheckBinance) {
                //lstNotiEntity.addAll(lstNotiBinance);
            } else {
                //isCheckBinance = false;
                lstNotiEntity.removeAll(lstNotiBinance);
                cbBinance.setText("Binance (" + "0" + ")");
            }

            if (isCheckBittrex) {
                //lstNotiEntity.addAll(lstNotiBinance);
            } else {
                //isCheckBinance = false;
                lstNotiEntity.removeAll(lstNotiBittrex);
                cbBittrex.setText("Bittrex (" + "0" + ")");
            }
            myReader.close();


            customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notification_n, lstNotiEntity, bittrexData, nam, thang, ngay);
            customAdapter.notifyDataSetChanged();
            listView.setAdapter(customAdapter);
        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
            e.printStackTrace();
        }
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
            customAdapter = new CustomNotificationAdapter(getActivity(), R.layout.layout_notification_n, lstNotiEntity, bittrexData, nam, thang, ngay);
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
            e.strImageURL = tmp.length > 10 ? tmp[13].trim() : "1";
            e.strId = tmp.length > 14 ? tmp[14].trim() : "1";
            e.strBuySell = tmp.length > 15 ? tmp[15].trim() : "1";
            e.numberBuy = tmp.length > 17 && strIn.contains("BUYY") ? tmp[17].trim() : "0";
            e.numberSell = tmp.length > 17 && strIn.contains("SELL") ? tmp[17].trim() : "0";
//            e.strBuySell = tmp.length > 16 ? tmp[16].trim() : "";
//            e.strBuySell = tmp.length > 17 ? tmp[17].trim() : "";
            e.strGiaTrade = tmp.length > 18 ? tmp[18].trim() : "";
            // itemBuy += "|" + "BUYY" + "|" + newOrderResponse.getOrderId() + "|" + newOrderResponse.getExecutedQty() + "|" + strGiaSan;
            String[] mTime = e.strTime.split(":");
            Calendar c = Calendar.getInstance();
            c.set(nam, thang - 1, ngay, Integer.parseInt(mTime[0]), Integer.parseInt(mTime[1]));
//            new ExchangeGetPrice(e).execute(e.strCoin, c.getTimeInMillis() + "");
//            new ExchangeGetPrice(e).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, e.strCoin, c.getTimeInMillis() + "");
            new ExchangeGetPrice(e).executeOnExecutor(threadPoolExecutor, e.strCoin, c.getTimeInMillis() + "");
            Double tangSoLan = (Double.parseDouble(e.strVol) / Double.parseDouble(e.strVolTB)) * 100;
            e.tangSoLan = tangSoLan;
//            GetUserDeviceDataService service = RetrofitInstance.getImageInstance().create(GetUserDeviceDataService.class);
//            Call<List<ImageEntity>> call = service.getAllPhotos();
//            call.enqueue(new Callback<List<ImageEntity>>() {
//                @Override
//                public void onResponse(Call<List<ImageEntity>> call, Response<List<ImageEntity>> response) {
//                    List<ImageEntity> strX = response.body();
//                    Log.d(TAG + " 436", strX.get(0).getId());
//                }
//
//                @Override
//                public void onFailure(Call<List<ImageEntity>> call, Throwable t) {
//                }
//            });
            return e;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "parseFile ex " + e.getMessage());
            return null;
        }

    }

    public Double getGiaHienTai(String strCoin) {
        Double giaHT = 0D;
        try {
            String res = readJsonFromUrl("https://api.binance.com/api/v1/ticker/price?symbol=" + strCoin + "BTC");
            JSONObject jsonObj = new JSONObject(res);
            giaHT = Double.parseDouble(jsonObj.getString("price"));
            Log.e(TAG, giaHT + "");
        } catch (Exception e) {

        }
        return giaHT;
    }

    public JSONArray subGetGiaMax(JSONArray arr) {
        JSONArray jsonMax = new JSONArray();
        try {
            Double giaMax = 0D;
            for (int i = 0; i < arr.length(); i++) {
                JSONArray arrItem = arr.getJSONArray(i);
                Double giaTime = Double.parseDouble(arrItem.getString(2));
                if (giaTime >= giaMax) {
                    giaMax = giaTime;
                    jsonMax = arrItem;
                }
            }
//            return giaMax;
            Log.e(TAG + " jsonMax", jsonMax.toString());
            if (jsonMax.length() == 0) {
                jsonMax.put(0);
                jsonMax.put(0);
                jsonMax.put(0);
            }
            return jsonMax;
        } catch (Exception e) {

        }
        return jsonMax;
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

    public Double getGiaMaxBinance(String strCoin, String strTime, NotificationEntity ne, StringBuilder sbMaxTime) {
        JSONArray jsonMax = new JSONArray();
        Double giaMax = 0D;
        try {
            long timeNow = System.currentTimeMillis();
            ne.strTimeFixProfit = System.currentTimeMillis() + "";
            long startTime = Long.parseLong(strTime);
            // Neu gio hien tai moi hon gio bat dau mua chua toi 60', thi query theo 3' de lay ra gia tri max
            if (timeNow - startTime < 60 * 60 * 1000) {
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
//                giaMax = subGetGiaMax(jsonArr);
                jsonMax = subGetGiaMax(jsonArr);
                giaMax = Double.parseDouble(jsonMax.getString(2));
                sbMaxTime.append(jsonMax.getString(0));

                Double dGia = Double.parseDouble(ne.strGia);
                for (int x = 0; x < jsonArr.length(); x++) {
                    JSONArray item = jsonArr.getJSONArray(x);
                    Double dbHigh = Double.parseDouble(item.getString(2));

                    if (dbHigh > 1.012 * dGia) {
                        ne.strTimeFixProfit = item.getString(0);
                        break;
                    }
                }
            } else if (timeNow - startTime < 24 * 60 * 60 * 1000) {
                // 1H dau theo 3m
                // 23H theo 1H
                // 1H cuoi theo 3m
                Calendar rightNow = Calendar.getInstance();
                int min = rightNow.get(Calendar.MINUTE);
                rightNow.add(Calendar.MINUTE, -min);
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + rightNow.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
//                Double giaMax1 = subGetGiaMax(jsonArr);
                JSONArray jsonMax1 = subGetGiaMax(jsonArr);
                Double giaMax1 = Double.parseDouble(jsonMax1.getString(2));
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + strTime + "&endTime=" + rightNow.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                JSONArray jsonArr2 = new JSONArray(res);
//                Double giaMax2 = subGetGiaMax(jsonArr);
                JSONArray jsonMax2 = subGetGiaMax(jsonArr2);
                Double giaMax2 = Double.parseDouble(jsonMax2.getString(2));
                // Cuoi gio
                Calendar endHour = Calendar.getInstance();
                endHour.setTimeInMillis(startTime);
                min = endHour.get(Calendar.MINUTE);
                endHour.add(Calendar.MINUTE, 60 - min);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime + "&endTime=" + endHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                JSONArray jsonArr3 = new JSONArray(res);
//                Double giaMax3 = subGetGiaMax(jsonArr);
                JSONArray jsonMax3 = subGetGiaMax(jsonArr3);
                Double giaMax3 = Double.parseDouble(jsonMax3.getString(2));
                giaMax = Math.max(Math.max(giaMax1, giaMax2), giaMax3);
                if (giaMax == giaMax1) sbMaxTime.append(jsonMax1.getString(0));
                if (giaMax == giaMax2) sbMaxTime.append(jsonMax2.getString(0));
                if (giaMax == giaMax3) sbMaxTime.append(jsonMax3.getString(0));

                JSONArray arrTemp = new JSONArray();
                for (int i = 0; i < jsonArr3.length(); i++) {
                    arrTemp.put(jsonArr3.getJSONArray(i));
                }
                for (int i = 0; i < jsonArr2.length(); i++) {
                    arrTemp.put(jsonArr2.getJSONArray(i));
                }
                for (int i = 0; i < jsonArr.length(); i++) {
                    arrTemp.put(jsonArr.getJSONArray(i));
                }
                Double dGia = Double.parseDouble(ne.strGia);
                for (int x = 0; x < arrTemp.length(); x++) {
                    JSONArray item = arrTemp.getJSONArray(x);
                    Double dbHigh = Double.parseDouble(item.getString(2));

                    if (dbHigh > 1.012 * dGia) {
                        ne.strTimeFixProfit = item.getString(0);
                        break;
                    }
                }
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

                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startHour.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
//                Double giaMax1 = subGetGiaMax(jsonArr);
                JSONArray jsonMax1 = subGetGiaMax(jsonArr);
                Double giaMax1 = Double.parseDouble(jsonMax1.getString(2));
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
                JSONArray jsonArr2 = new JSONArray(res);
//                Double giaMax2 = subGetGiaMax(jsonArr);
                JSONArray jsonMax2 = subGetGiaMax(jsonArr2);
                Double giaMax2 = Double.parseDouble(jsonMax2.getString(2));
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1d&startTime=" + startTime + "&endTime=" + startDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                JSONArray jsonArr3 = new JSONArray(res);
//                Double giaMax3 = subGetGiaMax(jsonArr);
                JSONArray jsonMax3 = subGetGiaMax(jsonArr3);
                Double giaMax3 = Double.parseDouble(jsonMax3.getString(2));
                // Cuoi ngay
                Calendar endDay = Calendar.getInstance();
                endDay.setTimeInMillis(startTime);
                hour = endDay.get(Calendar.HOUR_OF_DAY);
                endDay.add(Calendar.HOUR_OF_DAY, 24 - hour);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startTime + "&endTime=" + endDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                JSONArray jsonArr4 = new JSONArray(res);
//                Double giaMax4 = subGetGiaMax(jsonArr);
                JSONArray jsonMax4 = subGetGiaMax(jsonArr4);
                Double giaMax4 = Double.parseDouble(jsonMax4.getString(2));
                giaMax = Math.max(Math.max(Math.max(giaMax1, giaMax2), giaMax3), giaMax4);
                if (giaMax == giaMax1) sbMaxTime.append(jsonMax1.getString(0));
                if (giaMax == giaMax2) sbMaxTime.append(jsonMax2.getString(0));
                if (giaMax == giaMax3) sbMaxTime.append(jsonMax3.getString(0));
                if (giaMax == giaMax4) sbMaxTime.append(jsonMax4.getString(0));

                JSONArray arrTemp = new JSONArray();
                for (int i = 0; i < jsonArr4.length(); i++) {
                    arrTemp.put(jsonArr4.getJSONArray(i));
                }
                for (int i = 0; i < jsonArr3.length(); i++) {
                    arrTemp.put(jsonArr3.getJSONArray(i));
                }
                for (int i = 0; i < jsonArr2.length(); i++) {
                    arrTemp.put(jsonArr2.getJSONArray(i));
                }
                for (int i = 0; i < jsonArr.length(); i++) {
                    arrTemp.put(jsonArr.getJSONArray(i));
                }
                Double dGia = Double.parseDouble(ne.strGia);
                for (int x = 0; x < arrTemp.length(); x++) {
                    JSONArray item = arrTemp.getJSONArray(x);
                    Double dbHigh = Double.parseDouble(item.getString(2));

                    if (dbHigh > 1.012 * dGia) {
                        ne.strTimeFixProfit = item.getString(0);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        Log.e("Gia MAX Binance", strCoin + ": " + String.format("%.8f", giaMax) + " - " + sbMaxTime.toString());
        return giaMax;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        //inflater.inflate(R.menu.menu_log, menu);
        //super.onCreateOptionsMenu(menu, inflater);
//        getActivity().getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Coin ?");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
            }
        });

        try {

//            MenuItem spinnerMenuItem = menu.findItem(R.id.action_sort);
//            final Spinner spinner = (Spinner) spinnerMenuItem.getActionView();
//            final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.drawer_drop_down,
//                    android.R.layout.simple_spinner_item);
//            ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>
//                    (getContext(), android.R.layout.simple_spinner_item, R.array.drawer_drop_down) {
//                @Override
//                public View getView(int position, View convertView,
//                                    ViewGroup parent) {
//                    // this part is needed for hiding the original view
//                    View view = super.getView(position, convertView, parent);
//                    view.setVisibility(View.GONE);
//                    return view;
//                }
//            };
//            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, R.array.drawer_drop_down){
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    // this part is needed for hiding the original view
//                    View view = super.getView(position, convertView, parent);
////                    view.setVisibility(View.GONE);
//
//                    return view;
//                }
//            };
//            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(spinnerAdapter);
//            spinner.setPrompt("");
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                           int arg2, long arg3) {
//                    ((TextView) arg1).setText(null);
//                    if (arg2 == 0) {
//                    } else if (arg2 == 1) {
//                    } else if (arg2 == 2) {
//
//                    }
//
//                }
//
//                public void onNothingSelected(AdapterView<?> arg0) {
//
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double getGiaMinBinance(String strCoin, String strTime) {
        Double giaMin = 0D;
        try {
            long timeNow = System.currentTimeMillis();
            long startTime = Long.parseLong(strTime);
            // Neu gio hien tai moi hon gio bat dau mua chua toi 60', thi query theo 3' de lay ra gia tri max
            if (timeNow - startTime < 60 * 60 * 1000) {
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
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
                Double giaMin1 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + strTime + "&endTime=" + rightNow.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin2 = subGetGiaMin(jsonArr);
                // Cuoi gio
                Calendar endHour = Calendar.getInstance();
                endHour.setTimeInMillis(startTime);
                min = endHour.get(Calendar.MINUTE);
                endHour.add(Calendar.MINUTE, 60 - min);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime + "&endTime=" + endHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin3 = subGetGiaMin(jsonArr);
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

                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startHour.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
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
                Double giaMin2 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1d&startTime=" + startTime + "&endTime=" + startDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin3 = subGetGiaMin(jsonArr);

                // Cuoi ngay
                Calendar endDay = Calendar.getInstance();
                endDay.setTimeInMillis(startTime);
                hour = endDay.get(Calendar.HOUR_OF_DAY);
                endDay.add(Calendar.HOUR_OF_DAY, 24 - hour);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startTime + "&endTime=" + endDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin4 = subGetGiaMin(jsonArr);

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

    private SearchView searchView;
    private MenuItem searchMenuItem;

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
            {
                Log.e(TAG, "action Report");
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", this.lstNotiEntity);
                bundle.putString("TIME", c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                bundle.putString("TINHTOAN", tinhToan + "");
                bundle.putInt("NGAY", c.get(Calendar.DAY_OF_MONTH));
                bundle.putInt("THANG", (c.get(Calendar.MONTH) + 1));
                bundle.putInt("NAM", c.get(Calendar.YEAR));
                Intent intent = new Intent(getContext(), ReportActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } else if (id == R.id.sortTime) {
            customAdapter.sort(new Comparator<NotificationEntity>() {
                @Override
                public int compare(NotificationEntity arg0, NotificationEntity arg1) {
                    return -arg0.strId.compareTo(arg1.strId);
                }
            });
        } else if (id == R.id.sortCoin) {
            customAdapter.sort(new Comparator<NotificationEntity>() {
                @Override
                public int compare(NotificationEntity arg0, NotificationEntity arg1) {
                    return arg0.strCoin.compareTo(arg1.strCoin);
                }
            });
        } else if (id == R.id.sortGain) {
            customAdapter.sort(new Comparator<NotificationEntity>() {
                @Override
                public int compare(NotificationEntity arg0, NotificationEntity arg1) {
                    Double case1 = Double.parseDouble(arg0.strProfit == null ? "0" : arg0.strProfit);
                    Double case2 = Double.parseDouble(arg1.strProfit == null ? "0" : arg1.strProfit);
                    return -Double.compare(case1, case2);
                }
            });
        } else if (id == R.id.sortDefault) {
            customAdapter.sort(new Comparator<NotificationEntity>() {
                @Override
                public int compare(NotificationEntity arg0, NotificationEntity arg1) {
                    int case1 = Integer.parseInt(arg0.strCase);
                    int case2 = Integer.parseInt(arg1.strCase);
                    return -(case1 - case2);
                }
            });
        } else if (id == R.id.sortOther) {
            customAdapter.sort(new Comparator<NotificationEntity>() {
                @Override
                public int compare(NotificationEntity arg0, NotificationEntity arg1) {
                    return (int) (arg1.tangSoLan - arg0.tangSoLan);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//        if (!searchView.isIconified()) {
//
//        } else {
//            super.onBackPressed();
//        }
//    }

    public Double getGiaMinBinance_Fix(String strCoin, String strTime, String strTimeMax) {
        Double giaMax = 0D;
        Double giaMin = 0D;
        try {
            long timeNow = Long.parseLong(strTimeMax);
            long startTime = Long.parseLong(strTime);
            // Neu gio hien tai moi hon gio bat dau mua chua toi 60', thi query theo 3' de lay ra gia tri max
            if (timeNow - startTime < 60 * 60 * 1000) {
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime + "&endTime=" + strTimeMax;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                giaMin = subGetGiaMin(jsonArr);
            } else if (timeNow - startTime < 24 * 60 * 60 * 1000) {
                // 1H dau theo 3m
                // 23H theo 1H
                // 1H cuoi theo 3m
                Calendar rightNow = Calendar.getInstance();
                int min = rightNow.get(Calendar.MINUTE);
                rightNow.add(Calendar.MINUTE, -min);
//                rightNow.add(Calendar.HOUR_OF_DAY, -7);
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + rightNow.getTimeInMillis() + "&endTime=" + strTimeMax;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                Double giaMin1 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + strTime + "&endTime=" + rightNow.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin2 = subGetGiaMin(jsonArr);
                // Cuoi gio
                Calendar endHour = Calendar.getInstance();
                endHour.setTimeInMillis(startTime);
                min = endHour.get(Calendar.MINUTE);
                endHour.add(Calendar.MINUTE, 60 - min);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime + "&endTime=" + endHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin3 = subGetGiaMin(jsonArr);
//                giaMax = giaMax1 > giaMax2 ? giaMax1 : giaMax2;
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

                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startHour.getTimeInMillis() + "&endTime=" + strTimeMax;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
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
                Double giaMin2 = subGetGiaMin(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1d&startTime=" + startTime + "&endTime=" + startDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin3 = subGetGiaMin(jsonArr);

                // Cuoi ngay
                Calendar endDay = Calendar.getInstance();
                endDay.setTimeInMillis(startTime);
                hour = endDay.get(Calendar.HOUR_OF_DAY);
                endDay.add(Calendar.HOUR_OF_DAY, 24 - hour);
                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startTime + "&endTime=" + endDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMin4 = subGetGiaMin(jsonArr);

                giaMin = Math.min(Math.min(Math.min(giaMin1, giaMin2), giaMin3), giaMin4);
//                giaMax = Math.max(Math.max(giaMax1, giaMax2), giaMax3);
//                giaMax = giaMax1 > giaMax2 ? (giaMax1 > giaMax3 ? giaMax1 : giaMax3) : giaMax2;
//                int max =  Math.max(Math.max(x,y),z);
//                           Math.min(Math.min(a, b), c);
            }
        } catch (Exception e) {
            Log.e("ReportActivity", e.getMessage());
            e.printStackTrace();
        }
        Log.e("Gia MIN Binance", strCoin + ": " + String.format("%.8f", giaMin));
        return giaMin;
    }

    class ExchangeGetPrice extends AsyncTask<String, String, String> {

        NotificationEntity mEn;

        public ExchangeGetPrice(NotificationEntity mEn) {
            this.mEn = mEn;
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            if (mEn.strExchange.equals("Binance")) {
                mEn.strGiaMax = getGiaMaxBinance(params[0], params[1], mEn, sb);
                mEn.strGiaMin = getGiaMinBinance_Fix(params[0], params[1], mEn.strTimeFixProfit);
                mEn.strGiaHienTai = getGiaHienTai(params[0]);
                Double profit = 0D;
                Double dGia = mEn.strGia.length() > 0 ? Double.parseDouble(mEn.strGia) : 0D;
                if (mEn.strGiaMax != 0 && mEn.strGiaMax > dGia) {
                    profit = ((mEn.strGiaMax - dGia) / dGia) * 100;
                }
                mEn.strProfit = profit + "";
                Log.e(TAG + " sb", sb.toString());
                mEn.strTimeGiaMax = sb.toString();
            } else if (mEn.strExchange.equals("Bittrex")) {
//                mEn.strGiaMax = getGiaMaxBittrex(params[0], params[1]);
                mEn.strGiaHienTai = 0D;
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