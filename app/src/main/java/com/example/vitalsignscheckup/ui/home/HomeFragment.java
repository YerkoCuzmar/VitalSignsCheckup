package com.example.vitalsignscheckup.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.monitor.BloodPressureTest;
import com.example.vitalsignscheckup.monitor.HeartRateTest;
import com.example.vitalsignscheckup.monitor.StressLevelTest;
import com.example.vitalsignscheckup.monitor.TemperatureTest;
import com.google.android.material.card.MaterialCardView;

//import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflar o cargar el layout para el Fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        MaterialCardView temperatureCard   = (MaterialCardView) root.findViewById(R.id.temperatureCard);
        MaterialCardView heartRateCard     = (MaterialCardView) root.findViewById(R.id.heartRateCard);
        MaterialCardView bloodPressureCard = (MaterialCardView) root.findViewById(R.id.bloodPressureCard);
        MaterialCardView stressLevelCard   = (MaterialCardView) root.findViewById(R.id.stressLevelCard);

        temperatureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorTemperatureIntent = new Intent(view.getContext(), TemperatureTest.class);
                startActivity(monitorTemperatureIntent);
//                Toast.makeText(MainActivity.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();
            }
        });
        heartRateCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorHeartRateIntent = new Intent(view.getContext(), HeartRateTest.class);
                startActivity(monitorHeartRateIntent);
            }
        });
        bloodPressureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorBloodPressureIntent = new Intent(view.getContext(), BloodPressureTest.class);
                startActivity(monitorBloodPressureIntent);
            }
        });
        stressLevelCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorStressLevelIntent = new Intent(view.getContext(), StressLevelTest.class);
                startActivity(monitorStressLevelIntent);
//                Toast.makeText(MainActivity.this, "Funcion no disponible", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences preferences = this.getActivity().getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        String portbvp = preferences.getString("port", null);
        String interbvp = preferences.getString("interval", null);
        System.out.println("puertoBVP: " + portbvp);
        System.out.println("interBVP: " + interbvp);

        preferences = this.getActivity().getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
        String portecg = preferences.getString("port", null);
        String interecg = preferences.getString("interval", null);
        System.out.println("puertoECG: " + portecg);
        System.out.println("interECG: " + interecg);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = this.getActivity().getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
        String portbvp = preferences.getString("port", null);
        String interbvp = preferences.getString("interval", null);
        System.out.println("puertoBVP: " + portbvp);
        System.out.println("interBVP: " + interbvp);

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