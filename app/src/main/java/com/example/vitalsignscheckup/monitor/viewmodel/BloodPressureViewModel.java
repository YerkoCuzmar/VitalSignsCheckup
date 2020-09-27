package com.example.vitalsignscheckup.monitor.viewmodel;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitalsignscheckup.monitor.services.BloodPressureService;

public class BloodPressureViewModel extends ViewModel {

    private static final String TAG = "BloodPressureViewModel";
    private MutableLiveData<Boolean> isProgressUpdating = new MutableLiveData<>();
    private MutableLiveData<BloodPressureService.MyBinder> mBinder = new MutableLiveData<>();
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: Connected to service");
            BloodPressureService.MyBinder binder = (BloodPressureService.MyBinder) iBinder;
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder.postValue(null);
        }
    };
    
    public LiveData<Boolean> getIsProgressUpdating(){ return isProgressUpdating; }
    
    public LiveData<BloodPressureService.MyBinder> getBinder(){ return mBinder; }

    public ServiceConnection getServiceConnection(){ return serviceConnection; }

    public void setIsProgressUpdating(Boolean isUpdating){
        isProgressUpdating.postValue(isUpdating);
    }
}
