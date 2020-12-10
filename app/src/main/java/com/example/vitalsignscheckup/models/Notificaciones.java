package com.example.vitalsignscheckup.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Notificaciones {
    private int type; // 1 = temperatura; 2 = pulso ; 3 = estres ; 4 = presion ; 5 = SOS
    private String date;
    private String time;
    private int medcionTemp;
    private int medcionHeartRate;
    private int medcionStress;
    private int medcionBloodPressure1;
    private int medcionBloodPressure2;

    public Notificaciones(){}

    public Notificaciones(int type) {
        this.type = type;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = new Date();
        this.date = dateFormat.format(datetime);
        this.time = timeFormat.format(datetime);
    }

    public Notificaciones(int type, String date, String time) {
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDateTime() throws ParseException {
        String sDateTime = this.date + "-" + this.time;
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.getDefault());
        return parser.parse(sDateTime);
    }

    public void enviaraBD() {
        final ArrayList<String> idCuidadores = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario nuevo
//        mDataBase.child("Usuarios").child(id).child("cuidadores").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d("enviarNotificacion", "onDataChange: " + dataSnapshot.getKey());
//                for (DataSnapshot cuidadorSnapshot : dataSnapshot.getChildren()) {
//                    idCuidadores.add(cuidadorSnapshot.getKey());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        mDataBase.child("Notificaciones").child(id).push().setValue(this);
    }
}
