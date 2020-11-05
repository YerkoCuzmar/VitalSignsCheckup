package com.example.vitalsignscheckup;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.vitalsignscheckup.models.Notificaciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ServiceNotification extends Service {

    public static final String CHANNEL_ID = "notificationServiceChannel";

    private IBinder mBinder = new MyBinder();
    private String id;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ArrayList<String> pacientes = new ArrayList<>();

    Date serviceStartDate;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder{
        ServiceNotification getService(){
            return ServiceNotification.this; // se retorna instancia del servicio
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        serviceStartDate = new Date();
        Log.d("ServiceNotification", "onCreate: " + serviceStartDate);
        Intent notificationIntent = new Intent(this, MainActivityCuidadores.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Vital Signs Checkup")
                .setContentText("Aqui recibirás las alertas de tus pacientes")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intention, int flags, int idArranque) {
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid(); //obtener id del usuario
        getPacientes(id);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    private void getPacientes(String id) {
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        reference.child("Usuarios").child(id).child("pacientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    pacientes.add(String.valueOf(ds.getKey()));
                }
                getNotifications(5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNotifications(int key){

        int nPacientes = pacientes.size();
        int i;
        for(i = 0; i < nPacientes; i++) {

            final int finalI = i;
            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Notificaciones").child(pacientes.get(i)).child(String.valueOf(key)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Notificaciones notification = dataSnapshot.getValue(Notificaciones.class);
                        try {
                            assert notification != null;
                            Date notificationDate = notification.getDateTime();
                            if(notificationDate.after(serviceStartDate)){
                                Log.d("dataSnapshot", "onChildAdded: notificacion despues");
                                getPacientesName(pacientes.get(finalI));
                            }
                            else {
                                Log.d("dataSnapshot", "onChildAdded: notificacion antes");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


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
        }
    }

    private void getPacientesName(String id){
        reference = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        reference.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals("name")){
                        crearNotificacion(String.valueOf(ds.getValue()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void crearNotificacion(String name) {
        Intent notificationIntent = new Intent(this, MainActivityCuidadores.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alerta SOS")
                .setContentText("Su paciente " + name + " ha enviado una alerta")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

    }

}
