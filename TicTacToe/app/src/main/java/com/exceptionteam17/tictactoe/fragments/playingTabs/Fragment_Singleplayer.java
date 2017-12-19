package com.exceptionteam17.tictactoe.fragments.playingTabs;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.exceptionteam17.tictactoe.R;

public class Fragment_Singleplayer extends Fragment{

    private View view;
    private Button start;

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
                loadFragment(new Fragment_Gameplay());
            }
        });
    }
}
