package com.example.vitalsignscheckup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class MonitorBloodPressure extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_blood_pressure);
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_app_bar);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView actionBarTitle = findViewById(R.id.custom_app_bar_title);
        actionBarTitle.setText(R.string.MonitorBloodPressureTitle);

        //set valor de 1
        //TextView bp_medicion_mmhg = (TextView)findViewById(R.id.bp_medicion_mmhg);
        //bp_medicion_mmhg.setText("meditionuno");
        //set valor de 2
        //TextView bp_medicion_mmhg2 = (TextView)findViewById(R.id.bp_medicion_mmhg2);
        //bp_medicion_mmhg2.setText("meditiondos");
        //set alert type
        TextView estado = (TextView)findViewById(R.id.estado);
        estado.setText("presión alta");

        final TextView textView = (TextView)findViewById(R.id.bp_medicion_mmhg);
        final TextView textView2 = (TextView)findViewById(R.id.bp_medicion_mmhg2);
        Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                textView.setText(String.valueOf(count));
                                textView2.setText(String.valueOf(count+80));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

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