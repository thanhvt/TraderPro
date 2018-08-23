package com.traderpro.thanhvt;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.traderpro.model.accountapi.Order;
import com.traderpro.model.accountapi.OrderHistoryEntry;

import java.util.List;

public class CustomOrderHistoryAdapter extends ArrayAdapter<OrderHistoryEntry> {

    Context mContext;
    List<OrderHistoryEntry> lstOrder;

    public CustomOrderHistoryAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mContext = context;
    }

    public CustomOrderHistoryAdapter(Context context, int resource, List<OrderHistoryEntry> items) {
        super(context, resource, items);
        this.mContext = context;
        this.lstOrder = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_orderhistory, null);
        }

        final OrderHistoryEntry p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.txtTime);
            TextView tt2 = (TextView) v.findViewById(R.id.txtCoin);
            TextView tt3 = (TextView) v.findViewById(R.id.txtSL);
            TextView tt4 = (TextView) v.findViewById(R.id.txtGia);
            p.strDate = p.strDate.replace("T", " ");
            String[] x = p.strDate.split(" ");
            String y = x[1].substring(0, x[1].lastIndexOf(":"));
            tt1.setText(y+ " " + x[0]);
            tt2.setText(p.getExchange());
            tt3.setText(p.getQuantity() + "");
            tt4.setText(p.getLimit() + "");

        }

        return v;
    }

}
