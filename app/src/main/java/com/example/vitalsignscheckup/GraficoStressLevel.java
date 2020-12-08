package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.vitalsignscheckup.models.Mediciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraficoStressLevel extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private String idPaciente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        idPaciente = intent.getStringExtra("pacienteId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vergrafico);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        Toolbar toolbar = (Toolbar) findViewById(R.id.graficoToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gráfico Nivel de estrés");

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        double x,y;
        x = -5.0;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();

        /*for (int i = 0; i < 500; i++){
            x = x + 0.1;
            y = Math.sin(x);
            series.appendData(new DataPoint(x,y), true, 500);
        }*/
        //graph.addSeries(series);  //RECORDAR ESTOOOOOOOOO!!!!!!!!!!!
        Log.d("Paciente", idPaciente);
        final int[] i = {0};
        mDataBase.child("Mediciones").child(idPaciente).child("3").addChildEventListener(new ChildEventListener() {  //el "1" es por la temperatura
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                //Log.d("medicion: ",  String.valueOf(medicion.getMedicion()));
                //Log.d("fecha: ",  medicion.getDate());
                //Log.d("hora: ",  medicion.getTime());

                series.appendData(new DataPoint(i[0],medicion.getMedicion()), true, 50);
                i[0] = i[0] + 1;

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
        //graph.setScaleY(70);
        //graph.setScaleX(50);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        //graph.getViewport().setScrollableY(true); // enables vertical scrolling
        //graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        //graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        graph.addSeries(series);


    }
}
