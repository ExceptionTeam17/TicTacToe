package com.exceptionteam17.tictactoe.model;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothStateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ivan", "broadcast1");
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            // Bluetooth is disconnected, do handling here
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.e("ivan", "STATE OFF");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.e("ivan", "STATE TURNING OFF");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.e("ivan", "STATE ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.e("ivan", "STATE TURNING ON");
                    break;
            }
        }

    }
}
