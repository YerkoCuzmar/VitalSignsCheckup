package com.example.vitalsignscheckup;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitorBloodPressureViewModel extends ViewModel {
    private static final String TAG = "MonitorHeartRateViewMod";
    private MutableLiveData<Boolean> isBPUpdating = new MutableLiveData<>();
    private MutableLiveData<ServiceBloodPressure.MyBinder> mBinder = new MutableLiveData<>();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: connected to service");
            ServiceBloodPressure.MyBinder binder = (ServiceBloodPressure.MyBinder) iBinder;
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder.postValue(null);
        }
    };


    public LiveData<Boolean> getIsBPUpdating(){
        return isBPUpdating;
    }

    public LiveData<ServiceBloodPressure.MyBinder> getBinder(){
        return mBinder;
    }

    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }

    public void setIsBPUpdating(Boolean isUpdating){
        isBPUpdating.postValue(isUpdating);
    }

}
