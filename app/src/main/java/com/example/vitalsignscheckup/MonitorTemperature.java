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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MonitorTemperature extends AppCompatActivity  {

    private static final String TAG = "MonitorTemperature";

    private ServiceTemperature mService;              //servicio
    private MonitorTemperatureViewModel mViewModel;   //viewModel
    private TextView tempText;                       //medida de temperatura

    TextView tv3;


    DecimalFormat df = new DecimalFormat("#0.000");

//    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    Date date = new Date();
//    String dateformatted = dateFormat.format(date);
//    String histroy_log;

    int DATA_SIZE = 1000;
    boolean COLLECT_DATA = true;
    List<Double> data = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(MonitorTemperature.this, "monitor_heartRate", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_monitor_temperature);
        Toolbar toolbar = (Toolbar) findViewById(R.id.temperatureToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        //si no está no sé cuál es la diferencia
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tempText = findViewById(R.id.medida_temp);

        mViewModel = ViewModelProviders.of(this).get(MonitorTemperatureViewModel.class);

        //habian dos cosas para importar y no se si importe el correcto
        mViewModel.getBinder().observe(this, new Observer<ServiceTemperature.MyBinder>(){

            @Override
            public void onChanged(ServiceTemperature.MyBinder myBinder){
                if(myBinder != null){
                    Log.d(TAG, "onChanged: connected to service");
                    mService = myBinder.getService();
                    mService.unPausedPretendLongRunningTask();
                    mViewModel.setIsTempUpdating(true);
                }
                else {
                    Log.d(TAG, "onChanged: unbound from service");
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
                            String progress = String.valueOf(mService.getPpm());
                            tempText.setText(progress);
                            // TODO: AGREGAR AL HISTORIAL
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
        Intent serviceIntent = new Intent(this, ServiceTemperature.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceIntent = new Intent(this, ServiceTemperature.class);
        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    public void viewHistory(View view){
        Intent viewHistoryIntent = new Intent(view.getContext(), checkHistory.class);
        viewHistoryIntent.putExtra("origin", "heartRate");
        startActivity(viewHistoryIntent);
    }

}
