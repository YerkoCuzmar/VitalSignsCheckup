package com.example.vitalsignscheckup;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {
    private final String TAG = getClass().getSimpleName();

    //Permission
    final private int REQUEST_ENABLE_BT  = 0;

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //Initializes a BluetoothAdapter
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        //checks if bluetooth is supported on the device
        if(bluetoothAdapter == null){
            Toast.makeText(this, "Error - Bluetooth not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                    askPermissions();
                }
                else{
                    testBluetooth();
                }
            }
        }, 2*1000); //wait for 5 seconds
    }

    private void startScanActivity(){
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity2.class);
        startActivity(intent);

        //close this activity
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_CANCELED) {
                finish();
                return;
            }
            else {
                startScanActivity();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void askPermissions() {
        testBluetooth();
    }

    private void testBluetooth() {
        // Ensures Bluetooth is enabled on the device. If Bluetooth is not currently enabled
        // fire an intent to display a dialog asking the user to grant permission to enable it
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBthIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBthIntent, REQUEST_ENABLE_BT);
        }
        else {
            startScanActivity();
        }
    }
}
