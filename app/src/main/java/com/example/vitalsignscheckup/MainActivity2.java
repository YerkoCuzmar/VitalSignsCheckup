package com.example.vitalsignscheckup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    //get nombre y correo
    private TextView mTextViewName;
    private TextView mTextViewEmail;

    String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(MainActivity2.this, "Inicio2", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //get nombre y correo
        SharedPreferences preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        name = preferences.getString("name", null);
        email = preferences.getString("email", null);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.get_nombre);
        TextView navEmail = (TextView) headerView.findViewById(R.id.get_correo);
        navUsername.setText(name);
        navEmail.setText(email);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_perfil, R.id.nav_mi_cuidador, R.id.nav_configuracion,R.id.nav_cerrar_sesion )
                .setDrawerLayout(drawer)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.menu_mis_familiares, R.string.menu_mis_familiares);
        drawer.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_perfil) {
                } else if (id == R.id.nav_mi_cuidador) {
                    Intent cuidadorIntent = new Intent(getApplicationContext(), MisCuidadoresActivity.class);
                    startActivity(cuidadorIntent);
                } else if (id == R.id.nav_configuracion) {
//                    Toast.makeText(MainActivity2.this, "Configuración", Toast.LENGTH_SHORT).show();

                    Intent configIntent = new Intent(getApplicationContext(), ConfigActivity.class);
                    startActivity(configIntent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity2.this, ActivityLogin.class));
                    finish(); //para no volver atras cuando se cierre sesion
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //getUserInfo();  //Para actualizar los datos del usuario en la barra lateral izquierda
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(MainActivity2.this, "InicioResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*
    private void getUserInfo(){

        mTextViewName.setText(name);
        mTextViewEmail.setText(email);
    }
     */
}