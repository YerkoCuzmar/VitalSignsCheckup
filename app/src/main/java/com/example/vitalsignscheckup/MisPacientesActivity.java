package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.PacienteCuidador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private MisPacientesAdapter adapter;
    private DatabaseReference mDataBase;
    FirebaseAuth mAuth;
    private FloatingActionButton add_cuidador; //para agregar cuidadores
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cuidadores);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        add_cuidador = (FloatingActionButton) findViewById(R.id.add_cuidadores); //agregar cuidadores
        Toolbar toolbar = (Toolbar) findViewById(R.id.configToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MisPacientesActivity.this, MainActivityCuidadores.class));
                finish();
            }
        });

        add_cuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UserRecord userRecord = mAuth.getUserByEmail("alex2@gmail.com");
                startActivity(new Intent(MisPacientesActivity.this, ListaPacientes.class));
                finish();
            }
        });
        rvCuidadores = (RecyclerView) findViewById(R.id.rvCuidadores);
        adapter = new MisPacientesAdapter(dataSet(), 0);
        glm = new GridLayoutManager(this, 1);
        rvCuidadores.setLayoutManager(glm);
        rvCuidadores.setAdapter(adapter);
    }

    private ArrayList<PacienteCuidador> dataSet() {
        ArrayList<PacienteCuidador> data = new ArrayList<>();
//        data.add(new PacienteCuidador("Elva Stoncito", "Imagine.Dragons@ufale.chile", R.drawable.ic_awesome_user_circle));
        //data.add(new PacienteCuidador("Radioactive", "Ima.ragons@2.cia", R.drawable.ic_awesome_user_circle));
        //data.add(new PacienteCuidador("Asomecha", "AsomechaDragons@3.ciwei", R.drawable.ic_awesome_user_circle));

        //Agregar a data los cuidadores correspondientes al consultar la BD

        String id = mAuth.getCurrentUser().getUid();
        mDataBase.child("Usuarios").child(id).child("pacientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //Toast.makeText(MisPacientesActivity.this, "ds " + ds.child("email") , Toast.LENGTH_SHORT).show();
                    //Iterable<DataSnapshot> list_ids = ds.child("cuidadores").child("correo").getChildren();
                    //Toast.makeText(MisPacientesActivity.this, "lista: " + list_ids, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MisPacientesActivity.this  , "dsa " + ds.child("email"), Toast.LENGTH_SHORT).show();
                    PacienteCuidador cuidador = new PacienteCuidador(ds.child("name").getValue().toString(),
                            ds.child("email").getValue().toString(),R.drawable.ic_awesome_user_circle);
                    if (data.contains(cuidador)){
                        Toast.makeText(MisPacientesActivity.this, "Ya es tu paciente", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        data.add(cuidador);
                        //adapter.notifyItemInserted(1);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
        return data;
    }

}
