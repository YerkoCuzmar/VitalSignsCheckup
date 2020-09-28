package com.example.vitalsignscheckup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaCuidadores extends AppCompatActivity {

    private DatabaseReference mDataBase;


    private RecyclerView rvCuidadores;
    private ArrayList<PacienteCuidador> mCuidadores = new ArrayList<>();
    private MisPacientesCuidadoresAdapter_Add mAdapter;

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

        id_cuidador = "";


        agregar_cuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCuidadorToPaciente();
            }
        });
        etCuidador = (EditText) findViewById(R.id.search_users);
    }

    private void addCuidadorToPaciente(){
        mDataBase.child("Cuidadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String cuidador = etCuidador.getText().toString();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String name_cuidador = ds.child("name").getValue().toString();
                        String email_cuidador = ds.child("email").getValue().toString();

                        Iterable<DataSnapshot> list_ids = dataSnapshot.getChildren();  //lista con los ids cuidadores

                        if (email_cuidador.equals(cuidador)){
                            Map<String, Object> map = new HashMap<>();
                            map.put("Nombre", name_cuidador);
                            map.put("Correo", email_cuidador);
                            String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario nuevo


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
                            Toast.makeText(ListaCuidadores.this, "id_c es "+ id_cuidador, Toast.LENGTH_SHORT).show();
                            mDataBase.child("Pacientes").child(id).child("cuidadores").child(id_cuidador).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){ //tarea ahora es crear datos en la bd
                                        Toast.makeText(ListaCuidadores.this, "Has agregado a " + name_cuidador +
                                                " a tu lista de cuidadores",
                                                Toast.LENGTH_SHORT).show();
                                        agregado = true;
                                        return;
                                    }
                                    else{
                                        Toast.makeText(ListaCuidadores.this, "No se ha podido agregar " + name_cuidador +
                                                " a tu lista de cuidadores",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
