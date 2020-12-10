package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificacionDetalle extends AppCompatActivity {

    private String nombrePaciente;
    private String fecha;
    private String hora;
    private int type;
    private double mTemp;
    private int mHeartRate;
    private double mStress;
    private int mBP1;
    private int mBP2;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        nombrePaciente =  intent.getStringExtra("namePaciente");
        fecha =  intent.getStringExtra("date");
        hora =  intent.getStringExtra("time");
        nombrePaciente =  intent.getStringExtra("namePaciente");
        type =  intent.getIntExtra("type", 5);

        mTemp = intent.getDoubleExtra("Temp", 0.0);
        mHeartRate = intent.getIntExtra("HR", 2);
        mStress = intent.getDoubleExtra("Stress", 0.0);
        mBP1 = intent.getIntExtra("BP1", 2);
        mBP2 = intent.getIntExtra("BP2", 2);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_detalle);

        ImageView imageView = findViewById(R.id.notification_icon);
        TextView paciente = findViewById(R.id.Paciente);
        TextView date = findViewById(R.id.Fecha);
        TextView time = findViewById(R.id.Hora);
        TextView medicion = findViewById(R.id.Medicion);
        TextView nombreMedicion = findViewById(R.id.nombreMedicion);
        TextView medicion2 = findViewById(R.id.Medicion2);
        TextView nombreMedicion2 = findViewById(R.id.nombreMedicion2);
        TextView mensajeSOS = findViewById(R.id.mensajeAlerta);

        toolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alerta");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario

        paciente.setText(nombrePaciente);
        date.setText(fecha);
        time.setText(hora);

        switch (type){
            case 1:
                imageView.setImageResource(R.drawable.ic_thermometer);
                nombreMedicion.setText("Temperatura: ");
                medicion.setText(mTemp + " ºC");
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_heart);
                nombreMedicion.setText("Ritmo Cardíaco: ");
                medicion.setText(String.valueOf(mHeartRate) + " ppm");
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_stress);
                nombreMedicion.setText("Nivel de estrés: ");
                medicion.setText(String.valueOf(mStress) + " \u00B5S");
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_blood_pressure);
                nombreMedicion.setText("Presión Sistólica: ");
                nombreMedicion2.setText("Presión Diastólica: ");
                medicion.setText(String.valueOf(mBP1) + " mmHg");
                medicion2.setText(String.valueOf(mBP2) + " mmHg");
                nombreMedicion2.setVisibility(View.VISIBLE);
                medicion2.setVisibility(View.VISIBLE);
                break;
            case 5:
                mensajeSOS.setVisibility(View.VISIBLE);
                medicion.setVisibility(View.GONE);
                nombreMedicion.setVisibility(View.GONE);
//                imageView.setImageResource(R.drawable.sos_icon);
                break;
            default:
                break;
        }



    }
}