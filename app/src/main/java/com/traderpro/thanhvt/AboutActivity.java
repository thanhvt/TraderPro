package com.traderpro.thanhvt;

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

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        simulateDayNight(/* DAY */ 3);
        setContentView(R.layout.activity_about);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Trading Analytics is a Crypto Signal app for Bitcoin and Altcoin. It gives you access to cryptocurrency rates, coin market cap, detailed cryptocurrency trade, crypto news. Moreover it allows you to easily see blockchain cryptocurrency prices and price changes, coin market cap and 24h volume charts, coin details and advanced infomation.")
                .setImage(R.mipmap.ic_launcher)
                .addItem(new Element().setTitle("Trading Analytics").setGravity(Gravity.CENTER))
                .addItem(new Element().setTitle("Version 1.3.1").setGravity(Gravity.CENTER))
//                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("itradinganalytics@gmail.com")
                .addWebsite("https://sbitex.com/")
                .addFacebook("https://www.facebook.com/groups/155778618409566/")
                .addTwitter("BTCTN")
                .addYoutube("UCUn2Ic5Pqa9gpVc-9nM4UNA")
                .addPlayStore("com.vnit.cryptonew")
                .addInstagram("medyo80")
                .addGitHub("medyo")
                .addItem(new Element().setTitle("Change log"))
                .addItem(new Element().setTitle("FAQ"))
                .addItem(new Element().setTitle("Donate"))
                .addItem(new Element().setTitle("Share this app"))
                .addItem(new Element().setTitle("Privacy Policy"))
                .addItem(new Element().setTitle("Term & Conditions"))
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
        Element copyRightsElement = new Element();
        final String copyrights = String.format("Copyright %d by TDev", Calendar.getInstance().get(Calendar.YEAR));
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
