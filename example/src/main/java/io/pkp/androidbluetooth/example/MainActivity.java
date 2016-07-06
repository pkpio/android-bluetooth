package io.pkp.androidbluetooth.example;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import io.pkp.androidbluetooth.BluetoothSetup;

public class MainActivity extends AppCompatActivity implements io.pkp.androidbluetooth.OnBTScanListener{
    TextView testview;
    List<BluetoothDevice> mBTDevices;
    BluetoothSetup mBTSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testview = (TextView) findViewById(R.id.hello);
        mBTSetup = new BluetoothSetup(this);
    }

    @Override
    public void onDeviceFound(BluetoothDevice newDevice) {
        Log.d("BT_DEVICE", newDevice.getName());
    }

    @Override
    public void onScanComplete() {
        Log.e("BT_DEVICE", "Scan complete!");
    }

    public void onPause(){
        super.onPause();
        mBTSetup.stopDiscovery();
        Log.e("BT_DEVICE", "Scan stopped");
    }

    public void onResume(){
        super.onResume();
        mBTSetup.discoverDevices(this);
        Log.e("BT_DEVICE", "Scan started");
    }
}
