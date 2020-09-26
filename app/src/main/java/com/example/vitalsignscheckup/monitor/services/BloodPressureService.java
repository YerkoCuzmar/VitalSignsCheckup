package com.example.vitalsignscheckup.monitor.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Objects;

public class BloodPressureService extends Service {

    private static final String TAG = "BloodPressureService";

    private IBinder binder = new MyBinder();
    private Handler handler;
    private int progress, maxValue;
    private Boolean isPaused;
    private Runnable runnable;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        progress = 0;
        isPaused = true;
        maxValue = 100;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder{

        public BloodPressureService getService(){
            return BloodPressureService.this;
        }
    }


    public void startPretendLongRunningTask(){
        if (runnable == null){
            runnable = new Runnable() {
                @Override
                public void run() {
                    if(progress >= maxValue || isPaused){
                        Log.d(TAG, "run: pause.");
                        handler.removeCallbacks(this);
                        pausePretendLongRunningTask();
                    }
                    else {
                        Log.d(TAG, "run: progress: " + progress);
                        progress += 5;
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    public void pausePretendLongRunningTask() {
        isPaused = true;
    }

    public void unPausePretendLongRunningTask(){
        isPaused = false;
        startPretendLongRunningTask();
    }

    public Boolean getIsPaused(){ return isPaused; }

    public int getProgress(){ return progress; }

    public int getMaxValue(){ return maxValue; }

    public void resetTask(){ progress = 0; }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf(); // TODO: detiene el servicio al cerrar la app del appmanager
    }
}
