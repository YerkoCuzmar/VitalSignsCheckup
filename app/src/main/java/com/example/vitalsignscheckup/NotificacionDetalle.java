package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificacionDetalle extends AppCompatActivity {

    private double mTemp;
    private int mHeartRate;
    private double mStress;
    private int mBP1;
    private int mBP2;

    FirebaseAuth mAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        mTemp = intent.getDoubleExtra("Temp", 0.0);
        mHeartRate = intent.getIntExtra("HR", 2);
        mStress = intent.getDoubleExtra("Stress", 0.0);
        mBP1 = intent.getIntExtra("BP1", 2);
        mBP2 = intent.getIntExtra("BP2", 2);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_detalle);

        TextView T = findViewById(R.id.TEMP);
        TextView HR = findViewById(R.id.HR);
        TextView S = findViewById(R.id.STRESS);
        TextView BP1 = findViewById(R.id.BP1);
        TextView BP2 = findViewById(R.id.BP2);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario

        T.setText(String.valueOf(mTemp) + " ºC");
        HR.setText(String.valueOf(mHeartRate) + " ppm");
        S.setText(String.valueOf(mStress) + " uS");
        BP1.setText(String.valueOf(mBP1) + " mmHg");
        BP2.setText(String.valueOf(mBP2) + " mmHg");

    }
}