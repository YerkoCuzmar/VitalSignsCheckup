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
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ServiceTemperature extends Service {

    private static final String TAG = "ServiceTemperature"; // para debug

    private Runnable runnable;
    private BroadcastReceiver br;

    Double temp;

    int count = 0;
    int sample_rate;
    int value_i;
    int value_rate;

    private IBinder mBinder = new MyBinder();
    private Handler mHandler;
    private Boolean isPaused;

    int DATA_SIZE = 1000;
    boolean COLLECT_DATA = true;


    List<Double> data = new ArrayList<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //permite recuperar una instancia del servicio a través de un punto de acceso
    //conexión entre activity y servicio
    public class MyBinder extends Binder{
        ServiceTemperature getService(){
            return ServiceTemperature.this; // se retorna instancia del servicio
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        isPaused = true;
        br = new TempDataReciever();
        IntentFilter filt = new IntentFilter("analogData");
        this.registerReceiver(br, filt);
    }

    //como si estuviese corriendon una tarea largamente
    public void startPretendLongRunningTask(){
        if(runnable == null) {
            runnable = new Runnable() {

                @Override
                public void run() {
//                    calcularTempSensores();
                    test();
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

    public Double getTemp(){
        return temp;
    }

    public class TempDataReciever extends BroadcastReceiver {
        int[] puertos;
        int portbvp, portecg, porttemp, porteda;
        int postemp;

        public TempDataReciever(){
            puertos = new int[]{9, 9, 9, 9}; //puertos van del 1-4, 9 no altera el orden del sort
            SharedPreferences preferences = getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                portbvp = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[0] = portbvp;
            }

            preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                portecg = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[1] = portecg;
            }

            preferences = getSharedPreferences("TempConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                porttemp = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[2] = porttemp;
            }

            preferences = getSharedPreferences("EDAConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                porteda = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[3] = porteda;
            }

            Arrays.sort(puertos);
            String sPuertos = Arrays.toString(puertos);
            postemp = sPuertos.indexOf(String.valueOf(porteda));
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(COLLECT_DATA){
                double temp_value = intent.getExtras().getIntArray("analogData")[postemp];
                data.add(temp_value);
            }
        }
    }

    public void test(){
        System.out.println(transformarVaC(33628.0));;
    }


    public void calcularTempSensores() {

        while (data.size() < sample_rate) {

        }
        COLLECT_DATA = false;
        temp = transformDataToSingleTemp(data);

        data.clear();
        COLLECT_DATA = true;
    }


    public void calcularTempantiguo() {
        // TODO: IMPLEMENTAR CALCULO

        Log.d("data size", String.valueOf(data.size()));

        Log.d("collect ", String.valueOf(COLLECT_DATA));

        temp = transformDataToSingleTemp(data);

        COLLECT_DATA = false;

        count++;
        value_i = count*sample_rate;
        value_rate = value_rate + 1;

        if(sample_rate*value_rate >= DATA_SIZE) {

            count = 0;
            value_i = 0;
            value_rate = 1;
            COLLECT_DATA = true;
        }
    }

    public double transformarVaC(Double adc){
        if(adc < 17361.0){
            adc = 17361.0;
        }
        else if(adc > 50161.0){
            adc = 50161.0;
        }

        int vcc = 3;
        int n = 16;
        double a0 = 1.127664514 * Math.pow(10, -3);
        double a1 = 2.34282709 * Math.pow(10, -4);
        double a2 = 8.77303013 * Math.pow(10, 8);
        double ntc_v = adc * vcc / Math.pow(2, n);
        double ntc_ohm = (Math.pow(10, 4) * ntc_v) / (vcc - ntc_v);
        double temp_k = 1 / (a0 + a1 * Math.log(ntc_ohm) + a2 * Math.pow( Math.log(ntc_ohm) , 3));
        return temp_k - 273.15;
    }

    private double transformDataToSingleTemp(List<Double> data){
        double sum = 0;
        for (Double raw_data : data){
            sum += transformarVaC(raw_data);
        }
        return sum / data.size();
    }

}
