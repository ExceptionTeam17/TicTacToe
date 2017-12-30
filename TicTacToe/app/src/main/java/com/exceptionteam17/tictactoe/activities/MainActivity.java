package com.exceptionteam17.tictactoe.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
import com.exceptionteam17.tictactoe.interfaces.ScoreBoardInterface;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;

public class MainActivity extends AppCompatActivity implements ScoreBoardInterface{

    private String username;
    private TextView win, draw, lost, usernameView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        removeActionBar();
        InitElements();
        loadFragment(new Fragment_Home());
        username = Preferences.getStringFromPreferences(this,"user");
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

    private void InitElements(){
        username = Preferences.getStringFromPreferences(this,"user");
        db = DatabaseHelper.getInstance(this);
        usernameView = findViewById(R.id.toolbar_username);
        win = findViewById(R.id.toolbar_win);
        lost = findViewById(R.id.toolbar_lost);
        draw = findViewById(R.id.toolbar_draw);
//        usernameView.setText(username);
//        setSingle();
    }

    public void setSingle(){
        win.setText("Wins: " + db.getUserWins(username));
        draw.setText("Draws: " + db.getUserDraws(username));
        lost.setText("Loses: " + db.getUserLoses(username));
    }

    public void setMulti(){
        win.setText("Wins: " + db.getUserWinsMulti(username));
        draw.setText("Draws: " + db.getUserDrawsMulti(username));
        lost.setText("Loses: " + db.getUserLosesMulti(username));
    }
}
