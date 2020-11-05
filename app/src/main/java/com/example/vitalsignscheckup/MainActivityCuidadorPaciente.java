package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vitalsignscheckup.models.Mediciones;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class MainActivityCuidadorPaciente extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tempText;
    private TextView hrText;
    private TextView bpTexttop;
    private TextView bpTextbot;
    private TextView estresText;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    private String idPaciente;
    DecimalFormat df = new DecimalFormat("#0.00");



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "MOSTRÁ LOS DATOS PAPAFRITA", Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        idPaciente = "5ulURkmhPUOuKOhPIrd4DsUsTAh1";
        //idPaciente = intent.getStringExtra("pacienteId");

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_cuidador_paciente);
        setContentView(R.layout.activity_main_cuidador_paciente);

//        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.custom_app_bar);
//        TextView actionBarTitle = findViewById(R.id.custom_app_bar_title);
//        actionBarTitle.setText(R.string.app_name);

        MaterialCardView temperatureCard   = findViewById(R.id.temperatureCard);

        MaterialCardView heartRateCard     = findViewById(R.id.heartRateCard);

        MaterialCardView bloodPressureCard = findViewById(R.id.bloodPressureCard);

        MaterialCardView stressLevelCard   = findViewById(R.id.stressLevelCard);


        tempText = findViewById(R.id.medidatempCuid);
        hrText = findViewById(R.id.medidaritmCui);
        bpTexttop = findViewById(R.id.medicionbpCuitop);
        bpTextbot = findViewById(R.id.medicionbpCuibot);
        estresText = findViewById(R.id.medicionestresCui);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario
        reference.child("Mediciones").child(idPaciente).child("1").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot);
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(1);
                tempText.setText(df.format(medicion.getMedicion()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Mediciones").child(idPaciente).child("2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot);
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(2);
                hrText.setText(df.format(medicion.getMedicion()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Mediciones").child(idPaciente).child("3").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot);
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(3);
                estresText.setText(df.format(medicion.getMedicion()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Mediciones").child(idPaciente).child("4").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot);
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(4);
                bpTexttop.setText(df.format(medicion.getMedicion()));
                bpTextbot.setText(df.format(medicion.getMedicion()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //tempText.setText("25O");
        //hrText.setText("25O");
        //estresText.setText("25O");
        //bpText.setText("25O");

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