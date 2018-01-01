package com.exceptionteam17.tictactoe.bluetooth

import android.util.Log

class EmptyBluetoothEventListener : IBluetoothEventListener {
    override fun onDisconnecting() {
        Log.e("Kotlin", "1")
    }

    override fun onDisconnected() {
        Log.e("Kotlin", "2")
    }

    override fun onConnected(isSuccess: Boolean) {
        Log.e("Kotlin", "3")
    }

    override fun onPairing() {
        Log.e("Kotlin", "4")
    }

    override fun onConnecting() {
        Log.e("Kotlin", "5")
    }

    override fun onDiscovering() {
        Log.e("Kotlin", "6")
    }

    override fun onDiscovered() {
        Log.e("Kotlin", "7")
    }

    override fun onPaired() {
        Log.e("Kotlin", "8")
    }

    override fun onEnable() {
        Log.e("Kotlin", "9")
    }

}