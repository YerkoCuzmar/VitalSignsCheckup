package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.MisPacientesCuidadoresAdapter_Add;
import com.example.vitalsignscheckup.recyclerViewClasses.PacienteCuidador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaCuidadores extends AppCompatActivity {

    private DatabaseReference mDataBase;


    private RecyclerView rvCuidadores;
    private ArrayList<PacienteCuidador> mCuidadores = new ArrayList<>();
    private MisPacientesCuidadoresAdapter_Add mAdapter;



    private EditText etCuidador;
    private String cuidador = "";
    FirebaseAuth mAuth;
    private Button agregar_cuidador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cuidadores);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        rvCuidadores = (RecyclerView) findViewById(R.id.rvCuidadores);
        mAuth = FirebaseAuth.getInstance();
        rvCuidadores.setLayoutManager(new LinearLayoutManager(this));
        agregar_cuidador = (Button) findViewById(R.id.agregar_cuidador);


        agregar_cuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCuidadoresFromFirebase();
            }
        });


        etCuidador = (EditText) findViewById(R.id.search_users);

    }




    private void getCuidadoresFromFirebase(){
        mDataBase.child("Cuidadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String cuidador = etCuidador.getText().toString();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String name_cuidador = ds.child("name").getValue().toString();
                        String email_cuidador = ds.child("email").getValue().toString();


                        if (email_cuidador.equals(cuidador)){
                            Toast.makeText(ListaCuidadores.this, "SON IGUALES", Toast.LENGTH_SHORT).show();
                            Map<String, Object> map = new HashMap<>();
                            map.put("Nombre", name_cuidador);
                            String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario nuevo

                            mDataBase.child("Pacientes").child(id).child("cuidadores").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){ //tarea ahora es crear datos en la bd
                                        //startActivity(new Intent(ActivityRegister.this, MainActivity2.class));
                                        //finish();
                                        Toast.makeText(ListaCuidadores.this, "Has agregado a " + name_cuidador +
                                                " a tu lista de cuidadores",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        //System.out.println("salleee");
                                        Toast.makeText(ListaCuidadores.this, "No se ha podido agregar " + name_cuidador +
                                                " a tu lista de cuidadores",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(ListaCuidadores.this, "SON DISTINTOS " + cuidador    , Toast.LENGTH_SHORT).show();
                        }

                        mCuidadores.add(new PacienteCuidador(name_cuidador, email_cuidador ,1));

                    }
                    mAdapter = new MisPacientesCuidadoresAdapter_Add(mCuidadores,1);
                    rvCuidadores.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
