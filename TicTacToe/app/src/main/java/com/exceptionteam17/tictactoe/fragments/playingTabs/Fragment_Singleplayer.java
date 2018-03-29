package com.exceptionteam17.tictactoe.fragments.playingTabs;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;

public class Fragment_Singleplayer extends Fragment{

    private View view;
    private String username;
    private TextView win, draw, lost, usernameView;
    private Button start;
    private DatabaseHelper db;
    private RadioButton rbSimple;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_singleplayer, container, false);
        initialize();
        startGame();
        return view;
    }

    private void initialize() {
        start = view.findViewById(R.id.start_gameplay_single);
        db = DatabaseHelper.getInstance(view.getContext());
        username = Preferences.getStringFromPreferences(view.getContext(),"user");
        usernameView = view.findViewById(R.id.toolbar_username);
        win = view.findViewById(R.id.toolbar_win);
        lost = view.findViewById(R.id.toolbar_lost);
        draw = view.findViewById(R.id.toolbar_draw);
        usernameView.setText(username);
        rbSimple = view.findViewById(R.id.single_radio_easy);
        setSingle();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }

    private void startGame() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment_Gameplay fr = new Fragment_Gameplay();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSimple", rbSimple.isChecked());
                fr.setArguments(bundle);
                loadFragment(fr);
            }
        });
    }


    public void setSingle(){
        win.setText(getString(R.string.Wins, db.getUserWins(username)));
        draw.setText(getString(R.string.Draws, db.getUserDraws(username)));
        lost.setText(getString(R.string.Loses, db.getUserLoses(username)));
    }
}
