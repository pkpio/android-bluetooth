package io.pkp.androidbluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Callbacks for actions related to Client mode communication with a BT server
 */
public interface OnBTServerListener {

    public void onServerConnected(BluetoothDevice device);

    public void onServerConnectionLost();

    public void onServerDataReceived(int length, byte[] data);
}
