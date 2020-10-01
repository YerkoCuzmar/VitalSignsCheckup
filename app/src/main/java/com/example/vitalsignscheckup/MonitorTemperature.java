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
public class MonitorTemperature extends AppCompatActivity {

    int medicion = 0;
    private static final String TAG = "MonitorStressLevel";
    DecimalFormat df = new DecimalFormat("#0.00");

    FirebaseAuth mAuth;
    DatabaseReference reference;  //nodo principal de la base de datos

    private ServiceTemperature mService;                 //servicio
    private MonitorTemperatureViewModel mViewModel;      //viewModel
    private TextView tempText;                       //medida de nivel de estres
    private HistoryAdapter historyAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_temperature);
        Toolbar toolbar = (Toolbar) findViewById(R.id.temperatureToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Temperatura Actual");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView historyRV = (RecyclerView) findViewById(R.id.historyRecyclerView);

        historyAdapter = new HistoryAdapter();
        historyRV.setAdapter(historyAdapter);
        historyRV.setLayoutManager(new LinearLayoutManager(this));

        //TODO: definir tempText

        tempText = findViewById(R.id.medida_temp);

        mViewModel = ViewModelProviders.of(this).get(MonitorTemperatureViewModel.class);

        mViewModel.getBinder().observe(this, new Observer<ServiceTemperature.MyBinder>(){

            @Override
            public void onChanged(ServiceTemperature.MyBinder myBinder){
                if(myBinder != null){
                    //Log.d(TAG, "onChanged: connected to service"); no se porque tira error
                    mService = myBinder.getService();
                    mService.unPausedPretendLongRunningTask();
                    mViewModel.setIsTempUpdating(true);
                    mViewModel.setNewTemp(false);
                }
                else {
                    //Log.d(TAG, "onChanged: unbound from service"); no se porque tira error
                    mService = null;
                }
            }
        });

        mViewModel.getIsTempUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isUpdating) {
                final Handler handler = new Handler(getMainLooper());
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isUpdating){
                            if(mViewModel.getBinder().getValue() != null){
                                mViewModel.setIsTempUpdating(false);
                            }
                            if (mService.getNew_temp()){

                                String temp =  df.format(mService.getTemp());
                                Mediciones medicion = new Mediciones(mService.getTemp(), 1);
                                Log.d(TAG, "run: newTemp" + temp);
                                tempText.setText(temp);
                                medicion.enviaraBD();
                                mService.setNew_temp(false);
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
        reference = FirebaseDatabase.getInstance().getReference("Usuario");  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario
        reference.child(id).child("mediciones").child("1").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot);
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(1);
                Log.d(TAG, "onChildAdded: " + medicion.getMedicion());
                tempText.setText(df.format(medicion.getMedicion()));
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
        Intent serviceIntent = new Intent(this, ServiceTemperature.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceIntent = new Intent(this, ServiceTemperature.class);
        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    public void viewHistory(View view){
        if(historyAdapter != null){
            Mediciones med = new Mediciones(medicion, 1);
            med.enviaraBD();
            medicion++;
        }
    }


}
