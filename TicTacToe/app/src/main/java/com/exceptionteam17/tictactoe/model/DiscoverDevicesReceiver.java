package com.exceptionteam17.tictactoe.model;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.exceptionteam17.tictactoe.fragments.playingTabs.Fragment_Multiplayer;

public class DiscoverDevicesReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d("ivan", "onReceive: ACTION FOUND.");

        if (action.equals(BluetoothDevice.ACTION_FOUND)){
            BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
            Fragment_Multiplayer.addDevice(device);
            Log.e("ivan", "onReceive: " + device.getName() + ": " + device.getAddress());
        }
    }
}
