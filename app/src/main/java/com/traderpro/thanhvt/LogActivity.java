package com.traderpro.thanhvt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.traderpro.model.accountapi.OrderHistoryEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;

public class LogActivity extends Fragment {

    // constant

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    ListView listView;
    ArrayList<OrderHistoryEntry> lstCurrentOrder;
    CustomOrderHistoryAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layoutlog, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvTradeHistory);

        new ExchangeCoin().execute();

//        TextView txtLog = (TextView) rootView.findViewById(R.id.txtLog);
//        try {
//            Calendar rightNow = Calendar.getInstance();
//            int nam = rightNow.get(Calendar.YEAR);
//            int thang = rightNow.get(Calendar.MONTH) + 1;
//            int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
//            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
//            int min = rightNow.get(Calendar.MINUTE);
//
//            File folder = new File(Environment.getExternalStorageDirectory() +
//                    File.separator + "TraderBittrex");
//            boolean success = true;
//            if (!folder.exists()) {
//                success = folder.mkdirs();
//            }
//
//            File myFile = new File(Environment.getExternalStorageDirectory() +
//                    File.separator + "TraderBittrex" + File.separator + nam + thang + ngay + "_log.txt");
//            if (!myFile.exists()) {
//                myFile.createNewFile();
//            }
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            String aDataRow = "";
//            String aBuffer = "";
//            while ((aDataRow = myReader.readLine()) != null) {
//                aBuffer += aDataRow + "\n";
//            }
//            String strNoiDung = aBuffer;
//            txtLog.setText(aBuffer);
//            myReader.close();
//        } catch (Exception e) {
//
//        }
        return rootView;
    }

    class ExchangeCoin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BittrexData data = new BittrexData();
            BittrexAPI.setAPIKeys("1", "1");
            data.set(BittrexAPI.getOrderHistory());
            data.printAllElements();
            lstCurrentOrder = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                OrderHistoryEntry itemOrder = new OrderHistoryEntry();
                itemOrder.setOrderUuid(data.get(i, "OrderUuid"));
                itemOrder.setExchange(data.get(i, "Exchange"));
                itemOrder.setOrderType(data.get(i, "OrderType"));
                itemOrder.setLimit(BigDecimal.valueOf(Double.parseDouble(data.get(i, "Limit"))));
                itemOrder.setQuantity(BigDecimal.valueOf(Double.parseDouble(data.get(i, "Quantity"))));
                itemOrder.strDate = data.get(i, "Closed");

//                if (itemOrder.getType().contains("BUY"))
                {
                    lstCurrentOrder.add(itemOrder);
                    System.out.println(lstCurrentOrder.get(i).getExchange() + "|" + lstCurrentOrder.get(i).getQuantity() + "|" +
                            lstCurrentOrder.get(i).getLimit());
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
            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        customAdapter = new CustomOrderHistoryAdapter(getActivity(), R.layout.layout_orderhistory, lstCurrentOrder);
                        customAdapter.notifyDataSetChanged();
                        listView.setAdapter(customAdapter);

                    }
                });
            }

        }
    }
}