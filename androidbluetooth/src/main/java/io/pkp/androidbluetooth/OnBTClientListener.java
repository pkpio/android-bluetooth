package io.pkp.androidbluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Callbacks for actions related to Client mode communication with a BT server
 */
public interface OnBTClientListener {

    public void onClientConnected(BluetoothDevice device);

    public void onClientConnectionFailed(BluetoothDevice device);

    public void onClientConnectionLost();

    public void onClientDataReceived(int length, byte[] data);
}
