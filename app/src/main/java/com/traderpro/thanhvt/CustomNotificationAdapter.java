package com.traderpro.thanhvt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.squareup.picasso.Picasso;
import com.traderpro.GCM.Config;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.binance.api.client.domain.account.NewOrder.marketSell;

public class CustomNotificationAdapter extends ArrayAdapter<NotificationEntity> {

    Context mContext;
    List<NotificationEntity> lstOrder;
    BittrexData bittrexData;
    int nam;
    int thang;
    int ngay;
    boolean coMuaBan = false;
    String strTradeID = "";
    String strGiaSan = "";
    NewOrderResponse newOrderResponse;
    int numberSell = 0;
    int mResource;
    String strNN;
    String strErr = "";
//    private class DownloadAsyncTask extends AsyncTask<NotiHolder, Void, NotiHolder> {
//
//        NotificationEntity p;
//
//        public DownloadAsyncTask() {
//        }
//
//        public DownloadAsyncTask(NotificationEntity p) {
//            this.p = p;
//        }
//
//        @Override
//        protected NotiHolder doInBackground(NotiHolder... params) {
//            // TODO Auto-generated method stub
//            //load image directly
//            NotiHolder viewHolder = params[0];
//            try {
//                String[] mTime = p.strTime.split(":");
//                Calendar c = Calendar.getInstance();
//                c.set(nam, thang - 1, ngay, Integer.parseInt(mTime[0]), Integer.parseInt(mTime[1]));
//                Double pMax = 0D;
//                if (viewHolder.txtExchange.getText().toString().contains("Binance")) {
//                    pMax = getGiaMaxBinance(p.strCoin, c.getTimeInMillis() + "");
//                } else {
//                    pMax = getGiaMaxBittrex(p.strCoin, c.getTimeInMillis() + "");
//                }
//                Double profit = 0D;
//                Double dGia = p.strGia.length() > 0 ? Double.parseDouble(p.strGia) : 0D;
//                if (pMax != 0 && pMax > dGia) {
//                    profit = ((pMax - dGia) / dGia) * 100;
//                }
//                viewHolder.pMax = pMax;
//                viewHolder.pProfit = profit;
//
//            } catch (Exception e) {
//                // TODO: handle exception
//                Log.e("CustomNotificationAdapter", "Downloading Failed " + e.getMessage());
//                e.printStackTrace();
//
//            }
//
//            return viewHolder;
//        }
//
//        @Override
//        protected void onPostExecute(NotiHolder result) {
//            // TODO Auto-generated method stub
//            result.txtPrEx.setText(p.strGia + "/" + String.format("%.8f", result.pMax) + " | " + String.format("%.1f", result.pProfit) + "%");
//
//        }
//    }

    public CustomNotificationAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mContext = context;
    }

    public CustomNotificationAdapter(Context context, int resource, List<NotificationEntity> items, BittrexData bittrexDatap) {
        super(context, resource, items);
        this.mContext = context;
        this.lstOrder = items;
        this.bittrexData = bittrexData;

    }

    public CustomNotificationAdapter(Context context, int resource, List<NotificationEntity> items, BittrexData bittrexDatap, int nam, int thang, int ngay) {
        super(context, resource, items);
        this.mContext = context;
        this.lstOrder = items;
        this.bittrexData = bittrexData;
        this.nam = nam;
        this.thang = thang;
        this.ngay = ngay;
        this.mResource = resource;

        SharedPreferences pref = mContext.getSharedPreferences(Config.NGON_NGU, 0);
        strNN = pref.getString("NN", "VN");
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        NotiHolder viewHolder;
        if (view == null) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(getContext());

            view = mInflater.inflate(mResource, parent, false);

            viewHolder = new NotiHolder();

            viewHolder.txtExchange = (TextView) view.findViewById(R.id.txtExchange);
            viewHolder.txtTime = (TextView) view.findViewById(R.id.txtTime);
            viewHolder.txtPriceDetected = (TextView) view.findViewById(R.id.txtPriceDetected);
            viewHolder.txtPrEx = (TextView) view.findViewById(R.id.txtPr);
            viewHolder.txtProfit = (TextView) view.findViewById(R.id.txtProfit);
            viewHolder.txtVolumeTang = (TextView) view.findViewById(R.id.txtVolumeTang);
            viewHolder.txtVolumeGoc = (TextView) view.findViewById(R.id.txtVolumeGoc);

            viewHolder.txtU = (TextView) view.findViewById(R.id.txtU);
            viewHolder.txtVolDec = (TextView) view.findViewById(R.id.txtVolumeDected);
            viewHolder.txtPriceMax = (TextView) view.findViewById(R.id.txtPriceMax);
            viewHolder.txtCoin = (TextView) view.findViewById(R.id.txtCoin);
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.imgCoin);
            viewHolder.txtGiaHT = (TextView) view.findViewById(R.id.txtGiaHT);
            viewHolder.mImageTrade = (ImageView) view.findViewById(R.id.imgTrade);
//            if (mResource == R.layout.layout_notificustom) {
//                viewHolder.txtBS = (TextView) view.findViewById(R.id.txtBS);
//                viewHolder.txtTM = (TextView) view.findViewById(R.id.txtTM);
//            }

            view.setTag(viewHolder);
        } else {
            viewHolder = (NotiHolder) view.getTag();
        }


        NotificationEntity p = getItem(position);
        Double profit = 0D;
        Double dGia = p.strGia.length() > 0 ? Double.parseDouble(p.strGia) : 0D;
        if (p.strGiaMax != 0 && p.strGiaMax > dGia) {
            profit = ((p.strGiaMax - dGia) / dGia) * 100;
        }


        if (strNN.equalsIgnoreCase("VN")) {
            viewHolder.txtCoin.setText(p.strCoin);
            viewHolder.txtExchange.setText("Sàn: " + p.strExchange + " |" + p.strCase + "|");
            viewHolder.txtTime.setText("Time: " + p.strTime);
            viewHolder.txtPriceDetected.setText("Mua: " + p.strGia);
            viewHolder.txtPrEx.setText("Max: " + String.format("%.8f", p.strGiaMax));

            viewHolder.txtProfit.setText("Lãi: " + String.format("%.1f", profit) + "%");
            Double tangSoLan = (Double.parseDouble(p.strVol) / Double.parseDouble(p.strVolTB)) * 100;
            viewHolder.txtVolumeTang.setText("▲ " + String.format("%.1f", tangSoLan) + "%");
            viewHolder.txtGiaHT.setText(p.strGiaHienTai == null ? "" : String.format("%.8f", p.strGiaHienTai));

            viewHolder.txtVolumeGoc.setText("AvgV: " + p.strVolTB);

//            if (mResource == R.layout.layout_notificustom) {
//                viewHolder.txtBS.setText("Người mua/người bán: " + p.strBuySell);
//                viewHolder.txtTM.setText("Lệnh khớp/lệnh đặt: " + p.strTakerMaker);
//            }

//            viewHolder.txtU.⬆️("Giá báo:");
//            viewHolder.txtPriceMax.setText("Kỳ vọng:");
            viewHolder.txtVolDec.setText("Vol: " + p.strVol);
        } else {
            viewHolder.txtCoin.setText(p.strCoin);
            viewHolder.txtExchange.setText("Exchange: " + p.strExchange + " |" + p.strCase + "|");
            viewHolder.txtTime.setText("Time: " + p.strTime);
            viewHolder.txtPriceDetected.setText("Buy: " + p.strGia);
            viewHolder.txtPrEx.setText("Max: " + String.format("%.8f", p.strGiaMax));

            viewHolder.txtProfit.setText("Gain: " + String.format("%.1f", profit) + "%");
            Double tangSoLan = (Double.parseDouble(p.strVol) / Double.parseDouble(p.strVolTB)) * 100;
            viewHolder.txtVolumeTang.setText("▲ " + String.format("%.1f", tangSoLan) + "%");
            viewHolder.txtGiaHT.setText(p.strGiaHienTai == null ? "" : String.format("%.8f", p.strGiaHienTai));

            viewHolder.txtVolumeGoc.setText("AvgV: " + p.strVolTB);

//            if (mResource == R.layout.layout_notificustom) {
//                viewHolder.txtBS.setText("Number of Buyer/Seller: " + p.strBuySell);
//                viewHolder.txtTM.setText("Number of Taker/Maker: " + p.strTakerMaker);
//            }


//            viewHolder.txtU.setText("Quotation:");
//            viewHolder.txtPriceMax.setText("Expected Price:");
            viewHolder.txtVolDec.setText("Volume: " + p.strVol);
        }
        if (p.strGiaHienTai != null && p.strGiaHienTai >= dGia) {
            viewHolder.txtGiaHT.setTextColor(Color.parseColor("#27f546"));
        } else if (p.strGiaHienTai != null && p.strGiaHienTai < dGia && dGia > 0 && p.strGiaHienTai > 0) {
            viewHolder.txtGiaHT.setTextColor(Color.parseColor("#ff0000"));
        }
        if (p.strBuySell.contains("BUYY")) {
            viewHolder.mImageTrade.setVisibility(View.VISIBLE);
            viewHolder.mImageTrade.setImageResource(R.drawable.sellicon);
        } else if (p.strBuySell.contains("SELL")) {
            viewHolder.mImageTrade.setVisibility(View.VISIBLE);
            viewHolder.mImageTrade.setImageResource(R.drawable.sell);
        } else {
            viewHolder.mImageTrade.setVisibility(View.GONE);
        }
        viewHolder.mImageTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (p.strBuySell.equalsIgnoreCase("SELL")) return;
                    String PUBLIC_KEY = "";
                    String PRIVATE_KEY = "";
                    SharedPreferences pref = getContext().getSharedPreferences(Config.BOT_API, 0);
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
                            File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_notification.txt");
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
                    coMuaBan = false;
                    if (pref != null) {
                        int API = pref.getInt("USE_API", 0);
                        if (API == 1) {

                            PUBLIC_KEY = pref.getString("BIN_PUB", "");
                            PRIVATE_KEY = pref.getString("BIN_PRI", "");
                        }
                    }
                    if (PUBLIC_KEY != "" && PRIVATE_KEY != "") {
                        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(PUBLIC_KEY, PRIVATE_KEY);
                        BinanceApiRestClient client = factory.newRestClient();
                        try {
                            numberSell = 0;
                            for (int i = 0; i < lstObjectBuy.size(); i++) {
                                String itemBuy = lstObjectBuy.get(i);
                                if (!itemBuy.equalsIgnoreCase("")) {
                                    String objs[] = itemBuy.split(" ");
                                    if (objs.length >= 15 && objs[14].equals(p.strId)) {
                                        if (objs[17].contains("."))
                                            numberSell = Integer.parseInt(objs[17].substring(0, objs[17].indexOf(".")));
                                        else numberSell = Integer.parseInt(objs[17]);
                                    }
                                }
                            }

                            newOrderResponse = new NewOrderResponse();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // Do network action in this function
                                    try {
                                        Log.d("Handler sell ", numberSell + "");
                                        newOrderResponse = client.newOrder(marketSell(p.strCoin + "BTC", "" + numberSell).newOrderRespType(NewOrderResponseType.FULL));
                                        coMuaBan = true;
                                        strTradeID = newOrderResponse.getOrderId() + "";
                                        Double dCum = Double.parseDouble(newOrderResponse.getCummulativeQuoteQty());
                                        Double dSL = Double.parseDouble(newOrderResponse.getExecutedQty());
                                        Double dbGiaSan = dCum / dSL;
                                        strGiaSan = String.format("%.8f", dbGiaSan);
                                        strGiaSan = strGiaSan.replace(",", ".");
                                        strErr = "OK";
                                        if (strErr.equalsIgnoreCase("OK")) {
                                            Handler handler = new Handler(mContext.getMainLooper());
                                            handler.post(new Runnable() {
                                                public void run() {
                                                    viewHolder.mImageTrade.setImageResource(R.drawable.sell);
                                                    Toast.makeText(mContext, "Sell success " + newOrderResponse.getExecutedQty() + " " + p.strCoin + " !!!", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            for (int i = 0; i < lstObjectBuy.size(); i++) {
                                                String itemBuy = lstObjectBuy.get(i);
                                                if (!itemBuy.equalsIgnoreCase("")) {
                                                    String objs[] = itemBuy.split(" ");
                                                    if (objs.length >= 15 && objs[14].equals(p.strId)) {
                                                        if (itemBuy.contains("BUYY")) {

                                                            itemBuy = itemBuy.replace(objs[15], "SELL");
                                                            itemBuy = itemBuy.replace(objs[16], strTradeID);
                                                            itemBuy = itemBuy.replace(objs[17], newOrderResponse.getExecutedQty());
                                                            itemBuy = itemBuy.replace(objs[18], strGiaSan);
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
                                            p.isSellNow = false;

                                        } else {
                                            Handler handler = new Handler(mContext.getMainLooper());
                                            handler.post(new Runnable() {
                                                public void run() {

                                                    Toast.makeText(mContext, "Sell error " + strErr, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        strErr = e.getMessage();
                                        Handler handler = new Handler(mContext.getMainLooper());
                                        handler.post(new Runnable() {
                                            public void run() {

                                                Toast.makeText(mContext, "Sell error " + strErr, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        e.printStackTrace();
                                    }

                                }
                            }).start();

                        } catch (final Exception e) {
                            //Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            // Toast thong bao de nghi kiem tra lai xem du BTC de giao dich khong, hoạc API da chinh xac chua
                            Handler handler = new Handler(getContext().getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
//                                        Toast.makeText(mContext, "Amount of BTC not enough to trade or check your API!!!", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getContext(), e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getContext(), "Enter public key and private key !!!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Handler handler = new Handler(mContext.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(mContext, e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        TraderUtils utils = new TraderUtils();
        Picasso.with(mContext).load(utils.IMAGE_URL + p.strImageURL + ".png").into(viewHolder.mImageView);
//        Picasso.Builder builder = new Picasso.Builder(mContext);
//        builder.downloader(new OkHttp3Downloader(mContext));
//        Log.e("Adapter", utils.IMAGE_URL + p.strImageURL + ".png");
//        builder.build().load(utils.IMAGE_URL + p.strImageURL + ".png")
//                .placeholder((R.drawable.defaultcoin))
//                .error(R.drawable.defaultcoin)
//                .into(viewHolder.mImageView);
        this.notifyDataSetChanged();
//        if (p.strExchange.trim().contains("Bittrex")) {
//            viewHolder.txtExchange.setTextColor(Color.parseColor("#00FFFF"));
//            viewHolder.txtTime.setTextColor(Color.parseColor("#00FFFF"));
//            viewHolder.txtU.setTextColor(Color.parseColor("#00FFFF"));
//            viewHolder.txtVolDec.setTextColor(Color.parseColor("#00FFFF"));
//            viewHolder.txtVolumeTang.setTextColor(Color.parseColor("#00FFFF"));
//            viewHolder.txtPriceMax.setTextColor(Color.parseColor("#00FFFF"));
//
//            if (mResource == R.layout.layout_notificustom) {
//                viewHolder.txtBS.setTextColor(Color.parseColor("#00FFFF"));
//                viewHolder.txtTM.setTextColor(Color.parseColor("#00FFFF"));
//            }
//        } else {
//            viewHolder.txtExchange.setTextColor(Color.parseColor("#FFC125"));
//            viewHolder.txtTime.setTextColor(Color.parseColor("#FFC125"));
//            viewHolder.txtU.setTextColor(Color.parseColor("#FFC125"));
//            viewHolder.txtVolDec.setTextColor(Color.parseColor("#FFC125"));
//            viewHolder.txtVolumeTang.setTextColor(Color.parseColor("#FFC125"));
//            viewHolder.txtPriceMax.setTextColor(Color.parseColor("#FFC125"));
//
//            if (mResource == R.layout.layout_notificustom) {
//                viewHolder.txtBS.setTextColor(Color.parseColor("#FFC125"));
//                viewHolder.txtTM.setTextColor(Color.parseColor("#FFC125"));
//            }
//        }
//        new DownloadAsyncTask(p).execute(viewHolder);


        return view;
    }

    static class NotiHolder {
        TextView txtExchange;
        TextView txtTime;
        TextView txtPriceDetected;
        TextView txtPrEx;
        TextView txtProfit;
        TextView txtVolumeTang;
        TextView txtVolumeGoc;
        TextView txtU;
        TextView txtVolDec;
        TextView txtPriceMax;
        TextView txtBS;
        TextView txtTM;
        TextView txtCoin;
        Double pMax;
        Double pProfit;
        ImageView mImageView;
        ImageView mImageTrade;
        TextView txtGiaHT;
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

    public Double getGiaMaxBittrex(String strCoin, String strTime) {
        Double giaMax = 0D;
        try {
            long timeNow = System.currentTimeMillis() - 7 * 60 * 60 * 1000;
            long startTime = Long.parseLong(strTime) - 7 * 60 * 60 * 1000;
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (timeNow - startTime < 12 * 60 * 60 * 1000) {
                bittrexData.set(BittrexAPI.getMarketHistory("BTC-" + strCoin, "fivemin"));
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
            } else {
                bittrexData.set(BittrexAPI.getMarketHistory("BTC-" + strCoin, "hour"));
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
            }
        } catch (Exception e) {

        }
        Log.e("Gia MAX Bittrex", strCoin + ": " + String.format("%.8f", giaMax));
        return giaMax;
    }

    public Double getGiaMaxBinance(String strCoin, String strTime) {
        Double giaMax = 0D;
        try {

            long timeNow = System.currentTimeMillis() - 7 * 60 * 60 * 1000;
            long startTime = Long.parseLong(strTime);
            startTime = startTime - 7 * 60 * 60 * 1000;
            strTime = startTime + "";

            // Neu gio hien tai moi hon gio bat dau mua chua toi 60', thi query theo 3' de lay ra gia tri max
            if (timeNow - startTime < 60 * 60 * 1000) {
                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + strTime;
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                giaMax = subGetGiaMax(jsonArr);
            } else if (timeNow - startTime < 24 * 60 * 60 * 1000) {
                // 23H theo 1H
                // 1H cuoi theo 3m
                Calendar rightNow = Calendar.getInstance();
                int min = rightNow.get(Calendar.MINUTE);
                rightNow.add(Calendar.MINUTE, -min);
                rightNow.add(Calendar.HOUR_OF_DAY, -7);

                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + rightNow.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                Double giaMax1 = subGetGiaMax(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + strTime + "&endTime=" + rightNow.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax2 = subGetGiaMax(jsonArr);

                giaMax = giaMax1 > giaMax2 ? giaMax1 : giaMax2;

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
                startHour.add(Calendar.HOUR_OF_DAY, -7);

                String mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=3m&startTime=" + startHour.getTimeInMillis();
                String res = readJsonFromUrl(mUrl);
                JSONArray jsonArr = new JSONArray(res);
                Double giaMax1 = subGetGiaMax(jsonArr);

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
                startDay.add(Calendar.HOUR_OF_DAY, -hour - 7);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1h&startTime=" + startDay.getTimeInMillis() + "&endTime=" + startHour.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax2 = subGetGiaMax(jsonArr);

                mUrl = "https://api.binance.com/api/v1/klines?symbol=" + strCoin + "BTC&interval=1d&startTime=" + startTime + "&endTime=" + startDay.getTimeInMillis();
                res = readJsonFromUrl(mUrl);
                jsonArr = new JSONArray(res);
                Double giaMax3 = subGetGiaMax(jsonArr);

                giaMax = Math.max(Math.max(giaMax1, giaMax2), giaMax3);
//                giaMax = giaMax1 > giaMax2 ? (giaMax1 > giaMax3 ? giaMax1 : giaMax3) : giaMax2;
//                int max =  Math.max(Math.max(x,y),z);
//                           Math.min(Math.min(a, b), c);
            }
        } catch (Exception e) {
        }
        Log.e("Gia MAX Binance", strCoin + ": " + String.format("%.8f", giaMax));
        return giaMax;
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

}
