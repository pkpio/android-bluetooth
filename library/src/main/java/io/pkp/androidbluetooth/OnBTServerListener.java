package io.pkp.androidbluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Callbacks for actions related to Client mode communication with a BT server
 * <p/>
 * Created by praveen on 30.06.15.
 */
public interface OnBTServerListener {

    public void onConnected(BluetoothDevice device);

    public void onConnectionLost();

    public void onDataReceived(int length, byte[] data);
}
