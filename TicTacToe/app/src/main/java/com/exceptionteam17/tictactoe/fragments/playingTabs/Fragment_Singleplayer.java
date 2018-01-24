package com.exceptionteam17.tictactoe.fragments.playingTabs;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.activities.OnePlayerDificult;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;

public class Fragment_Singleplayer extends Fragment{

    private View view;
    private String username;
    private TextView win, draw, lost, usernameView;
    private Button start;
    private DatabaseHelper db;

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
//                loadFragment(new Fragment_Gameplay());
                Intent i = new Intent(getActivity(), OnePlayerDificult.class);
                 getActivity().startActivity(i);
            }
        });
    }


    public void setSingle(){
        win.setText("Wins: " + db.getUserWins(username));
        draw.setText("Draws: " + db.getUserDraws(username));
        lost.setText("Loses: " + db.getUserLoses(username));
    }
}
