package com.exceptionteam17.tictactoe.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.bluetooth.BluetoothConnectionService;
import com.exceptionteam17.tictactoe.bluetooth.EmptyBluetoothEventListener;
import com.exceptionteam17.tictactoe.bluetooth.request.DiscoverRequest;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
import com.exceptionteam17.tictactoe.interfaces.ScoreBoardInterface;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;

public class MainActivity extends AppCompatActivity{
    //kotlin test
//    private BluetoothConnectionService bluetoothConnectionService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        removeActionBar();
        InitElements();
        loadFragment(new Fragment_Home());
        /// kotlin test
        EmptyBluetoothEventListener listener = new EmptyBluetoothEventListener();
//        bluetoothConnectionService = new BluetoothConnectionService(this);
//        bluetoothConnectionService.setBluetoothEventListener(listener);
//        bluetoothConnectionService.enableBluetoothAdapter();
//        bluetoothConnectionService.discoverDevices();
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

    }
}