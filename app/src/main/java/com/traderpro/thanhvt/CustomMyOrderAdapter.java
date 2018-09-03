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

import java.util.List;

public class CustomMyOrderAdapter extends ArrayAdapter<Order> {

    Context mContext;
    List<Order> lstOrder;

    public CustomMyOrderAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mContext = context;
    }

    public CustomMyOrderAdapter(Context context, int resource, List<Order> items) {
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
            v = vi.inflate(R.layout.layout_orderitem, null);
        }

        final Order p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.txtContent);

            if (tt1 != null) {
                tt1.setText(p.getExchange() + " | " + p.getQuantity() + " | " + p.getLimit());
            }

            ImageView imgCancel = (ImageView) v.findViewById(R.id.imgCancel);
            imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, p.getOrderUuid(), Toast.LENGTH_SHORT).show();
                    new ExchangeCancel().execute(p.getOrderUuid());
                    lstOrder.remove(p);
                    notifyDataSetChanged();
                }
            });

        }

        return v;
    }

    class ExchangeCancel extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BittrexData data = new BittrexData();
            System.out.println("Param 0 " + params[0]);
            String uuid = params[0];
            // ACCOUNT
            BittrexAPI.setAPIKeys("1", "1");
            BittrexAPI.cancelOrder(uuid);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
//            mContext.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
    }

}
