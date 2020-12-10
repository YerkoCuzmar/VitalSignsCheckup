package com.example.vitalsignscheckup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GraficoTemperature extends AppCompatActivity implements View.OnClickListener {


    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private String idPaciente;

    //fechas y generar grafico

    Button fechai, fechal, generar;
    //EditText edfechai, edfechal;

    private int diai, mesi, anioi;
    private int dial, mesl, aniol;

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
        getSupportActionBar().setTitle("Gráfico Temperatura");


        fechai = (Button) findViewById(R.id.fecha_i);
        fechal = (Button) findViewById(R.id.fecha_l);
        generar = (Button) findViewById(R.id.generar_g);

        //EditText edfechai = (EditText) findViewById(R.id.efechai);
        //EditText edfechal = (EditText) findViewById(R.id.efechal);



        fechai.setOnClickListener(this);
        fechal.setOnClickListener(this);
        generar.setOnClickListener(this);


        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        double x,y;
        x = -5.0;
        //GraphView graph = (GraphView) findViewById(R.id.graph);
        //series = new LineGraphSeries<DataPoint>();

        /*for (int i = 0; i < 500; i++){
            x = x + 0.1;
            y = Math.sin(x);
            series.appendData(new DataPoint(x,y), true, 500);
        }*/
        //graph.addSeries(series);  //RECORDAR ESTOOOOOOOOO!!!!!!!!!!!
        //Log.d("Paciente", idPaciente);
        final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

        /*series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(GraficoTemperature.this, "Fecha y hora: "+ formato.format(dataPoint.getX()) + " Temperatura: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });*/

        final int[] i = {0};





}


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view == fechai){
            final Calendar c  = Calendar.getInstance();
            diai = c.get(Calendar.DAY_OF_MONTH);
            mesi = c.get(Calendar.MONTH);
            anioi = c.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    EditText edfechai=(EditText)findViewById(R.id.efechai);
                    edfechai.setText(i2 + "/" + (i1+1)+ "/" + i);
                }
            }
                    , diai, mesi, anioi);
            datePickerDialog.show();
        }
        if (view == fechal){
            Log.d("Imprimiendo", "nada");
            final Calendar c2 = Calendar.getInstance();
            dial = c2.get(Calendar.DAY_OF_MONTH);
            mesl = c2.get(Calendar.MONTH);
            aniol = c2.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Log.d("fechass1 ", String.valueOf(i));
                    Log.d("fechass2 ", String.valueOf(i1));
                    Log.d("fechass3 ", String.valueOf(i2));

                    //edfechal.setText(i2 + "/" + (i1+1)+ "/" + i);
                    EditText edfechal=(EditText)findViewById(R.id.efechal);
                    edfechal.setText(i2 + "/" + (i1+1) + "/" + i);
                    //Log.d("editext", edfechal.getText().toString());
                }
            }
            , dial, mesl, aniol);
            datePickerDialog.show();
        }
        if (view == generar){
            EditText edfechai = (EditText)findViewById(R.id.efechai);
            EditText edfechal = (EditText)findViewById(R.id.efechal);

            final String fe_i = edfechai.getText().toString();
            final String fe_l = edfechal.getText().toString();

            final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
            final SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            GraphView graph = (GraphView) findViewById(R.id.graph);
            final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

            mDataBase.child("Mediciones").child(idPaciente).child("1").addChildEventListener(new ChildEventListener() {  //el "1" es por la temperatura
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                    //Log.d("medicion: ",  String.valueOf(medicion.getMedicion()));
                    //Log.d("fecha: ",  medicion.getDate());
                    //Log.d("hora: ",  medicion.getTime());
                    try {
                        Date date = formato2.parse(medicion.getDate());
                        Date date_i = formato2.parse(fe_i);
                        Date date_l = formato2.parse(fe_l);

                        Log.d("DATE", String.valueOf(date));
                        Log.d("DATE_I", String.valueOf(date_i));
                        Log.d("DATE_L", String.valueOf(date_l));

                        if (medicion.getMedicion() < 50 && date.after(date_i) && date.before(date_l)){
                            String fecha_hora = medicion.getDate() + " " + medicion.getTime();
                            Log.d("string", fecha_hora);
                            Date date_hora = formato.parse(fecha_hora);
                            Log.d("fecha", String.valueOf(date_hora));
                            Log.d("date: ", String.valueOf(medicion.getDate()));
                            Log.d("hora: ", String.valueOf(medicion.getTime()));
                            series.appendData(new DataPoint(date_hora, medicion.getMedicion()), false,50);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
            graph.getViewport().setMinY(34);
            graph.getViewport().setMaxY(44);

            //graph.getViewport().setXAxisBoundsManual(true);

            //graph.getViewport().setMinX(0);
            //graph.getViewport().setMaxX(10);

            graph.addSeries(series);

            series.setDrawDataPoints(true);
            series.setDataPointsRadius(15);



            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(GraficoTemperature.this));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

            //graph.getGridLabelRenderer().setHumanRounding(false);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(GraficoTemperature.this, "Fecha y hora: "+ formato.format(dataPoint.getX()) + " Temperatura: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
