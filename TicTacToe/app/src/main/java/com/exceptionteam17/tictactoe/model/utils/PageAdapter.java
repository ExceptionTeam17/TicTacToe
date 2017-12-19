package com.exceptionteam17.tictactoe.model.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.exceptionteam17.tictactoe.fragments.playingTabs.Fragment_Multiplayer;
import com.exceptionteam17.tictactoe.fragments.playingTabs.Fragment_Singleplayer;

public class PageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PageAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        if(numberOfTabs >= 0) {
            this.numberOfTabs = numberOfTabs;
        } else {
            this.numberOfTabs = 0;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment_Singleplayer fragmentSingleplayer = new Fragment_Singleplayer();
                return fragmentSingleplayer;
            case 1:
                Fragment_Multiplayer fragmentMultiplayer = new Fragment_Multiplayer();
                return fragmentMultiplayer;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
