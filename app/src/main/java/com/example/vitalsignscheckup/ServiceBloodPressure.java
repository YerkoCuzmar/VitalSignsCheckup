package com.example.vitalsignscheckup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ServiceBloodPressure extends Service {

    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);

        Toast.makeText(this, "Se inició servicio bloodPressure", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
}
