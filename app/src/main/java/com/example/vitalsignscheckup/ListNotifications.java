package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.models.Notificaciones;
import com.example.vitalsignscheckup.recyclerViewClasses.NotificacionAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ListNotifications extends AppCompatActivity implements NotificacionAdapter.OnNotificacionListener{

    private RecyclerView rvNotifications;
    private NotificacionAdapter adapter;
    private String namePaciente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notifications);

        Toolbar toolbar = findViewById(R.id.notificationToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notificaciones");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent = getIntent();
        namePaciente = intent.getStringExtra("pacienteName");

        rvNotifications = (RecyclerView) findViewById(R.id.rvMainCuidadores);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificacionAdapter(this);
        rvNotifications.setAdapter(adapter);
    }

    @Override
    public void onNotificationClick(int position) throws ParseException {
        Notificaciones notificacion = adapter.getPaciente(position);
        Intent intent = new Intent(this, NotificationDetail.class);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        format.format(notificacion.getDateTime());
        intent.putExtra("type", notificacion.getType());
        intent.putExtra("dateTime", notificacion.getType());
        intent.putExtra("name", namePaciente);
    }
}