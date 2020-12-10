package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.models.Notificaciones;
import com.example.vitalsignscheckup.recyclerViewClasses.NotificacionesAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.Pacientes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListNotifications extends AppCompatActivity implements NotificacionesAdapter.OnPacienteListener {

    private Toolbar toolbar;

    private RecyclerView rvNotifications;
    private NotificacionesAdapter adapter;
    private GridLayoutManager glm;

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    private String idPaciente;
    private String namePaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notifications);

        Intent intent = getIntent();
        idPaciente = intent.getStringExtra("pacienteId");
        namePaciente = intent.getStringExtra("pacienteName");


        toolbar = findViewById(R.id.notification_toolbar);
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

        rvNotifications = (RecyclerView) findViewById(R.id.rvNotificaciones);

        adapter = new NotificacionesAdapter();
        glm = new GridLayoutManager(this, 1);
        rvNotifications.setLayoutManager(glm);
        rvNotifications.setAdapter(adapter);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mDataBase.child("Notificaciones").child(idPaciente).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Notificaciones notificacion = dataSnapshot.getValue(Notificaciones.class);
                adapter.addNotificacion(notificacion);
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
    public void onPacienteClick(int position) {
        Notificaciones notificacion = adapter.getNotificacion(position);
        Intent intent = new Intent(ListNotifications.this, Notificaciones.class); //tiene que ser el activity donde se mostrará alerta
        intent.putExtra("Temp", notificacion.getMedicionTemp());
        intent.putExtra("HR", notificacion.getMedicionStress());
        intent.putExtra("Stress", notificacion.getMedicionHeartRate());
        intent.putExtra("BP1", notificacion.getMedicionBloodPressure1());
        intent.putExtra("BP2", notificacion.getMedicionBloodPressure2());
        startActivity(intent);
        ListNotifications.this.finish();
    }
}