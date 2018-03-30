package com.exceptionteam17.tictactoe.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
import com.exceptionteam17.tictactoe.model.utils.Utils;
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
        MobileAds.initialize(this, "ca-app-pub-3532736192097860~3422811596");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(this);
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
        mInterstitialAd.setAdUnitId("ca-app-pub-3532736192097860/8439813485");
        AdRequest adRequest = new AdRequest.Builder().build();
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