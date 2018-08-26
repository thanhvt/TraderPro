package com.traderpro.thanhvt;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.traderpro.GCM.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class VVipActivityFragment extends Fragment {


    // constant
    String TAG = "VVipActivityFragment";
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    ListView listView;
    CustomVVipAdapter customAdapter;
    ArrayList<NotificationEntity> lstNotiEntity;
    ArrayList<NotificationEntity> lstNotiBinance;
    ArrayList<NotificationEntity> lstNotiBittrex;
    //    CheckBox cbBinance, cbBittrex;
    Button btnTrc, btnSau;
    EditText edTime;
    Calendar c;
    SimpleDateFormat df;
    String formattedDate;
    public BittrexData bittrexData = new BittrexData();
    ProgressDialog progDailog;
    TextView txtTongKet, txtLoiLo, txtLoiLo2, txtLoiLo3;
    int lanLai = 0, lanCho = 0, lanLo = 0;
    Double dLai = 0D, dLo = 0D, dTong = 0D;

    FloatingActionButton fab;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    private TelephonyManager mTelephonyManager;

    String strDevice = "";
    Executor threadPoolExecutor;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    public VVipActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar rightNow = Calendar.getInstance();
        int nam = rightNow.get(Calendar.YEAR);
        int thang = rightNow.get(Calendar.MONTH) + 1;
        int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFile(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vvip, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvTradeHistory);

//        cbBinance = (CheckBox) rootView.findViewById(R.id.cbBinance);
//        cbBittrex = (CheckBox) rootView.findViewById(R.id.cbBittrex);

        btnTrc = (Button) rootView.findViewById(R.id.btnTrc);
        btnSau = (Button) rootView.findViewById(R.id.btnSau);
        edTime = (EditText) rootView.findViewById(R.id.edTime);

        txtTongKet = (TextView) rootView.findViewById(R.id.txtTongKet);
        txtLoiLo = (TextView) rootView.findViewById(R.id.txtLoiLo);
        txtLoiLo2 = (TextView) rootView.findViewById(R.id.txtLoiLo2);
        txtLoiLo3 = (TextView) rootView.findViewById(R.id.txtLoiLo3);

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
                    File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_bot.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
//                aDataRow = aDataRow.replace("| ", "");
                NotificationEntity e = new NotificationEntity();
                e = parseFile(aDataRow, nam, thang, ngay);
                if (e != null) {
                    lstNotiEntity.add(e);
                    if (e.strExchange.equals("Bittrex")) lstNotiBittrex.add(e);
                    if (e.strExchange.equals("Binance")) lstNotiBinance.add(e);
                }
//                cbBinance.setText("Binance (" + lstNotiBinance.size() + ")");
//                cbBittrex.setText("Bittrex (" + lstNotiBittrex.size() + ")");
            }
            myReader.close();


            customAdapter = new CustomVVipAdapter(getActivity(), R.layout.layout_botsignal, lstNotiEntity);
            customAdapter.notifyDataSetChanged();
            listView.setAdapter(customAdapter);
            refreshKetQua(lstNotiEntity);
        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
            e.printStackTrace();
        }

//        cbBittrex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    lstNotiEntity.addAll(lstNotiBittrex);
//                } else {
//                    lstNotiEntity.removeAll(lstNotiBittrex);
//                }
//
//                customAdapter.notifyDataSetChanged();
//
//            }
//        });
//
//        cbBinance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    lstNotiEntity.addAll(lstNotiBinance);
//                } else {
//                    lstNotiEntity.removeAll(lstNotiBinance);
//                }
//
//                customAdapter.notifyDataSetChanged();
//            }
//        });

        btnTrc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                cbBinance.setText("Binance (" + 0 + ")");
//                cbBittrex.setText("Bittrex (" + 0 + ")");
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
//                cbBinance.setText("Binance (" + 0 + ")");
//                cbBittrex.setText("Bittrex (" + 0 + ")");
                c.add(Calendar.DATE, 1);
                formattedDate = df.format(c.getTime());

                Log.v("NEXT DATE : ", formattedDate);
                edTime.setText(formattedDate);

                getFile(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int btn_initPosY = fab.getScrollY();
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    fab.animate().cancel();
                    fab.animate().translationYBy(150);
                } else {
                    fab.animate().cancel();
                    fab.animate().translationY(btn_initPosY);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

//                if (mLastFirstVisibleItem < firstVisibleItem) {
//                    Log.i("SCROLLING DOWN", "TRUE");
//                }
//                if (mLastFirstVisibleItem > firstVisibleItem) {
//                    Log.i("SCROLLING UP", "TRUE");
//                }
//                mLastFirstVisibleItem = firstVisibleItem;

            }
        });

        txtLoiLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NotificationEntity> lstTmp = new ArrayList<>();
                for (NotificationEntity noti : lstNotiEntity) {
                    if (noti.strProfit.contains("+")) {
                        lstTmp.add(noti);
                    }
                }
                customAdapter = new CustomVVipAdapter(getActivity(), R.layout.layout_botsignal, lstTmp);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
            }
        });
        txtLoiLo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NotificationEntity> lstTmp = new ArrayList<>();
                for (NotificationEntity noti : lstNotiEntity) {
                    if (noti.strGiaBan.equals("GIA_BAN")) {
                            lstTmp.add(noti);
                    }
                }
                customAdapter = new CustomVVipAdapter(getActivity(), R.layout.layout_botsignal, lstTmp);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
            }
        });
        txtLoiLo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NotificationEntity> lstTmp = new ArrayList<>();
                for (NotificationEntity noti : lstNotiEntity) {
                    if (noti.strProfit.contains("-")) {
                        lstTmp.add(noti);
                    }
                }
                customAdapter = new CustomVVipAdapter(getActivity(), R.layout.layout_botsignal, lstTmp);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
            }
        });
        txtTongKet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAdapter = new CustomVVipAdapter(getActivity(), R.layout.layout_botsignal, lstNotiEntity);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
            }
        });
        return rootView;
    }

    public void refreshKetQua(ArrayList<NotificationEntity> lstNotiEntity) {
        try {
            lanLai = 0;
            lanCho = 0;
            lanLo = 0;
            dLai = 0D;
            dLo = 0D;
            dTong = 0D;

            for (NotificationEntity noti : lstNotiEntity) {
                if (noti.strGiaBan.equals("GIA_BAN")) {
                    lanCho++;
                }
                if (noti.strProfit.contains("+")) {
                    lanLai++;
                    String tmp = noti.strProfit.replace("+", "");
                    tmp = tmp.replace("\\%", "");
                    tmp = tmp.replace("%", "");
                    Double profit = Double.parseDouble(tmp);
                    dLai += profit;
                } else if (noti.strProfit.contains("-")) {
                    lanLo++;
                    String tmp = noti.strProfit.replace("-", "");
                    tmp = tmp.replace("\\%", "");
                    tmp = tmp.replace("%", "");
                    Double profit = Double.parseDouble(tmp);
                    dLo += profit;
                }
            }
            SharedPreferences pref = getActivity().getSharedPreferences(Config.NGON_NGU, 0);
            String strNN = pref.getString("NN", "VN");
            if(strNN.equalsIgnoreCase("VN")) {
                txtLoiLo.setText("Lãi: " + lanLai + " lần (+" + String.format("%.2f", dLai) + "%)");
                txtLoiLo3.setText("Lỗ: " + lanLo + " lần (-" + String.format("%.2f", dLo) + "%)");
                txtLoiLo2.setText("Đang chờ ra: " + lanCho + " lần");
                if (dLai >= dLo) {
                    dTong = dLai - dLo;
                    txtTongKet.setText("KQ: lãi +" + String.format("%.2f", dTong) + "%");
                    txtTongKet.setBackgroundColor(Color.parseColor("#27f546"));

                } else {
                    dTong = dLo - dLai;
                    txtTongKet.setText("KQ: lỗ -" + String.format("%.2f", dTong) + "%");
                    txtTongKet.setBackgroundColor(Color.parseColor("#ff0000"));
                }

            }else{
                txtLoiLo.setText("Win: " + lanLai + " times (+" + String.format("%.2f", dLai) + "%)");
                txtLoiLo3.setText("Lost: " + lanLo + " times (-" + String.format("%.2f", dLo) + "%)");
                txtLoiLo2.setText("Waiting: " + lanCho + " times");
                if (dLai >= dLo) {
                    dTong = dLai - dLo;
                    txtTongKet.setText("Result: win +" + String.format("%.2f", dTong) + "%");
                    txtTongKet.setBackgroundColor(Color.parseColor("#27f546"));

                } else {
                    dTong = dLo - dLai;
                    txtTongKet.setText("Result: lost -" + String.format("%.2f", dTong) + "%");
                    txtTongKet.setBackgroundColor(Color.parseColor("#ff0000"));
                }
            }
        } catch (Exception e) {
            Log.e(TAG + " refreshKetQua", e.getMessage());
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
                    File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_bot.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
                if (!aDataRow.trim().equalsIgnoreCase("")) {
//                    aDataRow = aDataRow.replace("| ", "");
                    Log.e(TAG, aDataRow);
                    NotificationEntity e = new NotificationEntity();
                    e = parseFile(aDataRow, nam, thang, ngay);
                    if (e != null) {
                        lstNotiEntity.add(e);
                        if (e.strExchange.equals("Bittrex")) lstNotiBittrex.add(e);
                        if (e.strExchange.equals("Binance")) lstNotiBinance.add(e);
                    }
                }


//                cbBinance.setText("Binance (" + lstNotiBinance.size() + ")");
//                cbBittrex.setText("Bittrex (" + lstNotiBittrex.size() + ")");
            }
            myReader.close();
            customAdapter = new CustomVVipAdapter(getActivity(), R.layout.layout_botsignal, lstNotiEntity);
            customAdapter.notifyDataSetChanged();
            listView.setAdapter(customAdapter);
            refreshKetQua(lstNotiEntity);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getFile ex " + e.getMessage());
        }
    }

    public NotificationEntity parseFile(String strIn, int nam, int thang, int ngay) {
        try {
            NotificationEntity e = new NotificationEntity();
            String tmp[] = strIn.split("\\|");
            if (tmp.length < 6) return null;
            e.strExchange = tmp[6].trim();
            e.strCoin = tmp[1].trim();
            e.strGia = tmp[2].trim();
//            e.strVol = tmp[3].trim();
//            e.strVolTB = tmp[4].trim();
            e.strTime = tmp[0].trim();
//            e.strCase = tmp.length > 6 ? tmp[6].trim() : "0";
//            e.strBuySell = tmp.length > 7 ? tmp[7].trim() : "";
//            e.strTakerMaker = tmp.length > 8 ? tmp[8].trim() : "";
//            e.strGiaMax = 0D;

            e.strGiaBan = tmp[3].trim().equals("GIA_BAN") ? "GIA_BAN" : tmp[3].trim();
            e.strTimeBan = tmp[3].trim().equals("TIME_BAN") ? "TIME_BAN" : tmp[4].trim();
            e.strProfit = tmp[3].trim().equals("PROFIT") ? "PROFIT" : tmp[5].trim();

            /*
                String content = strTime + "|" + strCoin + "|" + strGiaMua + "|" + strGiaBan
                        + "|" + strTimeBan + "|" + strProfit + "|" + "Binance";
             */
//            String[] mTime = e.strTime.split(":");
//            Calendar c = Calendar.getInstance();
//            c.set(nam, thang - 1, ngay, Integer.parseInt(mTime[0]), Integer.parseInt(mTime[1]));
            new ExchangeGetPrice(e).executeOnExecutor(threadPoolExecutor, e.strCoin);
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
                mEn.strGiaHienTai = getGiaHienTai(params[0]);
            } else if (mEn.strExchange.equals("Bittrex")) {
//                mEn.strGiaMax = getGiaMaxBittrex(params[0], params[1]);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customAdapter.notifyDataSetChanged();

                    }
                });
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

    public static String readJsonFromUrl(String url) throws IOException, JSONException {
        System.out.println(url);
        InputStream is = new URL(url).openStream();
        System.out.println("1.2.1");
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            System.out.println("1.2.2");
            String jsonText = readAll(rd);
            System.out.println("1.2.3");
            return jsonText;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            is.close();
        }
        return "";
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


}
