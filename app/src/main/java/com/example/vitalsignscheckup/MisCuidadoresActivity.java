package com.example.vitalsignscheckup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesCuidadoresAdapter_Add;
import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesCuidadoresAdapter_Delete;
import com.example.vitalsignscheckup.recyclerViewClasses.PacienteCuidador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MisCuidadoresActivity extends AppCompatActivity {

    private RecyclerView rvCuidadores;
    private GridLayoutManager glm;
    private MisPacientesCuidadoresAdapter_Delete adapter;

    private Button add_cuidador; //para agregar cuidadores
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cuidadores);

        add_cuidador = (Button) findViewById(R.id.add_cuidadores); //agregar cuidadores
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

            }
        });

        rvCuidadores = (RecyclerView) findViewById(R.id.rvCuidadores);
        adapter = new MisPacientesCuidadoresAdapter_Delete(dataSet(), 1);
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

        return data;
    }

}
