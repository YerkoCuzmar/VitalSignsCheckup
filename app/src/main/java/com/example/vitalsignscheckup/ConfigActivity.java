package com.example.vitalsignscheckup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import info.plux.api.DeviceScan;
import info.plux.api.interfaces.Constants;

public class ConfigActivity extends AppCompatActivity {

    int pvsPort = MainActivity.getPvsPort();
    int pvsInterval = MainActivity.getPvsInterval();
    int ecgPort = MainActivity.getEcgPort();
    int ecgInterval = MainActivity.getEcgInterval();

    private Handler handler = new Handler();

    Button connectDeviceButton;
    Button disconnectDeviceButton;
    private DeviceScan deviceScan;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        connectDeviceButton = (Button) findViewById(R.id.button_scanDevice);
        disconnectDeviceButton = (Button) findViewById(R.id.button_stopDevice);
        connectDeviceButton.setVisibility(View.VISIBLE);
        disconnectDeviceButton.setVisibility(View.GONE);


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

        final TextInputLayout inPort1 = findViewById(R.id.ConfiginPort1);
        final TextInputEditText inPortHint1 = findViewById(R.id.ConfiginPortHint1);
        inPortHint1.setHint(String.valueOf(pvsPort));

        final TextInputLayout inInterval1 = findViewById(R.id.ConfiginInterv1);
        final TextInputEditText inIntervalHint1 = findViewById(R.id.ConfiginIntervHint1);
        inIntervalHint1.setHint(String.valueOf(pvsInterval));


        final TextInputLayout inPort2 = findViewById(R.id.ConfiginPort2);
        final TextInputEditText inPortHint2 = findViewById(R.id.ConfiginPortHint2);
        inPortHint2.setHint(String.valueOf(ecgPort));

        final TextInputLayout inInterval2 = findViewById(R.id.ConfiginInterv2);
        final TextInputEditText inIntervalHint2 = findViewById(R.id.ConfiginIntervHint2);
        inIntervalHint2.setHint(String.valueOf(ecgInterval));

        inPortHint1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    pvsPort = Integer.valueOf(inPortHint1.getText().toString());
                    Toast.makeText(ConfigActivity.this, "Sensor PVS conectado en el puerto: " + String.valueOf(pvsPort), Toast.LENGTH_SHORT).show();
                    MainActivity.setPvsPort(pvsPort);
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });

        inIntervalHint1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    pvsInterval = Integer.valueOf(inIntervalHint1.getText().toString());
                    Toast.makeText(ConfigActivity.this, "PVS se medirá cada " + String.valueOf(pvsInterval) + " segundos", Toast.LENGTH_SHORT).show();
                    MainActivity.setPvsInterval(pvsInterval);
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });

        inPortHint2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    ecgPort = Integer.valueOf(inPortHint2.getText().toString());
                    Toast.makeText(ConfigActivity.this, "Sensor ECG conectado en el puerto: " + String.valueOf(ecgPort), Toast.LENGTH_SHORT).show();
                    MainActivity.setEcgPort(ecgPort);
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });

        inIntervalHint2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    ecgInterval = Integer.valueOf(inIntervalHint2.getText().toString());
                    Toast.makeText(ConfigActivity.this, "ECG se medirá cada " + String.valueOf(ecgInterval) + " segundos", Toast.LENGTH_SHORT).show();
                    MainActivity.setEcgInterval(ecgInterval);
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDevice(disconnectDeviceButton);
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
        // Stops scanning after a pre-defined scan period.
        String connectText = connectDeviceButton.getText().toString();
        connectDeviceButton.setText("Conectando...");
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                deviceScan.stopScan();
//                invalidateOptionsMenu();
//            }
//        }, SCAN_PERIOD);
//        deviceScan.doDiscovery();
        connectDeviceButton.setVisibility(View.GONE);
        disconnectDeviceButton.setVisibility(View.VISIBLE);
        connectDeviceButton.setText(connectText);
        System.out.println("Scan Fin");
    }

    public void stopDevice(View view) {
        System.out.println("Stop Inicio");
        String disconnectText = disconnectDeviceButton.getText().toString();
        disconnectDeviceButton.setText("Desconectando...");
//        deviceScan.stopScan();
        disconnectDeviceButton.setVisibility(View.GONE);
        connectDeviceButton.setVisibility(View.VISIBLE);
        disconnectDeviceButton.setText(disconnectText);
        System.out.println("Stop Fin");
    }
}