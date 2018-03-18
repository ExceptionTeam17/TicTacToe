package com.exceptionteam17.tictactoe.model;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothDiscoverableStateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.e("blaaaaa", intent.getAction());

        if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

            int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

            switch(mode){
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    Log.e("ivan", "Discoverable Enabled");
                    break;
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                    Log.e("ivan", "Discoverable Enabled. Able to receive connections.");
                    break;
                case BluetoothAdapter.SCAN_MODE_NONE:
                    Log.e("ivan", "Dicoverable Disabled. Unable to receive connections.");
                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                    Log.e("ivan", "Connecting...");
                    break;
                    case BluetoothAdapter.STATE_CONNECTED:
                    Log.e("ivan", "Connected");
                    break;
            }
        }
    }
}
