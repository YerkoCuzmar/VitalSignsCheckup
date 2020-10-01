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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.models.Mediciones;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MonitorBloodPressure extends AppCompatActivity {

    private static final String TAG = "MonitorBloodPressure";
    private ServiceBloodPressure mService;              //servicio
    private MonitorBloodPressureViewModel mViewModel;   //viewModel
    private HistoryAdapter historyAdapter;
    RecyclerView historyRV;

    TextView textViewBp;
    TextView textViewBp2;

//    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    Date date = new Date();
//    String dateformatted = dateFormat.format(date);
//    String histroy_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "BloodPressure", Toast.LENGTH_SHORT).show();
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

        historyAdapter = new HistoryAdapter();
        historyRV = (RecyclerView) findViewById(R.id.BPHistoryRecyclerView);
        historyRV.setAdapter(historyAdapter);
        historyRV.setLayoutManager(new LinearLayoutManager(this));

        textViewBp = (TextView) findViewById(R.id.bp_medicion_mmhg);
        textViewBp2 = (TextView) findViewById(R.id.bp_medicion_mmhg2);

        mViewModel = ViewModelProviders.of(this).get(MonitorBloodPressureViewModel.class);

        //habian dos cosas para importar y no se si importe el correcto
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
                            if(mService.getNewBp()){
                                Log.d(TAG, "run: " + mService.getNewBp());
                                String bp = String.valueOf(mService.getBp());
                                String bp2 = String.valueOf(mService.getBp2());
                                textViewBp.setText(bp);
                                textViewBp2.setText(bp2);
                                Mediciones medicion = new Mediciones(mService.getBp(), mService.getBp2(),2 );
                                historyAdapter.addNewHistory(medicion);
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
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        startService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mViewModel.getBinder() != null){
            unbindService(mViewModel.getServiceConnection());
        }
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



}