package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.models.Mediciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CuidadorMonitorBloodPressure extends AppCompatActivity {

    private static final String TAG = "MonitorBloodPressure";
    DecimalFormat df = new DecimalFormat("#0");
    private String idPaciente;
    private String namePaciente;

    FirebaseAuth mAuth;
    DatabaseReference reference;  //nodo principal de la base de datos
    private TextView bpText;   //medida de nivel de presion 1
    private TextView bpText2;   //medida de nivel de presion 2
    private HistoryAdapter historyAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        idPaciente = intent.getStringExtra("pactienteId");
        namePaciente = intent.getStringExtra("pacienteName");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_blood_pressure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bloodPressureToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(namePaciente);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView historyRV = (RecyclerView) findViewById(R.id.BPHistoryRecyclerView);

        historyAdapter = new HistoryAdapter();
        historyRV.setAdapter(historyAdapter);
        historyRV.setLayoutManager(new LinearLayoutManager(this));

        bpText = findViewById(R.id.bp_medicion_mmhg);
        bpText2 = findViewById(R.id.bp_medicion_mmhg2);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        reference.child("Mediciones").child(idPaciente).child("4").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot);
                Mediciones medicion = dataSnapshot.getValue(Mediciones.class);
                medicion.setType(4);
                Log.d(TAG, "onChildAdded: " + medicion.getMedicion() + "/" + medicion.getMedicion2());
                bpText.setText(df.format(medicion.getMedicion()));
                bpText2.setText(df.format(medicion.getMedicion2()));
                historyAdapter.addNewHistory(medicion);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }


}
