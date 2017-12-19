package com.exceptionteam17.tictactoe.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exceptionteam17.tictactoe.model.utils.PageAdapter;
import com.exceptionteam17.tictactoe.R;

public class Fragment_Home extends Fragment {

    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initialize();
        loadTabView();
        return view;
    }

    private void initialize() {
        viewPager = view.findViewById(R.id.viewPagerHomeFragment);
        tabLayout = view.findViewById(R.id.tabLayoutHomeFragment);
    }

    private void loadTabView() {
        tabLayout.addTab(tabLayout.newTab().setText("Singleplayer"));
        tabLayout.addTab(tabLayout.newTab().setText("Multiplayer"));
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PageAdapter adapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
//        viewPager.setPageTransformer(true, new RotateDownTransformer());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
