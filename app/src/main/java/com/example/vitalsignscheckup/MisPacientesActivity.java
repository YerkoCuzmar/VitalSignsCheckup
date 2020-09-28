package com.example.vitalsignscheckup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.PacienteCuidador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MisPacientesActivity extends AppCompatActivity {

    private RecyclerView rvCuidadores;
    private GridLayoutManager glm;
    private MisPacientesCuidadoresAdapter adapter;
    private DatabaseReference mDataBase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cuidadores);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.configToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvCuidadores = (RecyclerView) findViewById(R.id.rvCuidadores);
        adapter = new MisPacientesCuidadoresAdapter(dataSet(), 0);
        glm = new GridLayoutManager(this, 1);
        rvCuidadores.setLayoutManager(glm);
        rvCuidadores.setAdapter(adapter);
    }

    private ArrayList<PacienteCuidador> dataSet() {
        ArrayList<PacienteCuidador> data = new ArrayList<>();
        data.add(new PacienteCuidador("Elva Stoncito", "Imagine.Dragons@ufale.chile", R.drawable.ic_awesome_user_circle));
        data.add(new PacienteCuidador("Radioactive", "Ima.ragons@2.cia", R.drawable.ic_awesome_user_circle));
        data.add(new PacienteCuidador("Asomecha", "AsomechaDragons@3.ciwei", R.drawable.ic_awesome_user_circle));

        //Agregar a data los cuidadores correspondientes al consultar la BD
        /*String id = mAuth.getCurrentUser().getUid();
        mDataBase.child("Pacientes").child(id).child("cuidadores").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    data.add(new PacienteCuidador(ds.child("Nombre").getValue().toString(), ds.child("Correo").getValue().toString(),
                            R.drawable.ic_awesome_user_circle));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        return data;
    }

}
