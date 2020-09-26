package com.example.vitalsignscheckup.monitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.monitor.services.TemperatureService;
import com.example.vitalsignscheckup.monitor.viewmodel.TemperatureViewModel;

public class TemperatureTest extends AppCompatActivity {

    private static final String TAG = "TemperatureActivity";

    private ProgressBar progressBar;
    private TextView progressBarText;
    private Button progressButton;


    private TemperatureService mService;
    private TemperatureViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ActivityTest");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_test);

        progressBar = findViewById(R.id.progress_bar);
        progressBarText = findViewById(R.id.progress_bar_text);
        progressButton = findViewById(R.id.toggle_updates);

        mViewModel = ViewModelProviders.of(this).get(TemperatureViewModel.class);

        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleUpdates();
            }
        });

        mViewModel.getBinder().observe(this, new Observer<TemperatureService.MyBinder>() {
            @Override
            public void onChanged(TemperatureService.MyBinder myBinder) {
                if(myBinder != null){
                    Log.d(TAG, "onChanged: connected to service");
                    mService = myBinder.getService();
                }
                else {
                    Log.d(TAG, "onChanged: unbound from service");
                    mService = null;
                }
            }
        });

        mViewModel.getIsProgressUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isUpdating) {
                final Handler handler = new Handler(getMainLooper());
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isUpdating){
                            if(mViewModel.getBinder().getValue() != null){
                                mViewModel.setIsProgressUpdating(false);
                            }
                            progressBar.setProgress(mService.getProgress());
                            progressBar.setMax(mService.getMaxValue());
                            String progress = String.valueOf(100 * mService.getProgress() / mService.getMaxValue() + "%");
                            progressBarText.setText(progress);
                            handler.postDelayed(this, 100);
                        }
                        else {
                            handler.removeCallbacks(this);
                        }
                    }
                };

                if (isUpdating){
                    progressButton.setText("Pause");
                    handler.postDelayed(runnable, 100);
                }
                else {
                    if (mService.getProgress() == mService.getMaxValue()){
                        progressButton.setText("Restart");
                    }
                    else{
                        progressButton.setText("Start");
                    }
                }
            }
        });
    }

    private void toggleUpdates(){
        if(mService != null){
            if(mService.getProgress() == mService.getMaxValue()){
                mService.resetTask();
                progressButton.setText("Start");
            }
            else{
                if(mService.getIsPaused()){
                    mService.unPausePretendLongRunningTask();
                    mViewModel.setIsProgressUpdating(true);
                }
                else {
                    mService.pausePretendLongRunningTask();
                    mViewModel.setIsProgressUpdating(false);
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(mViewModel.getBinder() != null){
            unbindService(mViewModel.getServiceConnection());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        startService();
    }

    private void startService(){
        Intent serviceIntent = new Intent(this, TemperatureService.class);
        startService(serviceIntent);

        bindService();
    }

    private void bindService(){
        Intent serviceIntent = new Intent(this, TemperatureService.class);
        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);

    }
}