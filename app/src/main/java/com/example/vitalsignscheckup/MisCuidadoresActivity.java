package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.PacienteCuidador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MisCuidadoresActivity extends AppCompatActivity {

    private RecyclerView rvCuidadores;
    private GridLayoutManager glm;
    private MisPacientesCuidadoresAdapter adapter;

    private FloatingActionButton add_cuidador; //para agregar cuidadores


    private DatabaseReference mDataBase;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cuidadores);

        add_cuidador = (FloatingActionButton) findViewById(R.id.add_cuidadores); //agregar cuidadores

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

        add_cuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UserRecord userRecord = mAuth.getUserByEmail("alex2@gmail.com");
                startActivity(new Intent(MisCuidadoresActivity.this, ListaCuidadores.class));
                finish();
            }
        });

        rvCuidadores = (RecyclerView) findViewById(R.id.rvCuidadores);
        adapter = new MisPacientesCuidadoresAdapter(dataSet(), 1);
        glm = new GridLayoutManager(this, 1);
        rvCuidadores.setLayoutManager(glm);
        rvCuidadores.setAdapter(adapter);

    }

    private ArrayList<PacienteCuidador> dataSet() {
        ArrayList<PacienteCuidador> data = new ArrayList<>();
        //data.add(new PacienteCuidador("Elva Stoncito", "Imagine.Dragons@ufale.chile", R.drawable.ic_awesome_user_circle));
        //data.add(new PacienteCuidador("Radioactive", "Ima.ragons@2.cia", R.drawable.ic_awesome_user_circle));
        data.add(new PacienteCuidador("Asomecha", "AsomechaDragons@3.ciwei", R.drawable.ic_awesome_user_circle));

        //Agregar a data los cuidadores correspondientes al consultar la BD

        String id = mAuth.getCurrentUser().getUid();
        mDataBase.child("Pacientes").child(id).child("cuidadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //Toast.makeText(MisCuidadoresActivity.this, "ds " + ds.child("Correo") , Toast.LENGTH_SHORT).show();
                    //Iterable<DataSnapshot> list_ids = ds.child("cuidadores").child("correo").getChildren();
                    //Toast.makeText(MisCuidadoresActivity.this, "lista: " + list_ids, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MisCuidadoresActivity.this, "dsa " + ds.child("Correo"), Toast.LENGTH_SHORT).show();
                    data.add(new PacienteCuidador(ds.child("Nombre").getValue().toString(),
                            ds.child("Correo").getValue().toString(),
                            R.drawable.ic_awesome_user_circle));
                            adapter.notifyItemInserted(1);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return data;
    }


}
