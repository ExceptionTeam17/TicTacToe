package com.exceptionteam17.tictactoe.fragments.playingTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;

public class Fragment_Gameplay extends Fragment implements View.OnClickListener{

    private View view;
    private ImageView box1, box2, box3, box4, box5, box6, box7, box8, box9;
    private Button back;
    private ImageView[][] field;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gameplay, container, false);
        initialize();
        setImagesClickListeners();
        return view;
    }

    private void initialize() {
        box1 = view.findViewById(R.id.single_box1);
        box2 = view.findViewById(R.id.single_box2);
        box3 = view.findViewById(R.id.single_box3);
        box4 = view.findViewById(R.id.single_box4);
        box5 = view.findViewById(R.id.single_box5);
        box6 = view.findViewById(R.id.single_box6);
        box7 = view.findViewById(R.id.single_box7);
        box8 = view.findViewById(R.id.single_box8);
        box9 = view.findViewById(R.id.single_box9);
        back = view.findViewById(R.id.back_single);
        field = new ImageView[][]{
                         {box1, box2, box3},
                         {box4, box5, box6},
                         {box7, box8, box9}
                    };
    }

    private void setImagesClickListeners() {
        box1.setOnClickListener(this);
        box2.setOnClickListener(this);
        box3.setOnClickListener(this);
        box4.setOnClickListener(this);
        box5.setOnClickListener(this);
        box6.setOnClickListener(this);
        box7.setOnClickListener(this);
        box8.setOnClickListener(this);
        box9.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_box1:
                Toast.makeText(view.getContext(), "cukam [0][0]", Toast.LENGTH_SHORT).show();
                box1.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box2:
                Toast.makeText(view.getContext(), "cukam [0][1]", Toast.LENGTH_SHORT).show();
                box2.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box3:
                Toast.makeText(view.getContext(), "cukam [0][2]", Toast.LENGTH_SHORT).show();
                box3.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box4:
                Toast.makeText(view.getContext(), "cukam [1][0]", Toast.LENGTH_SHORT).show();
                box4.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box5:
                Toast.makeText(view.getContext(), "cukam [1][1]", Toast.LENGTH_SHORT).show();
                box5.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box6:
                Toast.makeText(view.getContext(), "cukam [1][2]", Toast.LENGTH_SHORT).show();
                box6.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box7:
                Toast.makeText(view.getContext(), "cukam [2][0]", Toast.LENGTH_SHORT).show();
                box7.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box8:
                Toast.makeText(view.getContext(), "cukam [2][1]", Toast.LENGTH_SHORT).show();
                box8.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.single_box9:
                Toast.makeText(view.getContext(), "cukam [2][2]", Toast.LENGTH_SHORT).show();
                box9.setImageResource(R.drawable.ic_launcher_background);
                break;
            case R.id.back_single:
                loadFragment(new Fragment_Home());
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }
}
