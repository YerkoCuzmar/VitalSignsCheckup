package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(MainActivity.this, "INICIO", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_app_bar);
        TextView actionBarTitle = findViewById(R.id.custom_app_bar_title);
        actionBarTitle.setText(R.string.app_name);

        MaterialCardView temperatureCard   = findViewById(R.id.temperatureCard);

        MaterialCardView heartRateCard     = findViewById(R.id.heartRateCard);

        MaterialCardView bloodPressureCard = findViewById(R.id.bloodPressureCard);

        MaterialCardView stressLevelCard   = findViewById(R.id.stressLevelCard);

        temperatureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorTemperatureIntent = new Intent(view.getContext(), MonitorTemperature.class);
                startActivity(monitorTemperatureIntent);
//                Toast.makeText(MainActivity.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();
            }
        });


        heartRateCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorHeartRateIntent = new Intent(view.getContext(), MonitorHeartRate.class);
                startActivity(monitorHeartRateIntent);
//                Toast.makeText(MainActivity.this, "heartRate", Toast.LENGTH_SHORT).show();
            }
        });


        //servicio heartRate
        /*
        heartRateCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent ServiceHeartRateIntent = new Intent(MainActivity.this, ServiceHeartRate.class);
                startService(ServiceHeartRateIntent);
                Toast.makeText(MainActivity.this, "heartRateService", Toast.LENGTH_SHORT).show();
            }
        });*/


        bloodPressureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorBloodPressureIntent = new Intent(view.getContext(), MonitorBloodPressure.class);
                startActivity(monitorBloodPressureIntent);
//                Toast.makeText(MainActivity.this, "bloodPressure", Toast.LENGTH_SHORT).show();
            }
        });


        stressLevelCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorStressLevelIntent = new Intent(view.getContext(), MonitorStressLevel.class);
//                startActivity(monitorStressLevelIntent);
//                Toast.makeText(MainActivity.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();
            }
        });

    }
}