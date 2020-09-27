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

@RequiresApi(api = Build.VERSION_CODES.N)
public class MonitorBloodPressure extends AppCompatActivity {

    private static final String TAG = "MonitorBloodPressure";
    private ServiceBloodPressure mService;              //servicio
    private MonitorBloodPressureViewModel mViewModel;   //viewModel

    TextView hist1;
    TextView hist2;

    TextView textView;
    TextView textView2;

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
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hist1 = (TextView)findViewById(R.id.hist1);
        hist2 = (TextView)findViewById(R.id.hist2);

        textView = (TextView)findViewById(R.id.bp_medicion_mmhg);
        textView2 = (TextView)findViewById(R.id.bp_medicion_mmhg2);

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
                            String progress = String.valueOf(mService.getPpm());
                            // TODO: AGREGAR TV2 y HISTORIAL
                            textView.setText(progress);
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

    public void viewHistory(View view){
        Intent viewHistoryIntent = new Intent(view.getContext(), checkHistory.class);
        viewHistoryIntent.putExtra("origin", "bloodPressure");
        startActivity(viewHistoryIntent);
    }


}