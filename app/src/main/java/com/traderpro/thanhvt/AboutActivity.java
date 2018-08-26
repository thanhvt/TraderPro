package com.traderpro.thanhvt;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binance.api.client.BinanceApiClientFactory;
import com.traderpro.GCM.Config;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        simulateDayNight(/* DAY */ 3);
        SharedPreferences prefNN = getSharedPreferences(Config.NGON_NGU, 0);
        String strNN = prefNN.getString("NN", "VN");
        String des = strNN.equals("VN") ? this.getString(R.string.des_App_Vn) : this.getString(R.string.des_App_Vn);
        String app = strNN.equals("VN") ? this.getString(R.string.app_name_Vn) : this.getString(R.string.app_name);
        String version = strNN.equals("VN") ? this.getString(R.string.version_Vn) : this.getString(R.string.version);
        String connect = strNN.equals("VN") ? this.getString(R.string.connect_Vn) : this.getString(R.string.connect);
        String changelog = strNN.equals("VN") ? this.getString(R.string.Changelog_Vn) : this.getString(R.string.Changelog);
        String faq = strNN.equals("VN") ? this.getString(R.string.faq_Vn) : this.getString(R.string.faq);
        String donate = strNN.equals("VN") ? this.getString(R.string.donate_Vn) : this.getString(R.string.donate);

        String share = strNN.equals("VN") ? this.getString(R.string.share_Vn) : this.getString(R.string.share);
        String privacy = strNN.equals("VN") ? this.getString(R.string.privacy_vn) : this.getString(R.string.privacy);
        String term = strNN.equals("VN") ? this.getString(R.string.term_vn) : this.getString(R.string.term);
        String copyrigh = strNN.equals("VN") ? this.getString(R.string.copyright_Vn) : this.getString(R.string.copyright);
        BinanceApiClientFactory fac = BinanceApiClientFactory.newInstance();

        setContentView(R.layout.activity_about);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                //.setDescription("Trading Analytics is a Crypto Signal app for Bitcoin and Altcoin. It gives you access to cryptocurrency rates, coin market cap, detailed cryptocurrency trade, crypto news. Moreover it allows you to easily see blockchain cryptocurrency prices and price changes, coin market cap and 24h volume charts, coin details and advanced infomation.")
                .setDescription(des)
                .setImage(R.mipmap.ic_launcher)
                .addItem(new Element().setTitle(app) .setGravity(Gravity.CENTER))
                //.addItem(new Element().setTitle(strNN.equals("VN") ? R.string.title_using_bot_Vn : R.string.title_using_bot)
                .addItem(new Element().setTitle(version+ " 1.3.1").setGravity(Gravity.CENTER))
//                .addItem(adsElement)
                .addGroup(connect)
                .addEmail("itradinganalytics@gmail.com")
                .addWebsite("https://sbitex.com/")
                .addFacebook("https://www.facebook.com/groups/155778618409566/")
                .addTwitter("BTCTN")
                .addYoutube("UCUn2Ic5Pqa9gpVc-9nM4UNA")
                .addPlayStore("com.vnit.cryptonew")
                .addInstagram("medyo80")
                .addGitHub("medyo")
                .addItem(new Element().setTitle(changelog))
                .addItem(new Element().setTitle(faq))
                .addItem(new Element().setTitle(donate))
                .addItem(new Element().setTitle(share))
                .addItem(new Element().setTitle(privacy))
                .addItem(new Element().setTitle(term))
                .addItem(getCopyRightsElement())
                .create()
                ;
        LinearLayout layout = (LinearLayout) findViewById(R.id.lill);
        layout.addView(aboutPage);
//        setContentView(aboutPage);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    Element getCopyRightsElement() {
        SharedPreferences prefNN = getSharedPreferences(Config.NGON_NGU, 0);
        String strNN = prefNN.getString("NN", "VN");
        String copyright = strNN.equals("VN") ? this.getString(R.string.copyright_Vn) : this.getString(R.string.copyright);
        Element copyRightsElement = new Element();
        final String copyrights = String.format(copyright, Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.mipmap.ic_launcher);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

}
