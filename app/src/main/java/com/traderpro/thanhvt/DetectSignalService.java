package com.traderpro.thanhvt;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;

public class DetectSignalService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000 * 6 * 10; // 60 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    public DetectSignalService() {
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
//        Toast.makeText(this, "Start the Treasure Hunt - Lambo Lamboo and Lambooo", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    // Hủy bỏ dịch vụ.
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
