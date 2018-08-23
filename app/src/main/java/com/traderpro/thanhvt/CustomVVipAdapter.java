package com.traderpro.thanhvt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.traderpro.GCM.Config;

import java.util.List;

public class CustomVVipAdapter extends ArrayAdapter<NotificationEntity> {

    Context mContext;
    List<NotificationEntity> lstOrder;
    BittrexData bittrexData;
    int mResource;
    String strNN;

    public CustomVVipAdapter(Context context, int resource, List<NotificationEntity> items) {
        super(context, resource, items);
        this.mContext = context;
        this.lstOrder = items;
        this.bittrexData = bittrexData;
        this.mResource = resource;
        SharedPreferences pref = mContext.getSharedPreferences(Config.NGON_NGU, 0);
        strNN = pref.getString("NN", "VN");
    }

    static class NotiHolder {
        TextView txtTimeMua;
        TextView txtGiaMua;
        TextView txtProfit;
        TextView txtGiaHienTai;
        TextView txtPriceBan;
        TextView txtTimeBan;
        TextView txtCoin;
        TextView txtText3;
        Double pMax;
        Double pProfit;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        CustomVVipAdapter.NotiHolder viewHolder;
        if (view == null) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(getContext());

            view = mInflater.inflate(mResource, parent, false);

            viewHolder = new CustomVVipAdapter.NotiHolder();
            viewHolder.txtGiaMua = (TextView) view.findViewById(R.id.txtGiaMua);
            viewHolder.txtTimeMua = (TextView) view.findViewById(R.id.txtTimeMua);
            viewHolder.txtGiaHienTai = (TextView) view.findViewById(R.id.txtGiaHienTai);
            viewHolder.txtPriceBan = (TextView) view.findViewById(R.id.txtGiaBan);
            viewHolder.txtProfit = (TextView) view.findViewById(R.id.txtProfit);
            viewHolder.txtTimeBan = (TextView) view.findViewById(R.id.txtTimeBan);
            viewHolder.txtCoin = (TextView) view.findViewById(R.id.txtCoin);
            viewHolder.txtText3 = (TextView) view.findViewById(R.id.txtText3);
            view.setTag(viewHolder);
        } else {
            viewHolder = (CustomVVipAdapter.NotiHolder) view.getTag();
        }
        this.notifyDataSetChanged();
        NotificationEntity p = getItem(position);
        Double profit = 0D;
        String strGia = p.strGia.replace("\\~", "");
        strGia = strGia.replace("~", "");
        Double gia = Double.parseDouble(strGia);
        Double chenhHT = 0D;
        if(p.strGiaHienTai != null && p.strGiaHienTai > 0){
            chenhHT = (Math.abs(p.strGiaHienTai - gia) / gia) * 100;
        }
        String strLaiLo = "";

        String strTimeMua = p.strTime.substring(0, p.strTime.lastIndexOf(":"));
        strTimeMua = strTimeMua.replace(":", "h");
        viewHolder.txtGiaMua.setText(p.strGia);
        viewHolder.txtTimeMua.setText("được báo mua lúc " + strTimeMua);
        viewHolder.txtCoin.setText(p.strCoin);
        if(p.strGiaHienTai != null) {
            viewHolder.txtGiaHienTai.setText("Giá hiện tại: " + String.format("%.8f", p.strGiaHienTai)
                    + " (" + (p.strGiaHienTai > gia ? "+" : "-") + String.format("%.2f", chenhHT) + "%)");
        }
        viewHolder.txtPriceBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "(hệ thống đang quét giá bán tốt nhất)" : (p.strGiaBan + ""));
        viewHolder.txtTimeBan.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("mà hệ thống báo bán lúc " + p.strTimeBan + ""));
        viewHolder.txtText3.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "===> Sẽ báo khi thấy có dấu hiệu " : "");
        viewHolder.txtProfit.setText(p.strGiaBan.equalsIgnoreCase("GIA_BAN") == true ? "" : ("===> thì profit thay đổi " + p.strProfit));

        if(p.strProfit.contains("+")){
            viewHolder.txtCoin.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtGiaHienTai.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtGiaMua.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtTimeMua.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtPriceBan.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtTimeBan.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtProfit.setTextColor(Color.parseColor("#27f546"));
            viewHolder.txtText3.setTextColor(Color.parseColor("#27f546"));
        }
        else if(p.strProfit.contains("-")){
            viewHolder.txtCoin.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtGiaHienTai.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtGiaMua.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtTimeMua.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtPriceBan.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtTimeBan.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtProfit.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.txtText3.setTextColor(Color.parseColor("#ff0000"));
        }
        else {

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
