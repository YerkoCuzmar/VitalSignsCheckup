package com.example.vitalsignscheckup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class MonitorHeartRate extends AppCompatActivity {

    int count = 0;

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    String dateformatted = dateFormat.format(date);
    String histroy_log;

    BroadcastReceiver br;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_heart_rate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.heartratetoolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv1 = (TextView)findViewById(R.id.alerta_heart);
        tv1.setText("Mostrar Alerta");

        TextView tv2 = (TextView)findViewById(R.id.medida_heart);
        tv2.setText("Midiendo");

        TextView tv3 = (TextView)findViewById(R.id.med_ppm);
        tv3.setText("ppm");

        final TextView textView = (TextView)findViewById(R.id.medida_heart);

        final TextView h1 = (TextView)findViewById(R.id.heart1);

        SharedPreferences preferences = getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        int portbvp = Integer.valueOf(preferences.getString("port", null));

        preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        int portecg = Integer.valueOf(preferences.getString("port", null));

        final int posbvp, posecg;
        if(portbvp > portecg){
            posecg = 0;
            posbvp = 1;
        }else{
            posecg = 1;
            posbvp = 0;
        }

        Thread t=new Thread(){
            @Override
            public void run(){

                br = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int strecg = intent.getExtras().getIntArray("analogData")[posecg];
                        textView.setText(String.valueOf(strecg));

                        date = new Date();
                        dateformatted = dateFormat.format(date);
                        histroy_log = dateformatted + ": " + String.valueOf(strecg) + " ppm";
                        h1.setText(histroy_log);
                        try {
                            OutputStreamWriter output = new OutputStreamWriter(openFileOutput("heart_rate_history.txt", Activity.MODE_APPEND));
                            output.append(histroy_log+"\n");
                            output.flush();
                            output.close();
                        } catch (IOException e) {
                        }
                    }
                };

                /*
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                textView.setText(String.valueOf(count));
                                date = new Date();
                                dateformatted = dateFormat.format(date);
                                histroy_log = dateformatted + ": " + count + " ppm";
                                h1.setText(histroy_log);

                            }
                        });
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                 */
            }
        };
        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filt = new IntentFilter("analogData");
        this.registerReceiver(br, filt);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
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

    public void viewHistory(View view){
        Intent viewHistoryIntent = new Intent(view.getContext(), checkHistory.class);
        viewHistoryIntent.putExtra("origin", "heartRate");
        startActivity(viewHistoryIntent);
    }



}