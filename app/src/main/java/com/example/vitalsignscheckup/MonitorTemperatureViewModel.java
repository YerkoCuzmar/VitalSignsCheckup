package com.example.vitalsignscheckup;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitorTemperatureViewModel extends ViewModel {
    //TODO: REVISAR "ppm"
    private static final String TAG = "TemperatureViewModel";
    private MutableLiveData<Boolean> isTempUpdating = new MutableLiveData<>();
    private MutableLiveData<ServiceTemperature.MyBinder> mBinder = new MutableLiveData<>();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: connected to service");
            ServiceTemperature.MyBinder binder = (ServiceTemperature.MyBinder) iBinder;
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder.postValue(null);
        }
    };


    public LiveData<Boolean> getIsTempUpdating(){
        return isTempUpdating;
    }

    public LiveData<ServiceTemperature.MyBinder> getBinder(){
        return mBinder;
    }

    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }

    public void setIsTempUpdating(Boolean isUpdating){
        isTempUpdating.postValue(isUpdating);
    }

}
