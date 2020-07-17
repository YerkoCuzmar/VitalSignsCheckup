package com.example.vitalsignscheckup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Objects;

public class MonitorHeartRate extends AppCompatActivity {

    private int count;
    private ReadingFragment readingFragment = new ReadingFragment();

    protected void onCreate(Bundle savedInstanceState) {
        count = 0;
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(R.id.readingContainer, readingFragment)
                .addToBackStack("heart_rate_reading").commit();
        setContentView(R.layout.activity_monitor_heart_rate);
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_app_bar);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView actionBarTitle = findViewById(R.id.custom_app_bar_title);
        actionBarTitle.setText(R.string.MonitorHeartRateTitle);


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

    
//    public void ShowAlerta(){
//        TextView tv1 = (TextView)findViewById(R.id.alerta_heart);
//        tv1.setText("Mostrar Alerta");
//    }
//    public void ShowData(){
//        TextView tv2 = (TextView)findViewById(R.id.medida_heart);
//        tv2.setText("55 BPM");
//    }
}