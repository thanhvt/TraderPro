package com.traderpro.thanhvt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.traderpro.GCM.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;
import static com.binance.api.client.domain.account.NewOrder.marketSell;

public class BuyIntentReceiver extends BroadcastReceiver {
    public Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mContext = context;
        Bundle extras = intent.getExtras();
        String BUYSELL = extras.getString("BUYSELL");
        String strCoin = extras.getString("COIN");
        String price = extras.getString("PRICE");
        if (price.contains("~")) {
            price = price.replace("~", "");
        }
        String title = extras.getString("INTENT");
        String message = extras.getString("MESSAGE");

        //int number = extras.getInt("PRICE");
        //Intent myIntent = new Intent(context, DetectSignalService.class);
        //context.startService(myIntent);
        //editor.putString("BIN_PUB", binPub);
        //editor.putString("BIN_PRI", binPri);
        //editor.putString("AMOUNT_BTC", amountBTC);
        new ExchangeSellBuy().execute(BUYSELL, strCoin, price, title, message);

    }

    class ExchangeSellBuy extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String BUYSELL = params[0];
            String strCoin = params[1];
            String price = params[2];
            String title = params[3];
            String message = params[4];
            String amountBTC = "";
            float number = 0;
            String PUBLIC_KEY = "";
            String PRIVATE_KEY = "";
            SharedPreferences pref = mContext.getSharedPreferences(Config.BOT_API, 0);
            if (pref != null) {
                int API = pref.getInt("USE_API", 0);
                if (API == 1) {
                    amountBTC = pref.getString("AMOUNT_BTC", "");
                    PUBLIC_KEY = pref.getString("BIN_PUB", "");
                    PRIVATE_KEY = pref.getString("BIN_PRI", "");

                    float numberF = Float.parseFloat(amountBTC) / Float.parseFloat(price);
                    number = (int) numberF;
                    Log.e("NUMBER: ", number + "");
                    Log.e("STRCOIN: ", strCoin);

                    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(PUBLIC_KEY, PRIVATE_KEY);
                    BinanceApiRestClient client = factory.newRestClient();
                    NewOrderResponse newOrderResponse = new NewOrderResponse();
                    if (BUYSELL.equals("BUY")) {
                        newOrderResponse = client.newOrder(marketBuy(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));

                    } else if (BUYSELL.equals("SELL")) {
                        newOrderResponse = client.newOrder(marketSell(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));

                    }
                } else {
                    // Hiển thị Toast báo cần cấu hình API
                }
            }


            return null;
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
}