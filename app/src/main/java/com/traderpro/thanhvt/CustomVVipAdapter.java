package com.traderpro.thanhvt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.traderpro.GCM.Config;

import java.util.List;

import static com.binance.api.client.domain.account.NewOrder.marketSell;

public class CustomVVipAdapter extends ArrayAdapter<NotificationEntity>{

    Context mContext;
    List<NotificationEntity> lstOrder;
    BittrexData bittrexData;
    int mResource;
    String strNN;
   public CustomVVipAdapter.NotiHolder viewHolder;
    public NotificationEntity p;

    public CustomVVipAdapter(Context context, int resource, List<NotificationEntity> items) {
        super(context, resource, items);
        this.mContext = context;
        this.lstOrder = items;
        this.bittrexData = bittrexData;
        this.mResource = resource;
        SharedPreferences pref = mContext.getSharedPreferences(Config.NGON_NGU, 0);
        strNN = pref.getString("NN", "VN");

        //viewHolder.btnSellNow = (Button) mContext.findViewById(R.id.btnSellNow);
    }

    public static class NotiHolder {
        TextView txtTimeMua;
        TextView txtGiaMua;
        TextView txtProfit;
        TextView txtGiaHienTai;
        TextView txtPriceBan;
        TextView txtTimeBan;
        TextView txtCoin;
        TextView txtText3;
        ImageView imgBuy;
        public Button btnSellNow;
        TextView txtText1;
        TextView txtText2;
        Double pMax;
        Double pProfit;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(getContext());

            view = mInflater.inflate(mResource, parent, false);


            viewHolder.txtGiaMua = (TextView) view.findViewById(R.id.txtGiaMua);
            viewHolder.txtTimeMua = (TextView) view.findViewById(R.id.txtTimeMua);
            viewHolder.txtGiaHienTai = (TextView) view.findViewById(R.id.txtGiaHienTai);
            viewHolder.txtPriceBan = (TextView) view.findViewById(R.id.txtGiaBan);
            viewHolder.txtProfit = (TextView) view.findViewById(R.id.txtProfit);
            viewHolder.txtTimeBan = (TextView) view.findViewById(R.id.txtTimeBan);
            viewHolder.txtCoin = (TextView) view.findViewById(R.id.txtCoin);
            viewHolder.txtText1 = (TextView) view.findViewById(R.id.txtText1);
            viewHolder.txtText2 = (TextView) view.findViewById(R.id.txtText2);
            viewHolder.txtText3 = (TextView) view.findViewById(R.id.txtText3);
            viewHolder.imgBuy = (ImageView) view.findViewById(R.id.imageBuy);
            viewHolder.btnSellNow = (Button) view.findViewById(R.id.btnSellNow);
            viewHolder = new CustomVVipAdapter.NotiHolder();
            viewHolder.btnSellNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String PUBLIC_KEY = "";
                    String PRIVATE_KEY = "";
                    SharedPreferences pref = getContext().getSharedPreferences(Config.BOT_API, 0);

                    if (pref != null) {
                        int API = pref.getInt("USE_API", 0);
                        if (API == 1) {

                            PUBLIC_KEY = pref.getString("BIN_PUB", "");
                            PRIVATE_KEY = pref.getString("BIN_PRI", "");
                        }
                    }
                    if(PUBLIC_KEY!=""&&PRIVATE_KEY!="") {
                        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(PUBLIC_KEY, PRIVATE_KEY);
                        BinanceApiRestClient client = factory.newRestClient();
                        try {
                            final NewOrderResponse newOrderResponse = client.newOrder(marketSell(p.strCoin + "BTC", "" + p.strBuySell).newOrderRespType(NewOrderResponseType.FULL));
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
                    }else{
                        Toast.makeText(getContext(), "Enter public key and private key !!!", Toast.LENGTH_LONG).show();
                    }
                    p.isSellNow = false;
                    viewHolder.btnSellNow.setVisibility(View.GONE);
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (CustomVVipAdapter.NotiHolder) view.getTag();
        }
        this.notifyDataSetChanged();
        p = getItem(position);
        Double profit = 0D;
        String strGia = p.strGia.replace("\\~", "");
        strGia = strGia.replace("~", "");
        Double gia = Double.parseDouble(strGia);
        Double chenhHT = 0D;
        if (p.strGiaHienTai != null && p.strGiaHienTai > 0) {
            chenhHT = (Math.abs(p.strGiaHienTai - gia) / gia) * 100;
        }

        if (strNN.equalsIgnoreCase("VN")) {
            String strLaiLo = "";
            String strTimeMua = "";
            if (p.strTime.lastIndexOf(":") >= 0) {
                strTimeMua = p.strTime.substring(0, p.strTime.lastIndexOf(":"));
                strTimeMua = strTimeMua.replace(":", "h");
            }
            viewHolder.txtGiaMua.setText(p.strGia);
            viewHolder.txtTimeMua.setText("được báo mua lúc " + strTimeMua);
            viewHolder.txtCoin.setText(p.strCoin);
            if (p.strGiaHienTai != null) {
                viewHolder.txtGiaHienTai.setText("Giá hiện tại: " + String.format("%.8f", p.strGiaHienTai)
                        + " (" + (p.strGiaHienTai > gia ? "+" : "-") + String.format("%.2f", chenhHT) + "%)");
            }
            viewHolder.txtPriceBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "(hệ thống đang quét giá bán tốt nhất)" : (p.strGiaBan + ""));
            viewHolder.txtTimeBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("mà hệ thống báo bán lúc " + p.strTimeBan + ""));
            if (p.numberBuy != "0") {
//                viewHolder.txtText3.setText("Mua vào: " + p.numberBuy + " đơn vị");
                if(p.isSellNow == true){
                viewHolder.imgBuy.setVisibility(View.VISIBLE);
                }
                viewHolder.imgBuy.setImageResource(R.drawable.buy);
                viewHolder.btnSellNow.setVisibility(View.VISIBLE);
//                viewHolder.txtText1.setText("Mua vào giá ");
//                viewHolder.txtText2.setVisibility(View.GONE);
//                viewHolder.txtPriceBan.setVisibility(View.GONE);
//                viewHolder.txtTimeBan.setVisibility(View.GONE);
            } else if (p.numberSell != "0") {
                viewHolder.btnSellNow.setVisibility(View.GONE);
//                viewHolder.txtText3.setText("Bán ra: " + p.numberSell + " đơn vị");
                viewHolder.imgBuy.setVisibility(View.VISIBLE);
                viewHolder.imgBuy.setImageResource(R.drawable.sell);
//                viewHolder.txtText1.setVisibility(View.GONE);
//                viewHolder.txtGiaMua.setVisibility(View.GONE);
//                viewHolder.txtTimeMua.setVisibility(View.GONE);
//                viewHolder.txtPriceBan.setVisibility(View.VISIBLE);
//                viewHolder.txtTimeBan.setVisibility(View.VISIBLE);
//                viewHolder.txtPriceBan.setText("Giá bán ra:  " + p.strGia);
//                viewHolder.txtTimeBan.setText("được báo bán lúc " + p.strTimeBan);
            } else {
                viewHolder.imgBuy.setVisibility(View.GONE);
                viewHolder.btnSellNow.setVisibility(View.GONE);
            }
            viewHolder.txtText3.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "===> Sẽ báo khi thấy có dấu hiệu " : "");
            viewHolder.txtProfit.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("===> thì profit thay đổi " + p.strProfit));
        } else {
            //
            String strLaiLo = "";
            String strTimeMua = "";
            if (p.strTime.lastIndexOf(":") >= 0) {
                strTimeMua = p.strTime.substring(0, p.strTime.lastIndexOf(":"));
                strTimeMua = strTimeMua.replace(":", "h");
            }
            viewHolder.txtGiaMua.setText(p.strGia);
            viewHolder.txtTimeMua.setText("time buy " + strTimeMua);
            viewHolder.txtCoin.setText(p.strCoin);
            if (p.strGiaHienTai != null) {
                viewHolder.txtGiaHienTai.setText("Current price: " + String.format("%.8f", p.strGiaHienTai)
                        + " (" + (p.strGiaHienTai > gia ? "+" : "-") + String.format("%.2f", chenhHT) + "%)");
            }
            viewHolder.txtPriceBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "(The system is scanning the best selling price)" : (p.strGiaBan + ""));
            viewHolder.txtTimeBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("system sells at " + p.strTimeBan + ""));
            if (p.numberBuy != "0") {
//                viewHolder.txtText3.setText("Buy in: " + p.numberBuy + " units");
                viewHolder.imgBuy.setVisibility(View.VISIBLE);
                viewHolder.imgBuy.setImageResource(R.drawable.buy);
                if(p.isSellNow == true){
                viewHolder.btnSellNow.setVisibility(View.VISIBLE);
                }
//                viewHolder.txtText1.setText("Price buy ");
//                viewHolder.txtText2.setVisibility(View.GONE);
//                viewHolder.txtPriceBan.setVisibility(View.GONE);
//                viewHolder.txtTimeBan.setVisibility(View.GONE);
            } else if (p.numberSell != "0") {
//                viewHolder.txtText3.setText("Sell out: " + p.numberSell + " units");
                viewHolder.imgBuy.setVisibility(View.VISIBLE);
                viewHolder.imgBuy.setImageResource(R.drawable.sell);
                viewHolder.btnSellNow.setVisibility(View.GONE);
//                viewHolder.txtText1.setVisibility(View.GONE);
//                viewHolder.txtGiaMua.setVisibility(View.GONE);
//                viewHolder.txtTimeMua.setVisibility(View.GONE);
//                viewHolder.txtPriceBan.setVisibility(View.VISIBLE);
//                viewHolder.txtTimeBan.setVisibility(View.VISIBLE);
//                viewHolder.txtPriceBan.setText("Price sell:  " + p.strGia);
//                viewHolder.txtTimeBan.setText("time sell " + p.strTimeBan);
            } else {
                viewHolder.imgBuy.setVisibility(View.GONE);
                viewHolder.btnSellNow.setVisibility(View.GONE);
            }
            viewHolder.txtText3.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "===> Will report when there are signs " : "");
            viewHolder.txtProfit.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("===> then profit changes " + p.strProfit));
//            String strLaiLo = "";
//            String strTimeMua = "";
//            if (p.strTime.lastIndexOf(":") >= 0) {
//                strTimeMua = p.strTime.substring(0, p.strTime.lastIndexOf(":"));
//                strTimeMua = strTimeMua.replace(":", "h");
//            }
//            viewHolder.txtGiaMua.setText(p.strGia);
//            viewHolder.txtTimeMua.setText("on time buy: " + strTimeMua);
//            viewHolder.txtCoin.setText(p.strCoin);
//            if (p.strGiaHienTai != null) {
//                viewHolder.txtGiaHienTai.setText("Current price: " + String.format("%.8f", p.strGiaHienTai)
//                        + " (" + (p.strGiaHienTai > gia ? "+" : "-") + String.format("%.2f", chenhHT) + "%)");
//            }
//            viewHolder.txtPriceBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "(The system is scanning the best selling price)" : (p.strGiaBan + ""));
//            viewHolder.txtTimeBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("system sells at " + p.strTimeBan + ""));
//            viewHolder.txtText3.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "===> Will report when there are signs " : "");
//            viewHolder.txtProfit.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("===> then profit changes " + p.strProfit));
        }

        if (p.strProfit.contains("+")) {
            viewHolder.txtCoin.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtGiaHienTai.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtGiaMua.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtTimeMua.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtPriceBan.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtTimeBan.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtProfit.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtText3.setTextColor(Color.parseColor("#27f546"));
        } else if (p.strProfit.contains("-")) {
            viewHolder.txtCoin.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtGiaHienTai.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtGiaMua.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtTimeMua.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtPriceBan.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtTimeBan.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtProfit.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtText3.setTextColor(Color.parseColor("#ff0000"));
        } else {

            viewHolder.txtCoin.setTextColor(Color.parseColor("#aa00ff"));
            viewHolder.txtGiaHienTai.setTextColor(Color.parseColor("#aa00ff"));
            viewHolder.txtGiaMua.setTextColor(Color.parseColor("#aa00ff"));
            viewHolder.txtTimeMua.setTextColor(Color.parseColor("#aa00ff"));
            viewHolder.txtPriceBan.setTextColor(Color.parseColor("#aa00ff"));
            viewHolder.txtTimeBan.setTextColor(Color.parseColor("#aa00ff"));
            viewHolder.txtProfit.setTextColor(Color.parseColor("#aa00ff"));
            viewHolder.txtText3.setTextColor(Color.parseColor("#aa00ff"));
        }
        return view;
    }

}
