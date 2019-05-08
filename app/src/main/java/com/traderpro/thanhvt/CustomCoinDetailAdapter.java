package com.traderpro.thanhvt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.binance.api.client.domain.account.NewOrderResponse;

import java.util.ArrayList;
import java.util.List;

public class CustomCoinDetailAdapter extends ArrayAdapter<NotificationEntity> implements Filterable {

    Context mContext;
    List<NotificationEntity> lstOrder, filterList;
    boolean coMuaBan = false;
    String strTradeID = "";
    String strGiaSan = "";
    NewOrderResponse newOrderResponse;
    int numberSell = 0;
    int mResource;
    String strNN;
    String strErr = "";
    private ContactFilter filter;

    public CustomCoinDetailAdapter(Context context, int resource, List<NotificationEntity> items) {
        super(context, resource, items);
        this.mContext = context;
        this.lstOrder = items;
        this.mResource = resource;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        strNN = sharedPrefs.getBoolean("NN", true) == true ? "VN" : "EN";

        filterList = new ArrayList<>();
        this.filterList.addAll(lstOrder);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
            viewHolder.txtGiaHT = (TextView) view.findViewById(R.id.txtGiaHT);

            viewHolder.txtGia5 = (TextView) view.findViewById(R.id.txtGiaTime5);
            viewHolder.txtGia30 = (TextView) view.findViewById(R.id.txtGiaTime30);
            viewHolder.txtGia1 = (TextView) view.findViewById(R.id.txtGiaTime1);
            viewHolder.txtGia2 = (TextView) view.findViewById(R.id.txtGiaTime2);
            viewHolder.txtGia4 = (TextView) view.findViewById(R.id.txtGiaTime4);
            viewHolder.txtVol5 = (TextView) view.findViewById(R.id.txtVolTime5);
            viewHolder.txtVol30 = (TextView) view.findViewById(R.id.txtVolTime30);
            viewHolder.txtVol1 = (TextView) view.findViewById(R.id.txtVolTime1);
            viewHolder.txtVol2 = (TextView) view.findViewById(R.id.txtVolTime2);
            viewHolder.txtVol4 = (TextView) view.findViewById(R.id.txtVolTime4);

//            viewHolder.txtCoin = (TextView) view.findViewById(R.id.txtCoin);
//            viewHolder.mImageView = (ImageView) view.findViewById(R.id.imgCoin);
//            viewHolder.mImageTrade = (ImageView) view.findViewById(R.id.imgTrade);
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
//            viewHolder.txtCoin.setText(p.strCoin);
            viewHolder.txtExchange.setText("|" + p.strCase + "|");
            viewHolder.txtTime.setText("Time: " + p.strTime);
            viewHolder.txtPriceDetected.setText("Mua: " + p.strGia);
            viewHolder.txtPrEx.setText("Max: " + String.format("%.8f", p.strGiaMax));

            viewHolder.txtProfit.setText("Lãi: " + String.format("%.1f", profit) + "%");
            Double tangSoLan = (Double.parseDouble(p.strVol) / Double.parseDouble(p.strVolTB)) * 100;
            viewHolder.txtVolumeTang.setText("▲ " + String.format("%.1f", tangSoLan) + "%");

            Double dLaiVsHT = 0D;
            if (p.strGiaHienTai != null && p.strGiaHienTai > 0) {
                dLaiVsHT = ((p.strGiaHienTai - dGia) / dGia) * 100;
                viewHolder.txtGiaHT.setText(p.strGiaHienTai == null ? "" : (String.format("%.8f", p.strGiaHienTai) + " (" + String.format("%.1f", dLaiVsHT) + "%)"));
            }
            if (dLaiVsHT != 0) {

            }
            viewHolder.txtVolumeGoc.setText("Avg: " + p.strVolTB);

            viewHolder.txtVolDec.setText("Vol: " + p.strVol);
        } else {
//            viewHolder.txtCoin.setText(p.strCoin);
//            viewHolder.txtExchange.setText("Exchange: " + p.strExchange + " |" + p.strCase + "|");
            viewHolder.txtTime.setText("Time: " + p.strTime);
            viewHolder.txtPriceDetected.setText("Buy: " + p.strGia);
            viewHolder.txtPrEx.setText("Max: " + String.format("%.8f", p.strGiaMax));

            viewHolder.txtProfit.setText("Gain: " + String.format("%.1f", profit) + "%");
            Double tangSoLan = (Double.parseDouble(p.strVol) / Double.parseDouble(p.strVolTB)) * 100;
            viewHolder.txtVolumeTang.setText("▲ " + String.format("%.1f", tangSoLan) + "%");

            Double dLaiVsHT = 0D;
            if (p.strGiaHienTai != null && p.strGiaHienTai > 0) {
                dLaiVsHT = ((p.strGiaHienTai - dGia) / dGia) * 100;
                viewHolder.txtGiaHT.setText(p.strGiaHienTai == null ? "" : (String.format("%.8f", p.strGiaHienTai) + " (" + String.format("%.1f", dLaiVsHT) + "%)"));
            }
            if (dLaiVsHT != 0) {

            }
            viewHolder.txtVolumeGoc.setText("Avg: " + p.strVolTB);

            viewHolder.txtVolDec.setText("Volume: " + p.strVol);
        }
        if (p.strGiaHienTai != null && p.strGiaHienTai >= dGia) {
            viewHolder.txtGiaHT.setTextColor(Color.parseColor("#27f546"));
        } else if (p.strGiaHienTai != null && p.strGiaHienTai < dGia && dGia > 0 && p.strGiaHienTai > 0) {
            viewHolder.txtGiaHT.setTextColor(Color.parseColor("#ff0000"));
        }
//        if (p.strBuySell.contains("BUYY")) {
//            viewHolder.mImageTrade.setVisibility(View.VISIBLE);
//            viewHolder.mImageTrade.setImageResource(R.drawable.sellicon);
//        } else if (p.strBuySell.contains("SELL")) {
//            viewHolder.mImageTrade.setVisibility(View.VISIBLE);
//            viewHolder.mImageTrade.setImageResource(R.drawable.sell);
//        } else {
//            viewHolder.mImageTrade.setVisibility(View.GONE);
//        }
//        viewHolder.mImageTrade.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (p.strBuySell.equalsIgnoreCase("SELL")) return;
//                    String PUBLIC_KEY = "";
//                    String PRIVATE_KEY = "";
//                    SharedPreferences pref = getContext().getSharedPreferences(Config.BOT_API, 0);
//                    Calendar rightNow = Calendar.getInstance();
//                    int nam = rightNow.get(Calendar.YEAR);
//                    int thang = rightNow.get(Calendar.MONTH) + 1;
//                    int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
//                    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
//                    int min = rightNow.get(Calendar.MINUTE);
//
//                    File folder = new File(Environment.getExternalStorageDirectory() +
//                            File.separator + "TraderPro");
//                    boolean success = true;
//                    if (!folder.exists()) {
//                        success = folder.mkdirs();
//                    }
//                    List<String> lstObjectBuy = new ArrayList<>();
//                    File myFile = new File(Environment.getExternalStorageDirectory() +
//                            File.separator + "TraderPro" + File.separator + nam + thang + ngay + "_notification.txt");
//                    if (!myFile.exists()) {
//                        myFile.createNewFile();
//                    }
//                    FileInputStream fIn = new FileInputStream(myFile);
//                    BufferedReader myReader = new BufferedReader(
//                            new InputStreamReader(fIn));
//                    String aDataRow = "";
//                    while ((aDataRow = myReader.readLine()) != null) {
//                        lstObjectBuy.add(aDataRow);
//                    }
//                    myReader.close();
//                    coMuaBan = false;
//                    if (pref != null) {
//                        int API = pref.getInt("USE_API", 0);
//                        if (API == 1) {
//
//                            PUBLIC_KEY = pref.getString("BIN_PUB", "");
//                            PRIVATE_KEY = pref.getString("BIN_PRI", "");
//                        }
//                    }
//                    if (PUBLIC_KEY != "" && PRIVATE_KEY != "") {
//                        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(PUBLIC_KEY, PRIVATE_KEY);
//                        BinanceApiRestClient client = factory.newRestClient();
//                        try {
//                            numberSell = 0;
//                            for (int i = 0; i < lstObjectBuy.size(); i++) {
//                                String itemBuy = lstObjectBuy.get(i);
//                                if (!itemBuy.equalsIgnoreCase("")) {
//                                    String objs[] = itemBuy.split(" ");
//                                    if (objs.length >= 15 && objs[14].equals(p.strId)) {
//                                        if (objs[17].contains("."))
//                                            numberSell = Integer.parseInt(objs[17].substring(0, objs[17].indexOf(".")));
//                                        else numberSell = Integer.parseInt(objs[17]);
//                                    }
//                                }
//                            }
//
//                            newOrderResponse = new NewOrderResponse();
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        Log.d("Handler sell ", numberSell + "");
//                                        newOrderResponse = client.newOrder(marketSell(p.strCoin + "BTC", "" + numberSell).newOrderRespType(NewOrderResponseType.FULL));
//                                        coMuaBan = true;
//                                        strTradeID = newOrderResponse.getOrderId() + "";
//                                        Double dCum = Double.parseDouble(newOrderResponse.getCummulativeQuoteQty());
//                                        Double dSL = Double.parseDouble(newOrderResponse.getExecutedQty());
//                                        Double dbGiaSan = dCum / dSL;
//                                        strGiaSan = String.format("%.8f", dbGiaSan);
//                                        strGiaSan = strGiaSan.replace(",", ".");
//                                        strErr = "OK";
//                                        if (strErr.equalsIgnoreCase("OK")) {
//                                            Handler handler = new Handler(mContext.getMainLooper());
//                                            handler.post(new Runnable() {
//                                                public void run() {
//                                                    viewHolder.mImageTrade.setImageResource(R.drawable.sell);
//                                                    Toast.makeText(mContext, "Sell success " + newOrderResponse.getExecutedQty() + " " + p.strCoin + " !!!", Toast.LENGTH_LONG).show();
//                                                }
//                                            });
//                                            for (int i = 0; i < lstObjectBuy.size(); i++) {
//                                                String itemBuy = lstObjectBuy.get(i);
//                                                if (!itemBuy.equalsIgnoreCase("")) {
//                                                    String objs[] = itemBuy.split(" ");
//                                                    if (objs.length >= 15 && objs[14].equals(p.strId)) {
//                                                        if (itemBuy.contains("BUYY")) {
//
//                                                            itemBuy = itemBuy.replace(objs[15], "SELL");
//                                                            itemBuy = itemBuy.replace(objs[16], strTradeID);
//                                                            itemBuy = itemBuy.replace(objs[17], newOrderResponse.getExecutedQty());
//                                                            itemBuy = itemBuy.replace(objs[18], strGiaSan);
//                                                            lstObjectBuy.set(i, itemBuy);
//                                                        } else {
//                                                            // Toast thong bao ban chua mua coin nay nen chua the bankey
//                                                            handler = new Handler(mContext.getMainLooper());
//                                                            handler.post(new Runnable() {
//                                                                public void run() {
//                                                                    Toast.makeText(mContext, "Can not sell because you didn't buy it!!!", Toast.LENGTH_LONG).show();
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                            if (coMuaBan == true) {
//                                                FileOutputStream fOut = new FileOutputStream(myFile, false);
//                                                OutputStreamWriter myOutWriter =
//                                                        new OutputStreamWriter(fOut);
//                                                for (int i = 0; i < lstObjectBuy.size(); i++) {
//                                                    String itemBuy = lstObjectBuy.get(i);
//                                                    if (!itemBuy.equals("")) {
//                                                        myOutWriter.append("\n" + itemBuy);
//                                                    }
//                                                }
//                                                myOutWriter.close();
//                                                fOut.close();
//                                            }
//                                            p.isSellNow = false;
//
//                                        } else {
//                                            Handler handler = new Handler(mContext.getMainLooper());
//                                            handler.post(new Runnable() {
//                                                public void run() {
//
//                                                    Toast.makeText(mContext, "Sell error " + strErr, Toast.LENGTH_LONG).show();
//                                                }
//                                            });
//                                        }
//                                    } catch (Exception e) {
//                                        strErr = e.getMessage();
//                                        Handler handler = new Handler(mContext.getMainLooper());
//                                        handler.post(new Runnable() {
//                                            public void run() {
//
//                                                Toast.makeText(mContext, "Sell error " + strErr, Toast.LENGTH_LONG).show();
//                                            }
//                                        });
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }).start();
//
//                        } catch (final Exception e) {
//                            e.printStackTrace();
//                            Handler handler = new Handler(getContext().getMainLooper());
//                            handler.post(new Runnable() {
//                                public void run() {
//                                    Toast.makeText(getContext(), e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//
//                    } else {
//                        Toast.makeText(getContext(), "Enter public key and private key !!!", Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Handler handler = new Handler(mContext.getMainLooper());
//                    handler.post(new Runnable() {
//                        public void run() {
//                            Toast.makeText(mContext, e.getMessage() + " !!!", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//        });

//        TraderUtils utils = new TraderUtils();
//        Picasso.with(mContext).load(utils.IMAGE_URL + p.strImageURL + ".png").into(viewHolder.mImageView);
        this.notifyDataSetChanged();
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ContactFilter();
        return filter;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
        //        TextView txtCoin;
        Double pMax;
        Double pProfit;
        //        ImageView mImageView;
//        ImageView mImageTrade;
        TextView txtGiaHT;

        TextView txtGia5;
        TextView txtGia30;
        TextView txtGia1;
        TextView txtGia2;
        TextView txtGia4;

        TextView txtVol5;
        TextView txtVol30;
        TextView txtVol1;
        TextView txtVol2;
        TextView txtVol4;

    }

    private class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String data = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            if (data.length() > 0) {
                List<NotificationEntity> filteredList = new ArrayList<>(filterList);
                List<NotificationEntity> nList = new ArrayList<>();
                int count = filteredList.size();
                for (int i = 0; i < count; i++) {
                    NotificationEntity item = filteredList.get(i);
                    String name = item.strCoin.toLowerCase();
                    String strCase = item.strCase;
                    if (data.startsWith(".")) {
                        String mCase = data.substring(1);
                        if (mCase.equals(strCase)) nList.add(item);

                    } else if (name.equalsIgnoreCase(data) || name.startsWith(data))
                        nList.add(item);
                }
                results.count = nList.size();
                results.values = nList;
            } else {
                List<NotificationEntity> list = new ArrayList<>(filterList);
                results.count = list.size();
                results.values = list;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lstOrder = (ArrayList<NotificationEntity>) results.values;
            clear();
            for (int i = 0; i < lstOrder.size(); i++) {
                NotificationEntity item = (NotificationEntity) lstOrder.get(i);
                add(item);
                notifyDataSetChanged();
            }
        }
    }

}
