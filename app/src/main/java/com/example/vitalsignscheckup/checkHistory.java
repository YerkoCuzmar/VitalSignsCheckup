package com.example.vitalsignscheckup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class checkHistory extends AppCompatActivity {
        TextView textViewfilename;
        TextView textViewHistory;
        String historyFilename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_history);
        textViewHistory = (TextView) findViewById(R.id.history);
        textViewHistory.setMovementMethod(new ScrollingMovementMethod());

        textViewfilename = (TextView) findViewById(R.id.textview_filename);

        String origin = getIntent().getStringExtra("origin");
        switch (origin){
            case "heartRate":
                historyFilename = "heart_rate_history.txt";
                break;
            case "bloodPressure":
                historyFilename = "blood_pressure_history.txt";
                break;
            default:
                historyFilename = "";
                break;
        }
        textViewfilename.setText(historyFilename);
        try {
            InputStreamReader archivo = new InputStreamReader(openFileInput(historyFilename));
            BufferedReader br = new BufferedReader(archivo);
            String bitacora = "";
            String line = br.readLine();
            while (line!= null){
                bitacora = bitacora + (line + "\n");
                line = br.readLine();
            }
            br.close();
            archivo.close();
            textViewHistory.setText(bitacora);
        } catch (IOException e) {
        }
    }

    public void cleanHistory(View view) throws IOException {
        try {
            OutputStreamWriter output = new OutputStreamWriter(openFileOutput(historyFilename, Activity.MODE_PRIVATE));
            output.write("");
            output.flush();
            output.close();
        } catch (IOException e) {
        }
        finish();
    }
}