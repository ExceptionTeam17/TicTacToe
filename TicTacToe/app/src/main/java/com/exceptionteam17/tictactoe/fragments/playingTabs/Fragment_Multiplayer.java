package com.exceptionteam17.tictactoe.fragments.playingTabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;

public class Fragment_Multiplayer extends Fragment{

    private View view;
    private String username;
    private TextView win, draw, lost, usernameView;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        db = DatabaseHelper.getInstance(view.getContext());
        username = Preferences.getStringFromPreferences(view.getContext(),"user");
        usernameView = view.findViewById(R.id.toolbar_username);
        win = view.findViewById(R.id.toolbar_win);
        lost = view.findViewById(R.id.toolbar_lost);
        draw = view.findViewById(R.id.toolbar_draw);
        usernameView.setText(username);
        setMulti();
    }

    public void setMulti(){
        win.setText("Wins: " + db.getUserWinsMulti(username));
        draw.setText("Draws: " + db.getUserDrawsMulti(username));
        lost.setText("Loses: " + db.getUserLosesMulti(username));
    }
}
