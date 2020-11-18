package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.vitalsignscheckup.models.Mediciones;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivityCuidadorPaciente extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tempText;
    private TextView hrText;
    private TextView bpTexttop;
    private TextView bpTextbot;
    private TextView estresText;
    private Button buttonNotifications;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    private String idPaciente;
    DecimalFormat df = new DecimalFormat("#0.00");
    DecimalFormat tempFormat = new DecimalFormat("#0.00");
    DecimalFormat HRFormat = new DecimalFormat("#0");
    DecimalFormat BPFormat = new DecimalFormat("#0");
    DecimalFormat SLFormat = new DecimalFormat("#0.00");



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        idPaciente = intent.getStringExtra("pacienteId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cuidador_paciente);
        Toolbar toolbar = findViewById(R.id.pacienteToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mi Paciente");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivityCuidadores.class);
                startActivity(intent);
                finish();
            }
        });

        MaterialCardView temperatureCard   = findViewById(R.id.temperatureCard);

        MaterialCardView heartRateCard     = findViewById(R.id.heartRateCard);

        MaterialCardView bloodPressureCard = findViewById(R.id.bloodPressureCard);

        MaterialCardView stressLevelCard   = findViewById(R.id.stressLevelCard);


        tempText = findViewById(R.id.medidatempCuid);
        hrText = findViewById(R.id.medidaritmCui);
        bpTexttop = findViewById(R.id.medicionbpCuitop);
        bpTextbot = findViewById(R.id.medicionbpCuibot);
        estresText = findViewById(R.id.medicionestresCui);
        buttonNotifications = findViewById(R.id.buttonNotification);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario

        //temperatura
        reference.child("Mediciones").child(idPaciente).child("1").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(1);
                tempText.setText(tempFormat.format(medicion.getMedicion()) + "°C");
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

        //ritmo cardíaco
        reference.child("Mediciones").child(idPaciente).child("2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(2);
                hrText.setText(BPFormat.format(medicion.getMedicion())+"ppm");
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

        //estrés
        reference.child("Mediciones").child(idPaciente).child("3").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(3);
                estresText.setText(SLFormat.format(medicion.getMedicion())+ " \u00B5" + "S");
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

        //presión sanguínea
        reference.child("Mediciones").child(idPaciente).child("4").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(4);
                bpTexttop.setText(BPFormat.format(medicion.getMedicion())+"mmHg");
                bpTextbot.setText(BPFormat.format(medicion.getMedicion2())+"mmHg");
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

        temperatureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorTemperatureIntent = new Intent(view.getContext(), CuidadorMonitorTemperature.class);
                monitorTemperatureIntent.putExtra("pacienteId", idPaciente);
                startActivity(monitorTemperatureIntent);
            }
        });


        heartRateCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorHeartRateIntent = new Intent(view.getContext(), CuidadorMonitorHeartRate.class);
                monitorHeartRateIntent.putExtra("pacienteId", idPaciente);
                startActivity(monitorHeartRateIntent);
            }
        });

        bloodPressureCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorBloodPressureIntent = new Intent(view.getContext(), CuidadorMonitorBloodPressure.class);
                monitorBloodPressureIntent.putExtra("pacienteId", idPaciente);
                startActivity(monitorBloodPressureIntent);
            }
        });


        stressLevelCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent monitorStressLevelIntent = new Intent(view.getContext(), CuidadorMonitorStressLevel.class);
                monitorStressLevelIntent.putExtra("pacienteId", idPaciente);
                startActivity(monitorStressLevelIntent);
            }
        });

        buttonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

}