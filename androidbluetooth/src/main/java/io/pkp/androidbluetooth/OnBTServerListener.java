package io.pkp.androidbluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Callbacks for actions related to Client mode communication with a BT server
 */
public interface OnBTServerListener {

    public void onConnected(BluetoothDevice device);

    public void onConnectionLost();

    public void onDataReceived(int length, byte[] data);
}
