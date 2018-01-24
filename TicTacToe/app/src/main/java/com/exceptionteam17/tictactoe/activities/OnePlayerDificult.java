package com.exceptionteam17.tictactoe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.exceptionteam17.tictactoe.R;

public class OnePlayerDificult extends Activity {

    public static Activity act_1d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act_1d = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player_difficult);
    }
}
