package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.recyclerViewClasses.MisCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.PacienteCuidador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaCuidadores extends AppCompatActivity {

    private DatabaseReference mDataBase;


    private RecyclerView rvCuidadores;
    private ArrayList<PacienteCuidador> mCuidadores = new ArrayList<>();
    private MisCuidadoresAdapter mAdapter;

    private String id_cuidador;

    String name_cuidador;
    String email_cuidador;
    String is;


    private EditText etCuidador;
    private String cuidador = "";
    FirebaseAuth mAuth;
    private Button agregar_cuidador;
    private TextView textHeader;
    private boolean agregado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cuidadores);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        rvCuidadores = (RecyclerView) findViewById(R.id.rvCuidadores);

        mAuth = FirebaseAuth.getInstance();
        rvCuidadores.setLayoutManager(new LinearLayoutManager(this));
        agregar_cuidador = (Button) findViewById(R.id.agregar_cuidador);
        textHeader = (TextView) findViewById(R.id.tvAddOption);
        textHeader.setText("Ingrese el mail del nuevo cuidador");

        id_cuidador = "";

        Toolbar toolbar = (Toolbar) findViewById(R.id.configToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agregar Cuidadores");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaCuidadores.this, MisCuidadoresActivity.class));
                finish();
            }
        });

        agregar_cuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCuidadorToPaciente();
                //startActivity(new Intent(ListaCuidadores.this, MisCuidadoresActivity.class));
                //addCuidadorToPaciente();
                //finish();
            }
        });
        etCuidador = (EditText) findViewById(R.id.search_users);
    }

    private void addCuidadorToPaciente(){
        mDataBase.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String cuidador = etCuidador.getText().toString();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        name_cuidador = ds.child("name").getValue().toString();
                        email_cuidador = ds.child("email").getValue().toString();
                        is = ds.child("paciente").getValue().toString();

                        Iterable<DataSnapshot> list_ids = dataSnapshot.getChildren();  //lista con los ids de los usuarios
                        if (email_cuidador.equals(cuidador) && is.equals("false")){
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", name_cuidador);
                            map.put("email", email_cuidador);
                            String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario actual


                            for (DataSnapshot d1 : list_ids){           //para obtener el id del cuidador
                                String uid = ds.getKey().toString();
                                if (d1.child("name").getValue().toString().equals(name_cuidador)){
                                    if (d1.child("email").getValue().toString().equals(email_cuidador)){
                                        Log.d("el id es ", id_cuidador);
                                        id_cuidador = uid;          //id del cuidador
                                    }
                                }
                            }
                            mDataBase.child("Usuarios").child(id).child("cuidadores").child(id_cuidador).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    Log.d("Entrando", "entro a mdabatase");
                                    if(task2.isSuccessful()){ //tarea ahora es crear datos en la bd

                                        startActivity(new Intent(ListaCuidadores.this, MisCuidadoresActivity.class));
                                        finish();
                                        agregado = true;
                                        Toast.makeText(ListaCuidadores.this, "Has agregado a " + name_cuidador +
                                                        " a tu lista de cuidadores",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{

                                        startActivity(new Intent(ListaCuidadores.this, MisCuidadoresActivity.class));
                                        finish();
                                        Toast.makeText(ListaCuidadores.this, "No se ha podido agregar " + name_cuidador +
                                                        " a tu lista de cuidadores",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        //mCuidadores.add(new PacienteCuidador(name_cuidador, email_cuidador ,1));
                    }

                    //mAdapter = new MisCuidadoresAdapter(mCuidadores,1);

                    //rvCuidadores.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }
}
