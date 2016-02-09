package io.pkp.androidbluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class does all the work related to listing and pairing up with a new
 * Bluetooth device
 */
public class BluetoothSetup {
    final String DEBUG_TAG = this.getClass().getName();

    // Received from caller
    Context mContext;

    // Member fields
    BluetoothAdapter mBluetoothAdapter;
    BroadcastReceiver mDiscoveryReceiver;
    BroadcastReceiver mBondingReceiver;
    BroadcastReceiver mStateChangeReceiver;

    OnBTScanListener mOnBTScanListener;
    OnBTBondListener mOnBTBondListener;
    OnBTStateListener mOnBTStateListener;

    /**
     * The calling activity must make sure that bluetooth is enabled before calling further methods
     * in this class
     */
    public BluetoothSetup(Context context) {
        this.mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * A list of paired devices
     *
     * @return List of Bluetooth devices
     */
    public List<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices == null)
            return null;
        return new ArrayList<BluetoothDevice>(pairedDevices);
    }

    /**
     * To check if BT is on
     *
     * @return True if On
     */
    public Boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * Starts discovering new bluetooth devices.
     *
     * @param onBTScanListener listener to receive updates when devices are found
     * @return False if the discovery process failed to start
     */
    public Boolean discoverDevices(OnBTScanListener onBTScanListener) {
        this.mOnBTScanListener = onBTScanListener;

        // Create a BroadcastReceiver for ACTION_FOUND
        mDiscoveryReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Send update to user
                    if (mOnBTScanListener != null)
                        mOnBTScanListener.onDeviceFound(device);
                }

                // When discovery is complete
                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    if (mOnBTScanListener != null)
                        mOnBTScanListener.onScanComplete();
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filters = new IntentFilter();
        filters.addAction(BluetoothDevice.ACTION_FOUND);
        filters.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filters.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(mDiscoveryReceiver, filters);

        // Start discovery
        return mBluetoothAdapter.startDiscovery();
    }

    /**
     * Stop discovering new devices.
     * -TODO- call this in onDestroy of caller activity
     */
    public void stopDiscovery() {
        try {
            mContext.unregisterReceiver(mDiscoveryReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        mBluetoothAdapter.cancelDiscovery();
    }

    public void subscribeStateChanges(OnBTStateListener onBTStateListener) {
        this.mOnBTStateListener = onBTStateListener;

        // Create a BroadcastReceiver for ACTION_STATE_CHANGED
        mStateChangeReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                // When BT state changed
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                    // Send update to user
                    if (mOnBTStateListener != null)
                        mOnBTStateListener.onStateChange(isEnabled());
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filters = new IntentFilter();
        filters.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mStateChangeReceiver, filters);
    }

    /**
     * Makes the device discoverable by other devices. This issues a prompt to the user which should
     * be handled / passed along by the calling activity. This will additionally turn on bluetooth.
     */
    public void enableVisibility() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                Params.BLUETOOTH_VISIBILITY_TIMEOUT);
        mContext.startActivity(discoverableIntent);
    }

    /**
     * Sets bluetooth visibility of this device to off. This uses a hack
     * to do this due to lack of relevant in BluetoothAdapter from Android
     */
    public void disableVisibility() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                1);
        mContext.startActivity(discoverableIntent);
    }


    /**
     * Bonds with the given bluetooth device
     * -TODO- Method for pre-kitkat devices
     *
     * @param device           BluetoothDevice
     * @param onBTBondListener Listener to receive callbacks for actions
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void bond(BluetoothDevice device, OnBTBondListener onBTBondListener) {
        this.mOnBTBondListener = onBTBondListener;

        // Create a BroadcastReceiver for BOND_STATE changes
        mBondingReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                // When device bond state changed
                if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int currState = 0;//intent.getParcelableExtra(BluetoothDevice.EXTRA_BOND_STATE);
                    int prevState = 0;//intent.getParcelableExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE);

                    // Send update to user
                    if (mOnBTBondListener != null)
                        mOnBTBondListener.onBondStateChange(device, currState, prevState);
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filters = new IntentFilter();
        filters.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mContext.registerReceiver(mBondingReceiver, filters);

        device.createBond();
    }

}
