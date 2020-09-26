package com.example.vitalsignscheckup.config;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.vitalsignscheckup.DoubleSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SignalDetector {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HashMap<String, List> analyzeDataForSignals(List<Double> data, int lag, Double threshold, Double influence, int DATA_SIZE) {

        // init stats instance
        DoubleSummary stats = new DoubleSummary();

        // the results (peaks, 1 or -1) of our algorithm
        List<Integer> signals = new ArrayList<Integer>(Collections.nCopies(DATA_SIZE, 0));

        // filter out the signals (peaks) from our original list (using influence arg)
        List<Double> filteredData = new ArrayList<Double>(data);

        // the current average of the rolling window
        List<Double> avgFilter = new ArrayList<Double>(Collections.nCopies(DATA_SIZE, 0.0d));

        // the current standard deviation of the rolling window
        List<Double> stdFilter = new ArrayList<Double>(Collections.nCopies(DATA_SIZE, 0.0d));

        // init avgFilter and stdFilter
        double ECG_V, ECG_mV;
        int G_ECG, VCC;

        if (data.get(0) >15000){
            for (int i = 0; i < DATA_SIZE; i ++){

                VCC = 3;      // operating voltage
                G_ECG = 1000; // sensor gain

                ECG_V = (data.get(i)/Math.pow(2, 16) - 0.5)*VCC/G_ECG;

                ECG_mV = ECG_V*1000;

                data.set(i,ECG_mV);

            }
        }



        for (int i = 0; i < lag; i++) {
            stats.accept(data.get(i));
        }
        avgFilter.set(lag - 1, stats.getAverage());
        stdFilter.set(lag - 1, (stats.getStandardDeviation())); // getStandardDeviation() uses sample variance


        // loop input starting at end of rolling window
        for (int i = lag; i < DATA_SIZE; i++) {

            // if the distance between the current value and average is enough standard deviations (threshold) away
            if (Math.abs((data.get(i) - avgFilter.get(i - 1))) > threshold * stdFilter.get(i - 1)) {

                // this is a signal (i.e. peak), determine if it is a positive or negative signal
                if (data.get(i) > avgFilter.get(i - 1)) {
                    signals.set(i, 1);
                } else {
                    signals.set(i, -1);
                }

                // filter this signal out using influence
                filteredData.set(i, (influence * data.get(i)) + ((1 - influence) * filteredData.get(i - 1)));
            } else {
                // ensure this signal remains a zero
                signals.set(i, 0);
                // ensure this value is not filtered
                filteredData.set(i, data.get(i));
            }
            DoubleSummary stats2 = new DoubleSummary();
            // update rolling average and deviation
            for (int j = i - lag; j < i; j++) {
                stats2.accept(filteredData.get(j));
            }
            avgFilter.set(i, stats2.getAverage());
            stdFilter.set(i, (stats2.getStandardDeviation()));
            //stats.clear();
        }

        HashMap<String, List> returnMap = new HashMap<String, List>();
        returnMap.put("signals", signals);
        returnMap.put("filteredData", filteredData);
        returnMap.put("avgFilter", avgFilter);
        returnMap.put("stdFilter", stdFilter);
        returnMap.put("data", data);

        return returnMap;

    } // end
}

