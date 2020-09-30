package com.example.vitalsignscheckup.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;



public class Mediciones {
    private int type; // 1 = temperatura; 2 = pulso ; 3 = estres ; 4 = presion
    private String date;
    private String time;
    private int medicion;
    private int medicion2;

    public Mediciones(){}

    public Mediciones(int medicion, int type){
        this.type = type;
        this.medicion = medicion;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = new Date();
        this.date = dateFormat.format(datetime);
        this.time = timeFormat.format(datetime);
    }

    public Mediciones(int medicion, int medicion2, int type ){
        this.type = type;
        this.medicion = medicion;
        this.medicion2 = medicion2;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = new Date();
        this.date = dateFormat.format(datetime);
        this.time = timeFormat.format(datetime);
    }

    public String getDate(){ return this.date; }

    public String getTime(){ return this.time; }

    public int getMedicion(){ return this.medicion; }

    public void enviaraBD(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario nuevo
        mDataBase.child("Pacientes").child(id).child("mediciones").child(String.valueOf(this.type)).push().setValue(this);
    }

}
