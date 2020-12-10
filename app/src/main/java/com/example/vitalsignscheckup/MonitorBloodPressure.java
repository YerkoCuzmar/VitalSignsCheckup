package com.example.vitalsignscheckup;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.models.Mediciones;
import com.example.vitalsignscheckup.models.Notificaciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MonitorBloodPressure extends AppCompatActivity {

    private static final String TAG = "MonitorBloodPressure";
    DecimalFormat df = new DecimalFormat("#0");

    FirebaseAuth mAuth;
    DatabaseReference reference;  //nodo principal de la base de datos

    private ServiceBloodPressure mService;                 //servicio
    private MonitorBloodPressureViewModel mViewModel;      //viewModel
    private TextView bpText;                       //medida de nivel de estres
    private TextView bp2Text;                       //medida de nivel de estres
    private HistoryAdapter historyAdapter;

    private static final double minNormalValue = 90.0;
    private static final double minNormalValue2 = 60.0;

    private static final double maxNormalValue = 120.0;
    private static final double maxNormalValue2 = 80.0;

    private static final double minValue = 40.0;
    private static final double minValue2 = 30.0;

    private static final double maxValue = 220.0;
    private static final double maxValue2 = 160.0;

    private static final boolean useMinMax = false;

    Date lastNotificationDate;
    int minTimeMinutes = 2;
    int calibrationMinutes = 1;
    boolean isFirstAlert = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_blood_pressure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bloodPressureToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Presión Actual");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView historyRV = (RecyclerView) findViewById(R.id.BPHistoryRecyclerView);

        historyAdapter = new HistoryAdapter();
        historyRV.setAdapter(historyAdapter);
        historyRV.setLayoutManager(new LinearLayoutManager(this));

        lastNotificationDate = new Date();

        bpText = findViewById(R.id.bp_medicion_mmhg);
        bp2Text = findViewById(R.id.bp_medicion_mmhg2);

        mViewModel = ViewModelProviders.of(this).get(MonitorBloodPressureViewModel.class);

        mViewModel.getBinder().observe(this, new Observer<ServiceBloodPressure.MyBinder>(){

            @Override
            public void onChanged(ServiceBloodPressure.MyBinder myBinder){
                if(myBinder != null){
                    //Log.d(TAG, "onChanged: connected to service"); no se porque tira error
                    mService = myBinder.getService();
                    mService.unPausedPretendLongRunningTask();
                    mViewModel.setIsBPUpdating(true);
                    mViewModel.setNewBP(false);
                }
                else {
                    //Log.d(TAG, "onChanged: unbound from service"); no se porque tira error
                    mService = null;
                }
            }
        });

        mViewModel.getIsBPUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isUpdating) {
                final Handler handler = new Handler(getMainLooper());
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isUpdating){
                            if(mViewModel.getBinder().getValue() != null){
                                mViewModel.setIsBPUpdating(false);
                            }
                            if (mService.getNewBp()){
                                double bp = mService.getBp();
                                double bp2 = mService.getBp2();
                                String sbp =  df.format(bp);
                                String sbp2 =  df.format(bp2);
                                Mediciones medicion = new Mediciones(bp, bp2, 4);
                                Log.d(TAG, "run: newTemp " + bp + "/" + bp2);
                                bpText.setText(sbp);
                                bp2Text.setText(sbp2);
                                medicion.enviaraBD();
                                if (isAlertable(medicion.getMedicion(), medicion.getMedicion2())){
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
                                    String sDateTime = medicion.getDate() + " " + medicion.getTime();
                                    Date alertDateTime;
                                    try {
                                        alertDateTime = sdf.parse(sDateTime);
                                        long minutesDiff = (alertDateTime.getTime() - lastNotificationDate.getTime())/(1000*60);
                                        if (isFirstAlert){
                                            Log.d(TAG, "run: primera alerta");
                                            if(minutesDiff >= calibrationMinutes){
                                                Log.d(TAG, "run: enviar primera alerta");
                                                Notificaciones notificacion = new Notificaciones(medicion.getType(), medicion.getMedicion());
                                                notificacion.enviaraBD();
                                                lastNotificationDate = alertDateTime;
                                                isFirstAlert = false;
                                            }
                                        }
                                        else {
                                            if(minutesDiff >= minTimeMinutes){
                                                Log.d(TAG, "run: alertas");
                                                Notificaciones notificacion = new Notificaciones(medicion.getType(), medicion.getMedicion());
                                                notificacion.enviaraBD();
                                                lastNotificationDate = alertDateTime;
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                mService.setNewBp(false);
                            }

                            handler.postDelayed(this, 1000);
                        }
                        else {
                            handler.removeCallbacks(this);
                        }
                    }
                };

                if (isUpdating){
                    handler.postDelayed(runnable, 100);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario
        reference.child("Mediciones").child(id).child("4").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(4);
                Log.d(TAG, "onChildAdded: " + medicion.getMedicion() + "/" + medicion.getMedicion2());
                historyAdapter.addNewHistory(medicion);
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mViewModel.getBinder() != null){
            unbindService(mViewModel.getServiceConnection());
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        startService();
    }

    private void startService(){
        Intent serviceIntent = new Intent(this, ServiceBloodPressure.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceIntent = new Intent(this, ServiceBloodPressure.class);
        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    private boolean isInRange(double medicion){
        if(useMinMax){
            return medicion >= minValue && medicion <= maxValue;
        }
        return true;
    };

    private boolean isInRange2(double medicion){
        if(useMinMax){
            return medicion >= minValue2 && medicion <= maxValue2;
        }
        return true;
    };

    private boolean isUnder(double medicion){
        return medicion < minNormalValue;
    };

    private boolean isUnder2(double medicion){
        return medicion < minNormalValue2;
    };

    private boolean isUpper(double medicion){
        return medicion > maxNormalValue;
    };

    private boolean isUpper2(double medicion){
        return medicion > maxNormalValue2;
    };

    private boolean isAlertable(double medicion, double medicion2){
        boolean alertableMed1 = isInRange(medicion)  && (isUnder(medicion) || isUpper(medicion));
        boolean alertableMed2 = isInRange2(medicion2)  && (isUnder2(medicion2) || isUpper2(medicion2));
        return alertableMed1 || alertableMed2;
    }

}