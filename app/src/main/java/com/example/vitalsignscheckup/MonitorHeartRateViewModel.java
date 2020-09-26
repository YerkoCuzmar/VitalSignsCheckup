package com.example.vitalsignscheckup;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ServiceConfigurationError;

public class MonitorHeartRateViewModel extends ViewModel {
    private static final String TAG = "MonitorHeartRateViewMod";
    private MutableLiveData<Boolean> isPpmUpdating = new MutableLiveData<>();
    private MutableLiveData<ServiceHeartRate.MyBinder> mBinder = new MutableLiveData<>();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: connected to service");
            ServiceHeartRate.MyBinder binder = (ServiceHeartRate.MyBinder) iBinder;
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder.postValue(null);
        }
    };


    public LiveData<Boolean> getIsPpmUpdating(){
        return isPpmUpdating;
    }

    public LiveData<ServiceHeartRate.MyBinder> getBinder(){
        return mBinder;
    }

    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }

    public void setIsPpmUpdating(Boolean isUpdating){
        isPpmUpdating.postValue(isUpdating);
    }

}
