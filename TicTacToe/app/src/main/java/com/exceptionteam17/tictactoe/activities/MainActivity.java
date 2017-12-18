package com.exceptionteam17.tictactoe.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
import com.exceptionteam17.tictactoe.model.Preferences;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        removeActionBar();
        loadFragment(new Fragment_Home());
        DatabaseHelper.getInstance(this).addUserWin(Preferences.getStringFromPreferences(this,"user"));
        Log.e("testtt", "" + DatabaseHelper.getInstance(this).getUserWins(Preferences.getStringFromPreferences(this,"user")));
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
}