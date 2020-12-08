package com.example.vitalsignscheckup.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vitalsignscheckup.MonitorBloodPressure;
import com.example.vitalsignscheckup.MonitorHeartRate;
import com.example.vitalsignscheckup.MonitorStressLevel;
import com.example.vitalsignscheckup.MonitorTemperature;
import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.TutorialActivity;
import com.example.vitalsignscheckup.models.Notificaciones;
import com.google.android.material.card.MaterialCardView;

//import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor spEditor;
    int showTutorial = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflar o cargar el layout para el Fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        MaterialCardView temperatureCard   = (MaterialCardView) root.findViewById(R.id.temperatureCard);
        MaterialCardView heartRateCard     = (MaterialCardView) root.findViewById(R.id.heartRateCard);
        MaterialCardView bloodPressureCard = (MaterialCardView) root.findViewById(R.id.bloodPressureCard);
        MaterialCardView stressLevelCard   = (MaterialCardView) root.findViewById(R.id.stressLevelCard);

        Button btnNotification = (Button) root.findViewById(R.id.btnNotification);

        //TEMPERATURE
        temperatureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorTemperatureIntent;
                preferences = getActivity().getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
                int tempTutorial = preferences.getInt("temperature", 1);
                if(tempTutorial == showTutorial){
                    monitorTemperatureIntent = new Intent(view.getContext(), TutorialActivity.class);
                    monitorTemperatureIntent.putExtra("type", 1);
                }
                else {
                    monitorTemperatureIntent = new Intent(view.getContext(), MonitorTemperature.class);
                }
                startActivity(monitorTemperatureIntent);
//                Toast.makeText(MainActivity.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();
            }
        });

        //HEART RATE
        heartRateCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorHeartRateIntent;
                preferences = getActivity().getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
                int heartRateTutorial = preferences.getInt("heartRate", 1);
                if(heartRateTutorial == showTutorial){
                    monitorHeartRateIntent = new Intent(view.getContext(), TutorialActivity.class);
                    monitorHeartRateIntent.putExtra("type", 2);
                }
                else {
                    monitorHeartRateIntent = new Intent(view.getContext(), MonitorHeartRate.class);
                }
                startActivity(monitorHeartRateIntent);
            }
        });


        //service
        /*
        heartRateCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Toast.makeText(HomeFragment.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();
                Intent serviceHeartRateIntent = new Intent(view.getContext(), ServiceHeartRate.class);
                getActivity().startService(serviceHeartRateIntent);
            }

        });
        */

        //BLOOD PRESSURE
        bloodPressureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorBloodPressureIntent;
                preferences = getActivity().getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
                int heartRateTutorial = preferences.getInt("bloodPressure", 1);
                if(heartRateTutorial == showTutorial){
                    monitorBloodPressureIntent = new Intent(view.getContext(), TutorialActivity.class);
                    monitorBloodPressureIntent.putExtra("type", 4);
                }
                else {
                    monitorBloodPressureIntent = new Intent(view.getContext(), MonitorBloodPressure.class);
                }
                startActivity(monitorBloodPressureIntent);
            }
        });

        //STRESS
        stressLevelCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorStressLevelIntent;
                preferences = getActivity().getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
                int heartRateTutorial = preferences.getInt("stress", 1);
                if(heartRateTutorial == showTutorial){
                    monitorStressLevelIntent = new Intent(view.getContext(), TutorialActivity.class);
                    monitorStressLevelIntent.putExtra("type", 3);
                }
                else {
                    monitorStressLevelIntent = new Intent(view.getContext(), MonitorStressLevel.class);
                }
                startActivity(monitorStressLevelIntent);
//                Toast.makeText(HomeFragment.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Notificaciones notificacion = new Notificaciones(5);
                notificacion.enviaraBD();
            }
        });

        SharedPreferences preferences = this.getActivity().getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        String portbvp = preferences.getString("port", null);
        System.out.println("puertoBVP: " + portbvp);

        preferences = this.getActivity().getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        String portecg = preferences.getString("port", null);
        System.out.println("puertoECG: " + portecg);

        preferences = this.getActivity().getSharedPreferences("TempConfig", Context.MODE_PRIVATE);
        String porttemp = preferences.getString("port", null);
        System.out.println("puertoTEMP: " + porttemp);

        preferences = this.getActivity().getSharedPreferences("EDAConfig", Context.MODE_PRIVATE);
        String porteda = preferences.getString("port", null);
        System.out.println("puertoTEMP: " + porttemp);

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = this.getActivity().getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        String portbvp = preferences.getString("port", null);


        preferences = this.getActivity().getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        String portecg = preferences.getString("port", null);
        String interecg = preferences.getString("interval", null);
        System.out.println("puertoECG: " + portecg);
        System.out.println("interECG: " + interecg);

        preferences = this.getActivity().getSharedPreferences("Device", Context.MODE_PRIVATE);
        boolean connected = preferences.getBoolean("connected", false);
        System.out.println( connected);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        // setContentView(R.layout.activity_main);
        // Para hacer findViewById, debes hacerlo con la referencia de root que es tu layout
        // Ejemplo: TextView textView = root.findViewById(R.id.textView);
    }
}