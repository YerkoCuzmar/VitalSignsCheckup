package com.example.vitalsignscheckup.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.lifecycle.ViewModelProviders;

import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.recyclerViewClasses.MainCuidadoresAdapter;
import com.example.vitalsignscheckup.recyclerViewClasses.Pacientes;

import java.util.ArrayList;

public class HomeFragmentCuidadores extends Fragment {

    private RecyclerView rvCuidadores;
    private GridLayoutManager glm;
    private MainCuidadoresAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflar o cargar el layout para el Fragment
        View root = inflater.inflate(R.layout.fragment_home_cuidadores, container, false);

        rvCuidadores = (RecyclerView) root.findViewById(R.id.rvMainCuidadores);
        adapter = new MainCuidadoresAdapter(dataSet());
        glm = new GridLayoutManager(root.getContext(), 1);
        rvCuidadores.setLayoutManager(glm);
        rvCuidadores.setAdapter(adapter);

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
        ArrayList<Pacientes> data = new ArrayList<>();
        //data.add(new PacienteCuidador("Elva Stoncito", "Imagine.Dragons@ufale.chile", R.drawable.ic_awesome_user_circle));
        //data.add(new PacienteCuidador("Radioactive", "Ima.ragons@2.cia", R.drawable.ic_awesome_user_circle));
        data.add(new Pacientes("Luisito Comunica", "12", "13", "11", "87", R.drawable.ic_awesome_user_circle));
        data.add(new Pacientes("Andres Calamaro", "120", "13", "116", "87", R.drawable.ic_awesome_user_circle));
        data.add(new Pacientes("Calamardo Gonzales", "12", "143", "11", "897", R.drawable.ic_awesome_user_circle));

        //Agregar a data los cuidadores correspondientes al consultar la BD

        return data;
    }

}