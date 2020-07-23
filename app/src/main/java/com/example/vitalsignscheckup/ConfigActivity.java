package com.example.vitalsignscheckup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import info.plux.api.DeviceScan;
import info.plux.api.interfaces.Constants;

public class ConfigActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    Button connectDeviceButton;
    Button disconnectDeviceButton;
    EditText portBVP;
    //EditText interBVP;
    EditText portECG;
    //EditText interECG;
    private DeviceScan deviceScan;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        connectDeviceButton = (Button) findViewById(R.id.button_scanDevice);
//        disconnectDeviceButton = (Button) findViewById(R.id.button_stopDevice);
        SharedPreferences preferences;
        preferences = getSharedPreferences("Device", Context.MODE_PRIVATE );
        boolean connected = preferences.getBoolean("connected", false);
//        if (connected){
//            disconnectDeviceButton.setVisibility(View.VISIBLE);
//            connectDeviceButton.setVisibility(View.GONE);
//        }
//        else {
//            connectDeviceButton.setVisibility(View.VISIBLE);
//            disconnectDeviceButton.setVisibility(View.GONE);
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.configToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deviceScan = new DeviceScan(this);

        portBVP = (EditText) findViewById(R.id.text_portBVP);
        //interBVP = (EditText) findViewById(R.id.text_interBVP);
        portECG = (EditText) findViewById(R.id.text_portECG);
        //interECG = (EditText) findViewById(R.id.text_interECG);

        preferences = getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        String portbvp = preferences.getString("port", "");
        //String interbvp = preferences.getString("interval", "");
        portBVP.setText(portbvp);
//        interBVP.setText(interbvp);

        preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        String portecg = preferences.getString("port", "");
        //String interecg = preferences.getString("interval", "");
        portECG.setText(portecg);
//        interECG.setText(interecg);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(deviceScan != null){
            deviceScan.closeScanReceiver();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }


    public void scanDevice(View view) {
        System.out.println("Scan Inicio");
        SharedPreferences preferences;
        SharedPreferences.Editor spEditor;
        preferences = getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        spEditor = preferences.edit();
        spEditor.putString("port", portECG.getText().toString());
        //spEditor.putString("interval", interBVP.getText().toString());
        spEditor.apply();

        preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        spEditor = preferences.edit();
        spEditor.putString("port", portBVP.getText().toString());
        //spEditor.putString("interval", interECG.getText().toString());
        spEditor.apply();

        Intent intent = new Intent(ConfigActivity.this, ScanActivity.class);
        System.out.println("Scan Fin");
        startActivity(intent);


//        preferences = getSharedPreferences("Device", Context.MODE_PRIVATE );
//        spEditor = preferences.edit();
//        spEditor.putBoolean("connected", true);
//        spEditor.apply();
//        connectDeviceButton.setVisibility(View.GONE);
//        disconnectDeviceButton.setVisibility(View.VISIBLE);
//        connectDeviceButton.setText(connectText);
    }

//    public void stopDevice(View view) {
//        SharedPreferences preferences;
//        SharedPreferences.Editor spEditor;
//        System.out.println("Stop Inicio");
//        String disconnectText = disconnectDeviceButton.getText().toString();
//        disconnectDeviceButton.setText("Desconectando...");
//        preferences = getSharedPreferences("Device", Context.MODE_PRIVATE );
//        spEditor = preferences.edit();
//        spEditor.putBoolean("connected", false);
//        spEditor.apply();
////        deviceScan.stopScan();
//        disconnectDeviceButton.setVisibility(View.GONE);
//        connectDeviceButton.setVisibility(View.VISIBLE);
//        disconnectDeviceButton.setText(disconnectText);
//        preferences = view.getContext().getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
//        preferences.edit().clear().apply();
//        preferences = view.getContext().getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
//        preferences.edit().clear().apply();
//        System.out.println("Stop Fin");
//    }
}