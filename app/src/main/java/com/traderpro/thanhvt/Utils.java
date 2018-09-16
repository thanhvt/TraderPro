package com.traderpro.thanhvt;

import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.traderpro.GCM.Config;
import com.traderpro.my_interface.GetUserDeviceDataService;
import com.traderpro.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.ContentResolver;
import android.content.Context;

public class Utils {
    private Context mContext;
    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public void insertData(UserDevice devices) {
        try {
            GetUserDeviceDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDeviceDataService.class);
            Call<Boolean> call = service.Post(devices);
            Log.wtf("URL Called", call.request().url() + "");
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Boolean postCheck = response.body().booleanValue();
                    Log.e("CHECK PUT", postCheck + "");
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("USERDEVICE", t.getMessage() + "");
                }

            });
        } catch (Exception e) {

        }
    }

    public String getUuid() {
        //SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        //String UUID = pref.getBoolean("FIRSTLOGIN", true);
        String uuid = Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
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
}
