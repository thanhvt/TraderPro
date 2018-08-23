package com.traderpro.thanhvt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class TraderProActivity extends AppCompatActivity {

  // History
  // https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName=BTC-WAVES&tickInterval=day
  // https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName=BTC-WAVES&tickInterval=thirtyMin&_=1499127220008
  // https://bittrex.com/Api/v2.0/pub/market/GetLatestTick?marketName=BTC-WAVES&tickInterval=onemin&_=1499127220008

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trader_pro);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });

    TextView txtCoin = (TextView) findViewById(R.id.txtCoin);
    TextView txtBaoNhieu = (TextView) findViewById(R.id.txtSoLuong);
    TextView txtGia = (TextView) findViewById(R.id.txtGia);

    Button btnChoiNo = (Button) findViewById(R.id.btnChoiNo);

    btnChoiNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Halu", Toast.LENGTH_LONG).show();
      }
    });

    new ExchangeConnect().execute();
  }

  class ExchangeConnect extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
      BittrexData data = new BittrexData();
      System.out.println("Param " + params[0]);
      System.out.println("Param " + params[1]);
      System.out.print("Param " + params[2]);
//      data.set(BittrexAPI.getMarketHistory("BTC-ETH"));
      data.set(BittrexAPI.getOrderBook("USDT-ETH"));
//      System.out.print(x);
      System.out.println(data.getObject());
//        System.out.println(data.getObject().get(0).get(1));
      for (int b = 0; b < data.size(); b++) {
//            System.out.println(data.get(b, "L"));
      }

        /* OUTPUT
            BTC-LTC
            BTC-DOGE
            ...
         */
      // ACCOUNT
      BittrexAPI.setAPIKeys("1", "1");
      System.out.println(BittrexAPI.getBalance("ETH"));
      try {
        JSONObject jSONObject = new JSONObject(BittrexAPI.getBalance("ETH"));
        JSONObject jResult = jSONObject.getJSONObject("result");
        Double balance = jResult.getDouble("Balance");
        System.out.println(balance);
      } catch (Exception e) {
      }
//            Mua BAT bằng ETH với giá 1 BAT = 0.00006 ETH và mua đúng 10
//            System.out.println(bittrexData.createBuyOrder(Currency.ETH, Currency.BAT, 10, 0.00006));
//
//            System.out.println(bittrexData.createSellOrder(Currency.ETH, Currency.BAT, 10, 0.1));
//            String buy = BittrexAPI.buyLimit("USDT-ETH", "0.05", "610");
//            System.out.println("UUID: " + buy);
//      BittrexAPI data2 = new BittrexAPI();
//      System.out.println(data.get("CryptoAddress"));
      return null;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {

        }
      });
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_trader, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
