package com.traderpro.thanhvt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.traderpro.GCM.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;
import static com.binance.api.client.domain.account.NewOrder.marketSell;

public class BuyIntentReceiver extends BroadcastReceiver {
    public Context mContext;

    public final static String TAG = BuyIntentReceiver.class.toString();

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
        String strID = extras.getString("ID");
        Log.d(TAG, message);
        Log.d(TAG, strID);
        //int number = extras.getInt("PRICE");
        //Intent myIntent = new Intent(context, DetectSignalService.class);
        //context.startService(myIntent);
        //editor.putString("BIN_PUB", binPub);
        //editor.putString("BIN_PRI", binPri);
        //editor.putString("AMOUNT_BTC", amountBTC);
        new ExchangeSellBuy().execute(BUYSELL, strCoin, price, title, message, strID);

    }

    class ExchangeSellBuy extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String BUYSELL = params[0];
                final String strCoin = params[1];
                String price = params[2];
                String title = params[3];
                String message = params[4];
                String ID = params[5];
                String amountBTC = "";
                float number = 0;
                String PUBLIC_KEY = "";
                String PRIVATE_KEY = "";
                SharedPreferences pref = mContext.getSharedPreferences(Config.BOT_API, 0);

                /*
                Con thieu truong hop ngay hom trc bao, hom sau moi ban ra dc
                 */
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
                List<String> lstObjectBuy = new ArrayList<>();
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
                    lstObjectBuy.add(aDataRow);
                }
                myReader.close();


                // ---------------------------------------------------------------

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

                        boolean coMuaBan = false;

                        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(PUBLIC_KEY, PRIVATE_KEY);
                        BinanceApiRestClient client = factory.newRestClient();
                        if (BUYSELL.equals("BUY")) {
                            try {
                                final NewOrderResponse newOrderResponse = client.newOrder(marketBuy(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));
                                String strTradeID = newOrderResponse.getOrderId() + "";
                                Double dCum = Double.parseDouble(newOrderResponse.getCummulativeQuoteQty());
                                Double dSL = Double.parseDouble(newOrderResponse.getExecutedQty());
                                Double dbGiaSan = dCum / dSL;
                                String strGiaSan = String.format("%.8f", dbGiaSan);
                                strGiaSan = strGiaSan.replace(",", ".");
                                coMuaBan = true;
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, "Buy success " + newOrderResponse.getExecutedQty() + " " + strCoin + " !!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                for (int i = 0; i < lstObjectBuy.size(); i++) {
                                    String itemBuy = lstObjectBuy.get(i);
                                    if (!itemBuy.equalsIgnoreCase("")) {
                                        String objs[] = itemBuy.split("\\|");
                                        if (objs.length >= 8 && objs[7].equals(ID)) {
                                            itemBuy += "|" + "BUYY" + "|" + strTradeID + "|" + newOrderResponse.getExecutedQty() + "|" + strGiaSan;
                                            lstObjectBuy.set(i, itemBuy);
                                        }
                                    }
                                }
                            } catch (final Exception e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                // Toast thong bao de nghi kiem tra lai xem du BTC de giao dich khong, hoạc API da chinh xac chua
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
//                                        Toast.makeText(mContext, "Amount of BTC not enough to trade or check your API!!!", Toast.LENGTH_LONG).show();
                                        Toast.makeText(mContext, e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } else if (BUYSELL.equals("SELL")) {
                            try {
                                int numberSell = 0;
                                for (int i = 0; i < lstObjectBuy.size(); i++) {
                                    String itemBuy = lstObjectBuy.get(i);
                                    if (!itemBuy.equalsIgnoreCase("")) {
                                        String objs[] = itemBuy.split("\\|");
                                        if (objs.length >= 8 && objs[7].equals(ID)) {
                                            if (objs[10].contains("."))
                                                numberSell = Integer.parseInt(objs[10].substring(0, objs[10].indexOf(".")));
                                            else numberSell = Integer.parseInt(objs[10]);
                                        }
                                    }
                                }
                                final NewOrderResponse newOrderResponse = client.newOrder(marketSell(strCoin + "BTC", "" + numberSell).newOrderRespType(NewOrderResponseType.FULL));
                                String strTradeID = newOrderResponse.getOrderId() + "";
                                Double dCum = Double.parseDouble(newOrderResponse.getCummulativeQuoteQty());
                                Double dSL = Double.parseDouble(newOrderResponse.getExecutedQty());
                                Double dbGiaSan = dCum / dSL;
                                String strGiaSan = String.format("%.8f", dbGiaSan);
                                strGiaSan = strGiaSan.replace(",", ".");
                                coMuaBan = true;
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, "Sell success " + newOrderResponse.getExecutedQty() + " " + strCoin + " !!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                for (int i = 0; i < lstObjectBuy.size(); i++) {
                                    String itemBuy = lstObjectBuy.get(i);
                                    if (!itemBuy.equalsIgnoreCase("")) {
                                        String objs[] = itemBuy.split("\\|");
                                        if (objs.length >= 8 && objs[7].equals(ID)) {
                                            if (itemBuy.contains("BUYY")) {

                                                itemBuy = itemBuy.replace(objs[8], "SELL");
                                                itemBuy = itemBuy.replace(objs[9], strTradeID);
                                                itemBuy = itemBuy.replace(objs[10], newOrderResponse.getExecutedQty());

//                                            itemBuy.replace(objs[3], strGiaSan);
//                                            Double giaMua = Double.parseDouble(objs[11]);
//                                            if(dbGiaSan > giaMua){
//                                                Double PROFIT = ((dbGiaSan - giaMua) / giaMua) * 100;
//                                                itemBuy.replace(objs[3], strGiaSan);
//                                            }
                                                itemBuy = itemBuy.replace(objs[11], strGiaSan);
                                                lstObjectBuy.set(i, itemBuy);
                                            } else {
                                                // Toast thong bao ban chua mua coin nay nen chua the bankey
                                                handler = new Handler(mContext.getMainLooper());
                                                handler.post(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(mContext, "Can not sell because you didn't buy it!!!", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            } catch (final Exception e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                // Toast thong bao de nghi kiem tra lai xem du BTC de giao dich khong, hoạc API da chinh xac chua
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
//                                        Toast.makeText(mContext, "Amount of BTC not enough to trade or check your API!!!", Toast.LENGTH_LONG).show();
                                        Toast.makeText(mContext, e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } else if (BUYSELL.equals("SIMPLE")) {
                            try {
                                final NewOrderResponse newOrderResponse = client.newOrder(marketBuy(strCoin + "BTC", "" + number).newOrderRespType(NewOrderResponseType.FULL));
                                String strTradeID = newOrderResponse.getOrderId() + "";
                                Double dCum = Double.parseDouble(newOrderResponse.getCummulativeQuoteQty());
                                Double dSL = Double.parseDouble(newOrderResponse.getExecutedQty());
                                Double dbGiaSan = dCum / dSL;
                                String strGiaSan = String.format("%.8f", dbGiaSan);
                                strGiaSan = strGiaSan.replace(",", ".");
                                coMuaBan = true;
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, "Buy success " + newOrderResponse.getExecutedQty() + " " + strCoin + " !!!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } catch (final Exception e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                Handler handler = new Handler(mContext.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        if (coMuaBan == true) {
                            FileOutputStream fOut = new FileOutputStream(myFile, false);
                            OutputStreamWriter myOutWriter =
                                    new OutputStreamWriter(fOut);
                            for (int i = 0; i < lstObjectBuy.size(); i++) {
                                String itemBuy = lstObjectBuy.get(i);
                                if (!itemBuy.equals("")) {
                                    myOutWriter.append("\n" + itemBuy);
                                }
                            }
                            myOutWriter.close();
                            fOut.close();
                        }
                    } else {
                        // Hiển thị Toast báo cần cấu hình API
                        Handler handler = new Handler(mContext.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, "Config Binance API in Trade API Settings please!!!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            } catch (final Exception e) {
                Log.e(TAG + " ExSellBuy", e.getMessage());
                e.printStackTrace();
                Handler handler = new Handler(mContext.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }

    public void ghiFileLog(NewOrderResponse newOrderResponse, String ID) {
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
                    File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_notification.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            List<String> lstObjectBuy = new ArrayList<>();
            // Doc trc
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
                lstObjectBuy.add(aDataRow);
            }
            myReader.close();

            Double dCum = Double.parseDouble(newOrderResponse.getCummulativeQuoteQty());
            Double dSL = Double.parseDouble(newOrderResponse.getExecutedQty());
            Double dbGiaSan = dCum / dSL;
            String strGiaSan = String.format("%.8f", dbGiaSan);
            strGiaSan = strGiaSan.replace(",", ".");
            for (int i = 0; i < lstObjectBuy.size(); i++) {
                String itemBuy = lstObjectBuy.get(i);
                if (!itemBuy.equalsIgnoreCase("")) {
                    String objs[] = itemBuy.split(" ");
                    if (objs.length >= 15 && objs[14].equals(ID)) {
                        itemBuy += " " + "BUYY" + " " + newOrderResponse.getOrderId() + " " + newOrderResponse.getExecutedQty() + " " + strGiaSan;
                        lstObjectBuy.set(i, itemBuy);
                    }
                }
            }

            // Ghi sau
            FileOutputStream fOut = new FileOutputStream(myFile, false);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            for (int i = 0; i < lstObjectBuy.size(); i++) {
                String itemBuy = lstObjectBuy.get(i);
                if (!itemBuy.equals("")) {
                    myOutWriter.append("\n" + itemBuy);
                }
            }
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {

            Log.e("Err write log: ", e.getMessage());
        }
    }
}