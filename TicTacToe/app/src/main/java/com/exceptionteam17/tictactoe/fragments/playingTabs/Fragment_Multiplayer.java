package com.exceptionteam17.tictactoe.fragments.playingTabs;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;

import java.util.Set;

public class Fragment_Multiplayer extends Fragment{

    private View view;
    private String username;
    private TextView win, draw, lost, usernameView;
    private DatabaseHelper db;
    private static final int REQUEST_PERMISSION = 1;
    private Button start;
    private BluetoothAdapter bluetoothAdapter;
    private static String[] PERMISSIONS= {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            //Manifest.permission.BLUETOOTH_PRIVILEGED api 19+
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        initialize();
        verifyPermissions(((Activity)view.getContext()));
        startGame();
        return view;
    }

    private void initialize() {
        db = DatabaseHelper.getInstance(view.getContext());
        start = view.findViewById(R.id.start_gameplay_multi);
        username = Preferences.getStringFromPreferences(view.getContext(),"user");
        usernameView = view.findViewById(R.id.toolbar_username);
        win = view.findViewById(R.id.toolbar_win);
        lost = view.findViewById(R.id.toolbar_lost);
        draw = view.findViewById(R.id.toolbar_draw);
        usernameView.setText(username);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setMulti();
    }

    public void setMulti(){
        win.setText("Wins: " + db.getUserWinsMulti(username));
        draw.setText("Draws: " + db.getUserDrawsMulti(username));
        lost.setText("Loses: " + db.getUserLosesMulti(username));
    }

    public static void verifyPermissions(Activity activity) {
        int btAdmin = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN);
        int bt = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH);

        if (bt != PackageManager.PERMISSION_GRANTED || btAdmin != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
        }
    }

    private void startGame() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivity(turnOn);
                if (bluetoothAdapter == null) {
                    Toast.makeText(view.getContext(), "NO BLUETOOTH", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
//                    loadFragment(new Fragment_Gameplay());
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }
}
