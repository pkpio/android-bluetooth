package io.pkp.androidbluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Provides necessary callbacks for newly discovered bluetooth devices
 * Created by praveen on 25.06.15.
 */
public interface OnBTScanListener {

    /**
     * Called when a new device has been discovered by the bluetooth discovery service
     *
     * @param newDevice Newly found device
     */
    public void onDeviceFound(BluetoothDevice newDevice);

    /**
     * When scan has been complete
     */
    public void onScanComplete();
}
