package io.pkp.androidbluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Provides necessary callbacks for bluetooth device bonding
 * Created by praveen on 29.06.15.
 */
public interface OnBTBondListener {

    /**
     * When bond state changes
     *
     * @param device
     * @param currState
     * @param prevState
     */
    public void onBondStateChange(BluetoothDevice device, int currState, int prevState);
}
