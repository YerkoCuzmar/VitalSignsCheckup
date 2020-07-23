package com.example.vitalsignscheckup;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitorBloodPressure extends AppCompatActivity {

    int count = 0;
    int count2 = 80;

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    String dateformatted = dateFormat.format(date);

    TextView hist1;
    TextView hist2;

    TextView textView;
    TextView textView2;

    SharedPreferences preferences;
    int portbvp, portecg;
    int posbvp, posecg;
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

        hist1 = (TextView)findViewById(R.id.hist1);
        hist2 = (TextView)findViewById(R.id.hist2);

        textView = (TextView)findViewById(R.id.bp_medicion_mmhg);
        textView2 = (TextView)findViewById(R.id.bp_medicion_mmhg2);

        preferences = getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        portbvp = Integer.valueOf(preferences.getString("port", null));

        preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        portecg = Integer.valueOf(preferences.getString("port", null));

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
                br = new BPDataReciever();
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

    private class BPDataReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int strbvp = intent.getExtras().getIntArray("analogData")[posbvp];
            textView.setText(String.valueOf(strbvp));
            textView2.setText(String.valueOf(strbvp));

            date = new Date();
            dateformatted = dateFormat.format(date);
            String text = dateformatted + "                     " + strbvp + " mmHg";
            hist1.setText(text);
            hist2.setText(text);
            try {
                OutputStreamWriter output = new OutputStreamWriter(openFileOutput("blood_pressure_history.txt", Activity.MODE_APPEND));
                output.append(strbvp + "\n");
                output.flush();
                output.close();
            } catch (IOException e) {
            }
        }
    }
}