package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivityCuidadorPaciente extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "MOSTRÁ LOS DATOS PAPAFRITA", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_cuidador_paciente);
        setContentView(R.layout.activity_main_cuidador_paciente);
        Toolbar toolbar = findViewById(R.id.pacienteToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mi Paciente");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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