package com.example.vitalsignscheckup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ListNotifications extends AppCompatActivity {

    private RecyclerView rvNotifications;
//    private NotificacionesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notifications);

        rvNotifications = (RecyclerView) findViewById(R.id.rvMainCuidadores);
//        glm = new GridLayoutManager(root.getContext(), 1);
//        rvCuidadores.setLayoutManager(glm);
//        rvCuidadores.setAdapter(adapter);
    }
}