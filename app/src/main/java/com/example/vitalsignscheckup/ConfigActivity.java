package com.example.vitalsignscheckup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import info.plux.api.DeviceScan;

public class ConfigActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    Button connectDeviceButton;
    Button disconnectDeviceButton;
    EditText portBVP;
    EditText portECG;
    EditText portTemp;
    EditText portEDA;
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
        portECG = (EditText) findViewById(R.id.text_portECG);
        portTemp = (EditText) findViewById(R.id.text_portTemp);
        portEDA = (EditText) findViewById(R.id.text_portEDA);


        preferences = getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        String portbvp = preferences.getString("port", "");
        portBVP.setText(portbvp);

        preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        String portecg = preferences.getString("port", "");
        portECG.setText(portecg);

        preferences = getSharedPreferences("TempConfig", Context.MODE_PRIVATE);
        String porttemp = preferences.getString("port", "");
        portTemp.setText(porttemp);
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
        spEditor.apply();

        preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        spEditor = preferences.edit();
        spEditor.putString("port", portBVP.getText().toString());
        spEditor.apply();

        preferences = getSharedPreferences("TempConfig", Context.MODE_PRIVATE);
        spEditor = preferences.edit();
        spEditor.putString("port", portTemp.getText().toString());
        spEditor.apply();

        preferences = getSharedPreferences("EDAConfig", Context.MODE_PRIVATE);
        spEditor = preferences.edit();
        spEditor.putString("port", portEDA.getText().toString());
        spEditor.apply();

        Intent intent = new Intent(ConfigActivity.this, ScanActivity.class);
        System.out.println("Scan Fin");
        startActivity(intent);

    }

    public static class ActivityLogin extends AppCompatActivity {

        TextInputLayout tiName, tiMobile, tiEmail, tiPass;
        EditText etName, etMobile, etEmail, etPass;
        Button btnRegister;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            instanceElements();

            btnRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent mainActivityIntent = new Intent(v.getContext(), MainActivity.class);
    //                startActivity(monitorTemperatureIntent);
                    Toast.makeText(ActivityLogin.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();

                }
            });

        }

        private void instanceElements(){
            etName = (EditText)findViewById(R.id.editTextName);
            etMobile = (EditText)findViewById(R.id.editTextMobile);
            etEmail = (EditText)findViewById(R.id.editTextEmail);
            etPass = (EditText)findViewById(R.id.editTextPassword);
            btnRegister = (Button)findViewById(R.id.cirLoginButton);
        }

    }
}