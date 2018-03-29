package com.exceptionteam17.tictactoe.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity{

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        removeActionBar();
        if(getWindow() != null) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        loadFragment(new Fragment_Home());
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713"); //TODO test id

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }

    private void removeActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }


    @Override
    protected void onPause() {
        mInterstitialAd = new InterstitialAd(this.getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //TODO test id
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("61D5A593BF290904C3894DE2CF09847D").build();  //TODO test device
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                MainActivity.this.finish();
            }
        });
        super.onPause();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}