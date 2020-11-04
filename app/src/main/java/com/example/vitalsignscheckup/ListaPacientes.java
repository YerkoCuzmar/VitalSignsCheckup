package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesAdapter;
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

public class ListaPacientes extends AppCompatActivity {

    private DatabaseReference mDataBase;


    private RecyclerView rvCuidadores;
    private ArrayList<PacienteCuidador> mCuidadores = new ArrayList<>();
    private MisPacientesAdapter mAdapter;

    private String id_cuidador;


    private EditText etCuidador;
    private String cuidador = "";
    FirebaseAuth mAuth;
    private Button agregar_cuidador;
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
        agregar_cuidador.setText("Agregar Paciente");
        id_cuidador = "";

        Toolbar toolbar = (Toolbar) findViewById(R.id.configToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaPacientes.this, MisPacientesActivity.class));
                finish();
            }
        });

        agregar_cuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPacienteToCuidador();
                //startActivity(new Intent(ListaCuidadores.this, MisPacientesActivity.class));
                //addCuidadorToPaciente();
                //finish();
            }
        });
        etCuidador = (EditText) findViewById(R.id.search_users);
    }

    private void addPacienteToCuidador(){
        mDataBase.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String cuidador = etCuidador.getText().toString();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String name_cuidador = ds.child("name").getValue().toString();
                        String email_cuidador = ds.child("email").getValue().toString();
                        String is = ds.child("paciente").getValue().toString();

                        Iterable<DataSnapshot> list_ids = dataSnapshot.getChildren();  //lista con los ids cuidadores

                        if (email_cuidador.equals(cuidador) && is.equals("true")){
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

                            //Log.d("id_cuidador", id);
                            //Toast.makeText(ListaCuidadores.this, "id_c es "+ id_cuidador, Toast.LENGTH_SHORT).show();
                            mDataBase.child("Usuarios").child(id).child("pacientes").child(id_cuidador).setValue(map).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){ //tarea ahora es crear datos en la bd
                                        //Toast.makeText(ListaPacientes.this, "Has agregado a " + name_cuidador +
                                        //                " a tu lista de pacientes",
                                        //        Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ListaPacientes.this, MisPacientesActivity.class));
                                        finish();
                                        agregado = true;

                                    }
                                    else{
                                        Toast.makeText(ListaPacientes.this, "No se ha podido agregar " + name_cuidador +
                                                        " a tu lista de pacientes",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                        //mCuidadores.add(new PacienteCuidador(name_cuidador, email_cuidador ,1));
                    }

                    //mAdapter = new MisPacientesAdapter(mCuidadores,1);

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
