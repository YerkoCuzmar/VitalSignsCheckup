package com.example.vitalsignscheckup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class MonitorBloodPressure extends AppCompatActivity {

    int count = 0;
    int count2 = 80;

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    String dateformatted = dateFormat.format(date);

    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_blood_pressure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bloodPressureToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView estado = (TextView)findViewById(R.id.estado);
        estado.setText("presión alta");

        final TextView hist1 = (TextView)findViewById(R.id.hist1);
        final TextView hist2 = (TextView)findViewById(R.id.hist2);

        final TextView textView = (TextView)findViewById(R.id.bp_medicion_mmhg);
        final TextView textView2 = (TextView)findViewById(R.id.bp_medicion_mmhg2);

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

        Thread t = new Thread(){
            @Override
            public void run(){

                br = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int strbvp = intent.getExtras().getIntArray("analogData")[posbvp];
                        textView.setText(String.valueOf(strbvp));
                        textView2.setText(String.valueOf(strbvp));

                        date = new Date();
                        dateformatted = dateFormat.format(date);
                        hist1.setText(dateformatted + "                     " + String.valueOf(strbvp) + " mmHg");
                        hist2.setText(dateformatted + "                     " + String.valueOf(strbvp) + " mmHg");
                        try {
                            OutputStreamWriter output = new OutputStreamWriter(openFileOutput("blood_pressure_history.txt", Activity.MODE_APPEND));
                            output.append(String.valueOf(strbvp) + "\n");
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
                                count2++;
                                textView.setText(String.valueOf(count));
                                textView2.setText(String.valueOf(count2));


                            }
                        });
                    } catch (InterruptedException e) {
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
        viewHistoryIntent.putExtra("origin", "bloodPressure");
        startActivity(viewHistoryIntent);
    }
}