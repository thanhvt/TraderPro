package com.traderpro.thanhvt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.traderpro.GCM.Config;

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
        String amountBTC = "";
        String binPri = "";
        String binPub = "";
        float number = 0;
        String BUYSELL = extras.getString("BUYSELL");
        String strCoin = extras.getString("COIN");
        String PUBLIC_KEY = "yfhnEwr47LSL7Lbdx2bymCAVoi9YkAZINVkKckotEy7YtbotIuudGisaEnWlyjS1";
        String PRIVATE_KEY = "eXoM5w9uAeqZYoRioeWctmgsbkJUBUeQn5AXnLZVoBq3DsoJLQLbFrAcoH6Z094C";
        String price = extras.getString("PRICE");
        if (price.contains("~")) {
            price = price.replace("~", "");
        }

        //int number = extras.getInt("PRICE");
        //Intent myIntent = new Intent(context, DetectSignalService.class);
        //context.startService(myIntent);
        //editor.putString("BIN_PUB", binPub);
        //editor.putString("BIN_PRI", binPri);
        //editor.putString("AMOUNT_BTC", amountBTC);
        new ExchangeSellBuy().execute(BUYSELL, strCoin, price);

    }

    class ExchangeSellBuy extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BittrexData data = new BittrexData();
            String BUYSELL = params[0];
            String strCoin = params[1];
            String price = params[2];
            String amountBTC = "";
            //String binPri ="";
            //String binPub = "";
            float number = 0;
            String PUBLIC_KEY = "yfhnEwr47LSL7Lbdx2bymCAVoi9YkAZINVkKckotEy7YtbotIuudGisaEnWlyjS1";
            String PRIVATE_KEY = "eXoM5w9uAeqZYoRioeWctmgsbkJUBUeQn5AXnLZVoBq3DsoJLQLbFrAcoH6Z094C";
            SharedPreferences pref = mContext.getSharedPreferences(Config.BOT_API, 0);
            if (pref != null) {
                int API = pref.getInt("USE_API", 0);
                if (API == 1) {
                    //String strNN = pref.getString("NN", "VN");
                    String bitPub = pref.getString("BIT_PUB", "");
                    String bitPri = pref.getString("BIT_PRI", "");
                    //binPub = pref.getString("BIN_PUB", "");
                    //binPri = pref.getString("BIN_PRI", "");
                    amountBTC = pref.getString("AMOUNT_BTC", "");
                    PUBLIC_KEY = pref.getString("BIT_PUB", "");
                    PRIVATE_KEY = pref.getString("BIT_PRI", "");
                } else {
                    String bitPub = pref.getString("BIT_PUB", "");
                    String bitPri = pref.getString("BIT_PRI", "");
                    //binPub = pref.getString("BIN_PUB", "");
                    //binPri = pref.getString("BIN_PRI", "");
                    amountBTC = pref.getString("AMOUNT_BTC", "");
                    PUBLIC_KEY = pref.getString("BIT_PUB", "");
                    PRIVATE_KEY = pref.getString("BIT_PRI", "");
                }
                if (API == 1) {
                    float numberF = Float.parseFloat(amountBTC) / Float.parseFloat(price);
                    number = (int) numberF;
                    Log.e("NUMBER: ", number + "");
                    Log.e("STRCOIN: ", strCoin);
                    //        NotiEventReceiver.setupAlarm(context);
                    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(PUBLIC_KEY, PRIVATE_KEY);
                    BinanceApiRestClient client = factory.newRestClient();
                    NewOrderResponse newOrderResponse = new NewOrderResponse();
                    if (BUYSELL.equals("BUY")) {
                        newOrderResponse = client.newOrder(marketBuy(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));

                    } else if (BUYSELL.equals("SELL")) {
                        newOrderResponse = client.newOrder(marketSell(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));
                    }
                }else{
                    // not buy sell

                }
            }


            return null;
        }
    }
}