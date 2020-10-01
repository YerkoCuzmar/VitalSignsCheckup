package com.example.vitalsignscheckup.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;



public class Mediciones {
    private int type; // 1 = temperatura; 2 = pulso ; 3 = estres ; 4 = presion
    private String date;
    private String time;
    private double medicion;
    private double medicion2;

    public Mediciones(){}

    public Mediciones(double medicion, int type){
        this.type = type;
        this.medicion = medicion;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = new Date();
        this.date = dateFormat.format(datetime);
        this.time = timeFormat.format(datetime);
    }

    public Mediciones(double medicion, double medicion2, int type ){
        this.type = type;
        this.medicion = medicion;
        this.medicion2 = medicion2;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = new Date();
        this.date = dateFormat.format(datetime);
        this.time = timeFormat.format(datetime);
    }

    public Mediciones(String date, String time, double medicion) {
        this.date = date;
        this.time = time;
        this.medicion = medicion;
    }

    public Mediciones(int type, String date, String time, double medicion, double medicion2) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.medicion = medicion;
        this.medicion2 = medicion2;
    }

    public String getDate(){ return this.date; }

    public String getTime(){ return this.time; }

    public double getMedicion(){ return round2(this.medicion); }

    public int getType() { return type; }

    public double getMedicion2() { return medicion2; }

    public void setType(int type) { this.type = type; }

    public void setDate(String date) { this.date = date; }

    public void setTime(String time) { this.time = time; }

    public void setMedicion(double medicion) { this.medicion = medicion; }

    public void setMedicion2(double medicion2) { this.medicion2 = medicion2; }

    private double round2(double num){
        return Math.round(num *100)/100.0;
    }

    public void enviaraBD(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario nuevo
        mDataBase.child("Usuarios").child(id).child("mediciones").child(String.valueOf(this.type)).push().setValue(this);
    }

}
