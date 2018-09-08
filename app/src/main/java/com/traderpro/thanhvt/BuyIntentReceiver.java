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
        String title = extras.getString("INTENT");
        String message = extras.getString("MESSAGE");

        //int number = extras.getInt("PRICE");
        //Intent myIntent = new Intent(context, DetectSignalService.class);
        //context.startService(myIntent);
        //editor.putString("BIN_PUB", binPub);
        //editor.putString("BIN_PRI", binPri);
        //editor.putString("AMOUNT_BTC", amountBTC);
        new ExchangeSellBuy().execute(BUYSELL, strCoin, price,title,message);

    }

    class ExchangeSellBuy extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BittrexData data = new BittrexData();
            String BUYSELL = params[0];
            String strCoin = params[1];
            String price = params[2];
            String title = params[3];
            String message =params[4];
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
                        //newOrderResponse = client.newOrder(marketBuy(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));
                        String strTime = "";
                        //String strCoin = "";
                        String strGiaMua = "GIA_MUA";
                        String strGiaBan = "GIA_BAN";
                        String strTimeBan = "TIME_BAN";
                        String strProfit = "PROFIT";
                        String strExchange = "";

                        if (title.contains("***")) {
                            strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
                            strCoin = title.substring(title.lastIndexOf("BNB") + 3, title.indexOf("BUYYY") - 1);
                            strCoin = strCoin.trim();

                            strTime = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();

                        }
                        strGiaMua = message.substring(message.indexOf("PRICE: ") + 7);
                        String content = strTime + "|" + strCoin + "|" + strGiaMua + "|" + strGiaBan
                                + "|" + strTimeBan + "|" + strProfit + "|" + "Binance"+"|"+ numberF+""+"|"+"0";
                        ghiFileBot(content);
                    } else if (BUYSELL.equals("SELL")) {
                        //newOrderResponse = client.newOrder(marketSell(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));
                        String strTime = "";
                        //String strCoin = "";
                        String strGiaMua = "GIA_MUA";
                        String strGiaBan = "GIA_BAN";
                        String strTimeBan = "TIME_BAN";
                        String strProfit = "PROFIT";
                        String strExchange = "";

                        if (title.contains("StopLoss")) {
                            strCoin = title.substring(title.lastIndexOf("StopLoss") + 8, title.indexOf("***") - 1);
                            strCoin = strCoin.trim();
                        }else{
                            strCoin = title.substring(title.lastIndexOf("TakeProfit") + 10, title.indexOf("***") - 1);
                            strCoin = strCoin.trim();
                        }
                        strGiaBan = message.substring(message.indexOf(":") + 6, message.indexOf(":") + 16);
                        strGiaBan = strGiaBan.trim();
                        strTimeBan = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
                        strProfit = "+" + message.substring(message.lastIndexOf(":") + 1, message.lastIndexOf("</b>")).trim();
                        strGiaMua = message.substring(message.indexOf("Buy:") + 9, message.indexOf("Buy") + 19).trim();
                         strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
                        String content = strTime + "|" + strCoin + "|" + strGiaMua + "|" + strGiaBan
                                + "|" + strTimeBan + "|" + strProfit + "|" + "Binance"+"|"+"0"+"|"+ numberF+"";
                        ghiFileBot(content);
                    }
                }else{
                    // not buy sell

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