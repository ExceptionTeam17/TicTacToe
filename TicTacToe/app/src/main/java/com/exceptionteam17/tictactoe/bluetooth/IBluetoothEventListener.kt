package com.exceptionteam17.tictactoe.bluetooth

interface IBluetoothEventListener {
    fun onEnable()
    fun onDiscovering()
    fun onDiscovered()
    fun onConnecting()
    fun onConnected(isSuccess: Boolean)
    fun onPairing()
    fun onPaired()
    fun onDisconnecting()
    fun onDisconnected()
}