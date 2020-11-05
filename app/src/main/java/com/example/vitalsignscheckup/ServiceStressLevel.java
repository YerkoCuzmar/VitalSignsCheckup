package com.example.vitalsignscheckup;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ServiceStressLevel extends Service {

    private static final String TAG = "ServiceStressLevel"; // para debug

    private Runnable runnable;
    private BroadcastReceiver br;

    int count = 0;

    private IBinder mBinder = new MyBinder();
    private Handler mHandler;
    private Boolean isPaused;

    private Boolean newStressLevel;

    Double us = 0.0;

    int lag = 30;
    double threshold = 3.5;
    double influence = 0;

    /*
    int pulsaciones = 0;
    int dif = 0;
    int sample_rate = 10;
    int ppm = 0;
    int pulsaciones2 = 0;

    int eda_value;

    int value_i = 0;
    int value_rate = 1;
     */

    int DATA_SIZE = 100;
    boolean COLLECT_DATA = true;

    ArrayList<Double> data = new ArrayList<Double>();

    //SignalDetector signalDetector = new SignalDetector();

    /*
    HashMap<String, List> resultsMap = signalDetector.analyzeDataForSignals(data, lag, threshold, influence, data.size());
    List<Integer> signalsList = resultsMap.get("signals");
    List<Double> filteredDataList = resultsMap.get("filteredData");
    List<Double> datos = resultsMap.get("data");

     */

    /*
    List<Double> data = new ArrayList<Double>();
    HashMap<String, List> resultsMap;
    List<Integer> signalsList;
    List<Double> filteredDataList;
    List<Double> datos;
*/

    int i;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //permite recuperar una instancia del servicio a través de un punto de acceso
    //conexión entre activity y servicio
    public class MyBinder extends Binder{
        ServiceStressLevel getService(){
            return ServiceStressLevel.this; // se retorna instancia del servicio
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        isPaused = true;
        br = new SLDataReciever();
        newStressLevel = false;
        IntentFilter filt = new IntentFilter("analogData");
        this.registerReceiver(br, filt);
    }

    //como si estuviese corriendon una tarea largamente
    public void startPretendLongRunningTask(){
        if(runnable == null) {
            runnable = new Runnable() {

                @Override
                public void run() {
                    if(data.size() > DATA_SIZE)
                        calcularSLSensores();
//                    calcularSLantiguo();
//                    ppm = eda_value;
                    mHandler.postDelayed(this, 1000);
                }
            };
        }
        mHandler.postDelayed(runnable, 1000);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        unregisterReceiver(br);
        stopSelf();
    }

    public void unPausedPretendLongRunningTask(){
        isPaused = false;
        startPretendLongRunningTask();
    }

    public Double getSL(){
        return us;
    }

    public void setNewStressLevel(Boolean bool){ newStressLevel = bool; }

    public Boolean getNewStressLevel(){ return newStressLevel; }

    public class SLDataReciever extends BroadcastReceiver {
        int[] puertos;
        int porteda;
        int poseda;

        public SLDataReciever(){
            SharedPreferences preferences = getSharedPreferences("EDAConfig", Context.MODE_PRIVATE);
            porteda = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "9")));

            if(porteda != 9){
                poseda = porteda - 1;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(COLLECT_DATA) {
                double eda_value = intent.getExtras().getIntArray("analogData")[poseda];
                data.add(eda_value);
            }
        }
    }


    public void calcularSLSensores() {
        // TODO:IMPLEMENTAR CALCULO
        while (data.size() < DATA_SIZE) {

        }
        COLLECT_DATA = false;
        Double suma = 0.0;
        Double calculo = 0.0;
        //ArrayList<Double> moda = new ArrayList<Double>();


        for(int i = 0; i < DATA_SIZE; i++) {
            calculo = ((data.get(i)/Math.pow(2, 16)) * 3) / 0.12;
            suma += calculo;
            //moda.add(calculo);
        }

        us = suma / DATA_SIZE;

        newStressLevel = true;

        data.clear();
        COLLECT_DATA = true;
    }

    public double signalToEDA(double adc){
        int n = 16;
        int vcc = 3;
        return ((adc/Math.pow(2, n))*vcc)/0.12;
    }

}
