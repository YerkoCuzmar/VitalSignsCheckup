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
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MonitorStressLevel extends AppCompatActivity {

    private static final String TAG = "MonitorStressLevel";

    DecimalFormat df = new DecimalFormat("#0.00");

    private ServiceStressLevel mService;                 //servicio
    private MonitorStressLevelViewModel mViewModel;      //viewModel
    private HistoryAdapter historyAdapter;
    RecyclerView historyRV;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    private TextView stressText;
    private TextView stressParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_stress_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.stressToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Estrés Actual");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        historyAdapter = new HistoryAdapter();
        historyRV = (RecyclerView) findViewById(R.id.stressHistoryRecyclerView);
        historyRV.setAdapter(historyAdapter);
        historyRV.setLayoutManager(new LinearLayoutManager(this));

        stressText = (TextView) findViewById(R.id.medida_stress);
        stressParam = (TextView) findViewById(R.id.alerta_stress);

        mViewModel = ViewModelProviders.of(this).get(MonitorStressLevelViewModel.class);

        mViewModel.getBinder().observe(this, new Observer<ServiceStressLevel.MyBinder>(){

            @Override
            public void onChanged(ServiceStressLevel.MyBinder myBinder){
                if(myBinder != null){
                    //Log.d(TAG, "onChanged: connected to service"); no se porque tira error
                    mService = myBinder.getService();
                    mService.unPausedPretendLongRunningTask();
                    mViewModel.setIsStressUpdating(true);
                }
                else {
                    //Log.d(TAG, "onChanged: unbound from service"); no se porque tira error
                    mService = null;
                }
            }
        });

        mViewModel.getIsStressUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isUpdating) {
                final Handler handler = new Handler(getMainLooper());
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isUpdating){
                            if(mViewModel.getBinder().getValue() != null){
                                mViewModel.setIsStressUpdating(false);
                            }
                            if(mService.getNewStressLevel()){
                                Double stress = mService.getSL();
                                String text = "";
                                Mediciones medicion = new Mediciones(stress, 3);
                                if(stress < 5){
                                    text = "Niveles de Estrés Normales";
                                }else{
                                    text = "Niveles de Estrés Elevados";
                                }
                                stressText.setText(df.format(stress));
                                stressParam.setText(text);
                                historyAdapter.addNewHistory(medicion);
                                mService.setNewStressLevel(false);
                            }
                            handler.postDelayed(this, 100);
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
        Intent serviceIntent = new Intent(this, ServiceStressLevel.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceIntent = new Intent(this, ServiceStressLevel.class);
        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    public void viewHistory(View view){
        Intent viewHistoryIntent = new Intent(view.getContext(), checkHistory.class);
        viewHistoryIntent.putExtra("origin", "stressLevel");
        startActivity(viewHistoryIntent);
    }

}