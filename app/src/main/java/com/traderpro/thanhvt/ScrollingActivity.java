package com.traderpro.thanhvt;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.traderpro.GCM.Config;
import com.traderpro.GCM.NotificationUtils;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ScrollingActivity extends AppCompatActivity {


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Toolbar toolbar;
    TextView tvBTC, tvPrice;
    String regId = "";
    String TAG = "ScrollingActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        setupToolbar();

        setupViewPager();

        setupCollapsingToolbar();

        setupDrawer();

        setUpGCM();

        requestAppPermissions();

        Intent myIntent = new Intent(getApplicationContext(), DetectSignalService.class);
        startService(myIntent);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//        notificationUtils.readExcel(getApplicationContext());
//        notificationUtils.saveExcelFile(getApplicationContext());

    }

    public void setUpGCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                }
            }
        };

        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e("REGID", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            Log.e("REGID", "Firebase Reg Id: " + regId);

            try {
                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "TraderPro");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }

                File myFile = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "TraderPro" + File.separator + "key.txt");
                // Neu chua co file key thi tao file key, ghi, push
                if (!myFile.exists()) {
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append(regId);
                    myOutWriter.close();
                    fOut.close();

                    String strMess = getManufacturer() + " | " + getModel() + " | " + "Android" + " | " + getSerialNumber()
                            + " | " + getUuid() + " | " + getOSVersion() + " | " + regId;
//                    AsyncPush push = new AsyncPush();
//                    push.new TraderPush().excute("NEW REGISTER", strMess);
                    new AsyncPush().execute("NEW REGISTER", strMess);

                } else {
                    // Neu co roi thi kiem tra giong hay khong, khong giong thi update, push
                    FileInputStream fIn = new FileInputStream(myFile);
                    BufferedReader myReader = new BufferedReader(
                            new InputStreamReader(fIn));
                    String aDataRow = "";
                    String regCu = "";
                    while ((aDataRow = myReader.readLine()) != null) {
                        regCu += aDataRow;
                    }
                    myReader.close();

                    if (regCu.equalsIgnoreCase(regId)) {

                    } else {
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.write(regId);
                        myOutWriter.close();
                        fOut.close();

                        String strMess = getManufacturer() + " | " + getModel() + " | " + "Android" + " | " + getSerialNumber()
                                + " | " + getUuid() + " | " + getOSVersion() + " | " + regId;
                        new AsyncPush().execute("UPDATED REGISTER", strMess);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

        } else
            Log.e("NOT OK", "Firebase Reg Id is not received yet!");
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

    public String getUuid() {
        String uuid = Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return uuid;
    }

    public String getModel() {
        String model = android.os.Build.MODEL;
        return model;
    }

    public String getProductName() {
        String productname = android.os.Build.PRODUCT;
        return productname;
    }

    public String getManufacturer() {
        String manufacturer = android.os.Build.MANUFACTURER;
        return manufacturer;
    }

    public String getSerialNumber() {
        String serial = android.os.Build.SERIAL;
        return serial;
    }

    /**
     * Get the OS version.
     *
     * @return
     */
    public String getOSVersion() {
        String osversion = android.os.Build.VERSION.RELEASE;
        return osversion;
    }

    public String getSDKVersion() {
        @SuppressWarnings("deprecation")
        String sdkversion = android.os.Build.VERSION.SDK;
        return sdkversion;
    }

//    String ipServer = "http://103.63.109.173:8070";
//
//    class InsertDeviceTask extends AsyncTask<UserDevice, Void, String> {
//        String jsonText = "";
//        HttpURLConnection connection;
//
//        @Override
//        protected String doInBackground(UserDevice... us) {
//            try {
//                URL url = new URL(ipServer + "/api/ecoin/update_device?userid=undefined" + "&nhasx=" + us[0].NHASX
//                        + "&tentbi=" + us[0].TENTBI + "&os=" + "Android" + "&serial=" + us[0].SERIAL + "&uuid=" + us[0].UUID + "&version=" + us[0].VERSION
//                        + "&devicetoken=" + us[0].DEVICE_TOKEN);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.connect();
//                InputStream inputStream = connection.getInputStream();
//
//                int byteCharacter;
//
//                while ((byteCharacter = inputStream.read()) != -1) {
//                    char c = (char) byteCharacter;
//                    jsonText += c;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                connection.disconnect();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void setupDrawer() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("TabbedCoordinatorLayout");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvBTC = (TextView) findViewById(R.id.tv_title);
        tvPrice = (TextView) findViewById(R.id.tv_description);
        tvBTC.setText("Wallet: 1623 BTC");
        tvPrice.setText("Estimated Value: 12 821 966 $");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new TabMua(), "BUY");
        adapter.addFrag(new LogNotification(), "RingRing");
//        adapter.addFrag(new LogActivity(), "History");
//        adapter.addFrag(new TabBan(), "SELL");
        adapter.addFrag(new VVipActivityFragment(), "VVip");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        SharedPreferences pref = getSharedPreferences(Config.NGON_NGU, 0);
        String strNN = pref.getString("NN", "VN");
        MenuItem item0 = menu.getItem(0);
        item0.setTitle(strNN.equals("VN") ? R.string.trade_api_Vn : R.string.trade_api);
        MenuItem item1 = menu.getItem(1);
        item1.setTitle(strNN.equals("VN") ? R.string.setting_Vn : R.string.setting);
        MenuItem item2 = menu.getItem(2);
        item2.setTitle(strNN.equals("VN") ? R.string.active_traderpro_Vn : R.string.active_traderpro_Vn);
        MenuItem item3 = menu.getItem(3);
        item3.setTitle(strNN.equals("VN") ? R.string.report_statistics_Vn : R.string.report_statistics);
        MenuItem item4 = menu.getItem(4);
        item4.setTitle(strNN.equals("VN") ? R.string.about_Vn : R.string.about);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent m = new Intent(ScrollingActivity.this, SettingsPrefActivity.class);
            m.putExtra("KEY", regId);
            startActivity(m);
            return true;
        }
        if (id == R.id.action_key) {
            Intent m = new Intent(ScrollingActivity.this, TradeApiActivity.class);
            startActivity(m);
            return true;
        } if (id == R.id.action_active) {
            Intent m = new Intent(ScrollingActivity.this, BidAskActivity.class);
            startActivity(m);
            return true;
        }
        if (id == R.id.action_about) {
            Intent m = new Intent(ScrollingActivity.this, AboutActivity.class);
            startActivity(m);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
