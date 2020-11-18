package com.example.vitalsignscheckup.ui.home;

import android.content.Intent;
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

import com.example.vitalsignscheckup.MainActivityCuidadorPaciente;
import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.recyclerViewClasses.MainCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.Pacientes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HomeFragmentCuidadores extends Fragment implements MainCuidadoresAdapter.OnPacienteListener{
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
        adapter = new MainCuidadoresAdapter(this);
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
                if(dataSnapshot.child("place").exists()){
                    paciente.setPlace(dataSnapshot.child("place").getValue().toString());
                }
                else{
                    paciente.setPlace("No informa");
                }
                Log.d(TAG, "onChildAdded: img" + paciente.getImage());
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
        return data;
    }

    @Override
    public void onPacienteClick(int position) {
        Pacientes paciente = adapter.getPaciente(position);
        Intent intent = new Intent(this.getActivity(), MainActivityCuidadorPaciente.class); //pasa de actividad a monitoreo de tal sensor
        Log.d("pacienteID", paciente.getId());
        intent.putExtra("pacienteId", paciente.getId()); // antes de startearlo
        intent.putExtra("pacienteName", paciente.getName()); // se le entrega info
        startActivity(intent);
        getActivity().finish();
    }


}