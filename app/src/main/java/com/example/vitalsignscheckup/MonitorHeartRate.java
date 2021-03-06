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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MonitorHeartRate extends AppCompatActivity {

    int medicion = 0;
    private static final String TAG = "MonitorHeartRate";
    DecimalFormat df = new DecimalFormat("#0");

    FirebaseAuth mAuth;
    DatabaseReference reference;  //nodo principal de la base de datos

    private ServiceHeartRate mService;                 //servicio
    private MonitorHeartRateViewModel mViewModel;      //viewModel
    private TextView ppmText;                       //medida de nivel de estres
    private HistoryAdapter historyAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_heart_rate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.heartratetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ritmo Cardiaco Actual");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView historyRV = (RecyclerView) findViewById(R.id.heartRateHistoryRecyclerView);

        historyAdapter = new HistoryAdapter();
        historyRV.setAdapter(historyAdapter);
        historyRV.setLayoutManager(new LinearLayoutManager(this));


        ppmText = findViewById(R.id.medida_heart);

        mViewModel = ViewModelProviders.of(this).get(MonitorHeartRateViewModel.class);

        mViewModel.getBinder().observe(this, new Observer<ServiceHeartRate.MyBinder>() {

            @Override
            public void onChanged(ServiceHeartRate.MyBinder myBinder) {
                if (myBinder != null) {
                    //Log.d(TAG, "onChanged: connected to service"); no se porque tira error
                    mService = myBinder.getService();
                    mService.unPausedPretendLongRunningTask();
                    mViewModel.setIsPpmUpdating(true);
                    mViewModel.setNewPpm(false);
                } else {
                    //Log.d(TAG, "onChanged: unbound from service"); no se porque tira error
                    mService = null;
                }
            }
        });

        mViewModel.getIsPpmUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isUpdating) {
                final Handler handler = new Handler(getMainLooper());
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (isUpdating) {
                            if (mViewModel.getBinder().getValue() != null) {
                                mViewModel.setIsPpmUpdating(false);
                            }
                            if (mService.getNewPpm()) {
                                double ppm = mService.getPpm();
                                String sppm =  df.format(ppm);
                                Mediciones medicion = new Mediciones(ppm, 2);
                                Log.d(TAG, "run: new ppm " + ppm);
                                ppmText.setText(sppm);
                                medicion.enviaraBD();
                                mService.setNewPpm(false);
                            }
                            handler.postDelayed(this, 1000);
                        } else {
                            handler.removeCallbacks(this);
                        }
                    }
                };

                if (isUpdating) {
                    handler.postDelayed(runnable, 100);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario
        reference.child("Mediciones").child(id).child("2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(2);
                Log.d(TAG, "onChildAdded: " + medicion.getMedicion());
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
        if (mViewModel.getBinder() != null) {
            unbindService(mViewModel.getServiceConnection());
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        startService();
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, ServiceHeartRate.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService() {
        Intent serviceIntent = new Intent(this, ServiceHeartRate.class);
        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }
}



//{
//
//private static final String TAG = "MonitorHeartRate";
//        int count = 0;
//
//private ServiceHeartRate mService;              //servicio
//private MonitorHeartRateViewModel mViewModel;   //viewModel
//private TextView ppmText;                       //medida de presión
//private HistoryAdapter historyAdapter;
//
//        FirebaseAuth mAuth;
//        DatabaseReference reference;
//
//        TextView tv1;
//        TextView tv3;
//
//        int pulsaciones = 0;
//
//        int finalPulsaciones;
//
//
//protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        Toast.makeText(MonitorHeartRate.this, "monitor_heartRate", Toast.LENGTH_SHORT).show();
//        setContentView(R.layout.activity_monitor_heart_rate);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.heartratetoolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Ritmo Cardíaco Actual");
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_back);
//
//        //si no está no sé cuál es la diferencia
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        finish();
//        }
//        });
//
//        RecyclerView historyRV = (RecyclerView) findViewById(R.id.heartRateHistoryRecyclerView);
//
//        historyAdapter = new HistoryAdapter();
//        historyRV.setAdapter(historyAdapter);
//        historyRV.setLayoutManager(new LinearLayoutManager(this));
//
//        tv1 = (TextView) findViewById(R.id.alerta_heart);
//
//        ppmText = findViewById(R.id.medida_heart);
//        ppmText.setText("   --");
//
//        tv3 = (TextView) findViewById(R.id.med_ppm);
//        tv3.setText("  ppm");
//
//        finalPulsaciones = pulsaciones;
//
//        mViewModel = ViewModelProviders.of(this).get(MonitorHeartRateViewModel.class);
//
//        //habian dos cosas para importar y no se si importe el correcto
//        mViewModel.getBinder().observe(this, new Observer<ServiceHeartRate.MyBinder>(){
//
//@Override
//public void onChanged(ServiceHeartRate.MyBinder myBinder){
//        if(myBinder != null){
//        //Log.d(TAG, "onChanged: connected to service"); no se porque tira error
//        mService = myBinder.getService();
//        mService.unPausedPretendLongRunningTask();
//        mViewModel.setIsPpmUpdating(true);
//        }
//        else {
//        //Log.d(TAG, "onChanged: unbound from service"); no se porque tira error
//        mService = null;
//        }
//        }
//        });
//
//
//        mViewModel.getIsPpmUpdating().observe(this, new Observer<Boolean>() {
//@Override
//public void onChanged(@Nullable final Boolean isUpdating) {
//final Handler handler = new Handler(getMainLooper());
//final Runnable runnable = new Runnable() {
//@Override
//public void run() {
//        if(isUpdating){
//        if(mViewModel.getBinder().getValue() != null){
//        mViewModel.setIsPpmUpdating(false);
//        }
//        if(mService.getNewPpm()){
//        double ppm = mService.getPpm();
//        Mediciones medicion = new Mediciones(ppm, 2);
//        Log.d(TAG, "run: new ppm " + ppm);
//        ppmText.setText(String.valueOf((int)ppm));
////                                medicion.enviaraBD();
//        mService.setNewPpm(false);
//        }
//        handler.postDelayed(this, 100);
//        }
//        else {
//        handler.removeCallbacks(this);
//        }
//        }
//        };
//
//        if (isUpdating){
//        handler.postDelayed(runnable, 100);
//        }
//        }
//        });
//
//        mAuth = FirebaseAuth.getInstance();
//        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
//        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario
//        reference.child("Mediciones").child(id).child("2").addChildEventListener(new ChildEventListener() {
//@Override
//public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//        System.out.println(dataSnapshot);
//        Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
//        medicion.setType(2);
//        Log.d(TAG, "onChildAdded: " + medicion.getMedicion());
//        historyAdapter.addNewHistory(medicion);
//        }
//
//@Override
//public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//        }
//
//@Override
//public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//        }
//
//@Override
//public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//        });
//        }
//
//
//
//
//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//        // Respond to the action bar's Up/Home button
//        case android.R.id.home:
//        //NavUtils.navigateUpFromSameTask(this);
//        onBackPressed();
//        return true;
//        }
//        return super.onOptionsItemSelected(item);
//        }
//
//@Override
//protected void onPause() {
//        super.onPause();
//        if(mViewModel.getBinder() != null){
//        unbindService(mViewModel.getServiceConnection());
//        }
//        }
//
//@Override
//protected void onResume() {
//        Log.d(TAG, "onResume: ");
//        super.onResume();
//        startService();
//        }
//
//private void startService(){
//        Intent serviceIntent = new Intent(this, ServiceHeartRate.class);
//        startService(serviceIntent);
//        bindService();
//        }
//
//private void bindService(){
//        Intent serviceIntent = new Intent(this, ServiceHeartRate.class);
//        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
//        }
//
//
//        }
