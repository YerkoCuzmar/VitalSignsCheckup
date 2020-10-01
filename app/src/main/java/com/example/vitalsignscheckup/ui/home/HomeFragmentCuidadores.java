package com.example.vitalsignscheckup.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.recyclerViewClasses.MainCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.Pacientes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

//import androidx.lifecycle.ViewModelProviders;

public class HomeFragmentCuidadores extends Fragment {
    
    private static final String TAG = "HomeFragmentCuidadores";

    private RecyclerView rvCuidadores;
    private GridLayoutManager glm;
    private MainCuidadoresAdapter adapter;

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflar o cargar el layout para el Fragment
        View root = inflater.inflate(R.layout.fragment_home_cuidadores, container, false);

        rvCuidadores = (RecyclerView) root.findViewById(R.id.rvMainCuidadores);
        adapter = new MainCuidadoresAdapter();
        glm = new GridLayoutManager(root.getContext(), 1);
        rvCuidadores.setLayoutManager(glm);
        rvCuidadores.setAdapter(adapter);


        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        mDataBase.child("Usuarios").child(userId).child("pacientes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pacientes paciente = new Pacientes();
                paciente.setId(dataSnapshot.getKey());
                paciente.setName(dataSnapshot.child("name").getValue().toString());
//                dataSnapshot.child("mediciones").child("1").getValue()
                adapter.addPaciente(paciente);
                adapter.notifyDataSetChanged();
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
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        // setContentView(R.layout.activity_main);
        // Para hacer findViewById, debes hacerlo con la referencia de root que es tu layout
        // Ejemplo: TextView textView = root.findViewById(R.id.textView);
    }



    private ArrayList<Pacientes> dataSet() {
        Log.d(TAG, "dataSet: ");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        ArrayList<Pacientes> data = new ArrayList<>();

        String userId = mAuth.getCurrentUser().getUid();
        mDataBase.child("Usuarios").child(userId).child("pacientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Pacientes paciente = new Pacientes();
                    paciente.setId(ds.getKey());
                    paciente.setName(ds.child("name").getValue().toString());
                    data.add(paciente);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG, "dataSet: for paciente");
        Log.d(TAG, "dataSet: " + data);
        for( Pacientes paciente : data){
            Log.d(TAG, "dataSet: paciente");
            mDataBase.child("Usuarios").child(paciente.getId()).child("mediciones").child("1").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: Temp");
                    int i = data.indexOf(paciente);
                    if(dataSnapshot.getValue() != null){
                        paciente.setLastTemp(Objects.requireNonNull(dataSnapshot.child("medicion").getValue()).toString());
                    }
                    else {
                        paciente.setLastTemp("--");
                    }
                    data.set( i, paciente);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        Log.d(TAG, "dataSet: despues for");

        for( Pacientes paciente : data){
            mDataBase.child("Usuarios").child(paciente.getId()).child("mediciones").child("2").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i = data.indexOf(paciente);
                    if(dataSnapshot.getValue() != null){
                        paciente.setLastHeartRate(Objects.requireNonNull(dataSnapshot.child("medicion").getValue()).toString());
                    }
                    else {
                        paciente.setLastHeartRate("--");
                    }
                    data.set( i, paciente);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

        for( Pacientes paciente : data){
            mDataBase.child("Usuarios").child(paciente.getId()).child("mediciones").child("3").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i = data.indexOf(paciente);
                    if(dataSnapshot.getValue() != null){
                        paciente.setLastStress(Objects.requireNonNull(dataSnapshot.child("medicion").getValue()).toString());
                    }
                    else {
                        paciente.setLastStress("--");
                    }
                    data.set( i, paciente);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

        for( Pacientes paciente : data){
            mDataBase.child("Usuarios").child(paciente.getId()).child("mediciones").child("4").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i = data.indexOf(paciente);
                    if(dataSnapshot.getValue() != null) {
                        String med1 = Objects.requireNonNull(dataSnapshot.child("medicion").getValue()).toString();
                        String med2 = Objects.requireNonNull(dataSnapshot.child("medicion2").getValue()).toString();
                        String med = med1 + "/" + med2;
                        paciente.setLastPressure(med);
                    }
                    else {
                        paciente.setLastPressure("--/--");
                    }
                    data.set( i, paciente);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

        return data;
    }

}