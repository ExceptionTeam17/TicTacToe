package com.exceptionteam17.tictactoe.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            //discovery starts, we can show progress dialog or perform other tasks
            Log.e("ivan", "0");
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            //discovery finishes, dismis progress dialog
            Log.e("ivan", "1");
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //bluetooth device found
            Log.e("ivan", "2");
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.e("ivan", "3");
            Toast.makeText(context, "Found device " + device.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
