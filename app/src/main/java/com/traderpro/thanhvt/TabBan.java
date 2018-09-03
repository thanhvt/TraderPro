package com.traderpro.thanhvt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.traderpro.model.accountapi.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TabBan extends Fragment {

    public Context mContex;
    AutoCompleteTextView txtCoin;
    EditText txtBaoNhieu, txtGia;
    List<String> lstCoin = new ArrayList<String>();
    private SwipeMenuListView mListView;
    ListView listView;
    CustomMyOrderAdapter customAdapter;
    ArrayList<Order> lstCurrentOrder;
    private ArrayList<String> mArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabban, container, false);

        txtCoin = (AutoCompleteTextView) rootView.findViewById(R.id.txtCoin);
        txtBaoNhieu = (EditText) rootView.findViewById(R.id.txtSoLuong);
        txtGia = (EditText) rootView.findViewById(R.id.txtGia);
        listView = (ListView) rootView.findViewById(R.id.lvBuySell);

        new ExchangeCoin().execute();
        txtCoin.setAllCaps(true);
        Button btnChoiNo = (Button) rootView.findViewById(R.id.btnChoiNo);

        btnChoiNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                final String strCapCoin = txtCoin.getText().toString();
                final String strSoLuong = txtBaoNhieu.getText().toString();
                final String strGia = txtGia.getText().toString();
                if (strCapCoin.length() == 0 || strSoLuong.length() == 0 || strGia.length() == 0)
                    return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Đại ca, chắc chắn bán " + strSoLuong
                        + " " + strCapCoin.split("-")[1] + " ra " + strCapCoin.split("-")[0] + " với giá " + strGia + " chứ a ??");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("OK ngon, bán đi em", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity().getApplicationContext(), "Halu: " + strCapCoin + "|" + strSoLuong + "|" + strGia, Toast.LENGTH_LONG).show();
                        new ExchangeSell().execute(strCapCoin, strSoLuong, strGia);

                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton("Bình tĩnh, chưa được", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return rootView;
    }

    class ExchangeSell extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BittrexData data = new BittrexData();
            String strCapCoin = params[0];
            String strSoLuong = params[1];
            String strGia = params[2];

            System.out.println("Param 0 " + strCapCoin);
            System.out.println("Param 1 " + strSoLuong);
            System.out.println("Param 2 " + strGia);
            BittrexAPI.setAPIKeys("1", "1");

//            Mua BAT bằng ETH với giá 1 BAT = 0.00006 ETH và mua đúng 10
//            System.out.println(bittrexData.createBuyOrder(Currency.ETH, Currency.BAT, 10, 0.00006));

//            System.out.println(bittrexData.createSellOrder(Currency.ETH, Currency.BAT, 10, 0.1));

//            String buy = BittrexAPI.buyLimit(strCapCoin, strSoLuong, strGia);
            String sell = BittrexAPI.sellLimit(strCapCoin, strSoLuong, strGia);

            System.out.println("UUID: " + sell);
            Order nO = new Order();
            nO.setOrderUuid(sell);
            nO.setExchange(strCapCoin);
            nO.setQuantity(BigDecimal.valueOf(Double.parseDouble(strSoLuong)));
            nO.setType("LIMIT SELL");
            nO.setLimit(BigDecimal.valueOf(Double.parseDouble(strGia)));
            lstCurrentOrder.add(nO);
//            customAdapter = new CustomMyOrderAdapter(getActivity(), R.layout.layout_orderitem, lstCurrentOrder);
//            customAdapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    class ExchangeCoin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BittrexData data = new BittrexData();
            data.set(BittrexAPI.getMarkets());

            for (int b = 0; b < data.size(); b++) {
                lstCoin.add(data.get(b, "MarketName"));
            }

            BittrexAPI.setAPIKeys("1", "1");
            data.set(BittrexAPI.getOrders());
            data.printAllElements();
            lstCurrentOrder = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                System.out.println(data.get(i, "OrderType"));
                Order itemOrder = new Order();
                itemOrder.setOrderUuid(data.get(i, "OrderUuid"));
                itemOrder.setExchange(data.get(i, "Exchange"));
                itemOrder.setType(data.get(i, "OrderType"));
                itemOrder.setQuantity(BigDecimal.valueOf(Double.parseDouble(data.get(i, "Quantity"))));
                itemOrder.setLimit(BigDecimal.valueOf(Double.parseDouble(data.get(i, "Limit"))));
                if (itemOrder.getType().contains("SELL")) {
                    lstCurrentOrder.add(itemOrder);
//                    System.out.println(lstCurrentOrder.get(i).getExchange() + "|" + lstCurrentOrder.get(i).getQuantity() + "|" +
//                            lstCurrentOrder.get(i).getLimit());
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter adapterCountries = new ArrayAdapter<String>
                                (getActivity(), android.R.layout.simple_list_item_1, lstCoin);
                        AutoSuggestAdapter adapter = new AutoSuggestAdapter(getActivity(), android.R.layout.simple_list_item_1, lstCoin);

                        txtCoin.setAdapter(adapter);

                        // specify the minimum type of characters before drop-down list is shown
                        txtCoin.setThreshold(1);

                        customAdapter = new CustomMyOrderAdapter(getActivity(), R.layout.layout_orderitem, lstCurrentOrder);
                        customAdapter.notifyDataSetChanged();
                        listView.setAdapter(customAdapter);

                    }
                });
        }
    }


}
