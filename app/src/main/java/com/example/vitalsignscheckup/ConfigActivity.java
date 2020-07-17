package com.example.vitalsignscheckup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.Objects;
import java.util.concurrent.Executor;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ConfigActivity extends AppCompatActivity {

    int pvsPort = 1;
    int pvsInterval = 1;
    int ecgPort = 2;
    int ecgInterval = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_app_bar);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView actionBarTitle = findViewById(R.id.custom_app_bar_title);
        actionBarTitle.setText(R.string.ConfigTitle);


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
                    Toast.makeText(ConfigActivity.this, "Port 1:" + String.valueOf(pvsPort), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        inIntervalHint1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    pvsInterval = Integer.valueOf(inIntervalHint1.getText().toString());
                    Toast.makeText(ConfigActivity.this, "Interval 1:" + String.valueOf(pvsInterval), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        inPortHint2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    ecgPort = Integer.valueOf(inPortHint2.getText().toString());
                    Toast.makeText(ConfigActivity.this, "Port 2:" + String.valueOf(ecgPort), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        inIntervalHint2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    ecgInterval = Integer.valueOf(inIntervalHint2.getText().toString());
                    Toast.makeText(ConfigActivity.this, "Interval 2:" + String.valueOf(ecgInterval), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });



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


}