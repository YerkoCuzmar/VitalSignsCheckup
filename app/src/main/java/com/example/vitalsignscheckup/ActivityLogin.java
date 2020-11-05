package com.example.vitalsignscheckup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {

    //layout_login
    private EditText etEmailLogin, etPassLogin; //datos que se pide

    private Button btnIngresar; //para logearse
    private TextView tvRegister; //para ir a pantalla registrarse

    //datos que se llenan
    private String email = "";
    private String pass = "";

    String id_user;

    SharedPreferences preferences;
    SharedPreferences.Editor spEditor;

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //instanceLoginElements();

        etEmailLogin = (EditText) findViewById(R.id.editTextEmailLog);
        etPassLogin = (EditText) findViewById(R.id.editTextPasswordLog);
        btnIngresar = (Button) findViewById(R.id.cirLoginButtonLog);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivityIntent = new Intent(v.getContext(), ActivityRegister.class);
                startActivity(registerActivityIntent);
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                email = etEmailLogin.getText().toString();
                pass = etPassLogin.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()){
                    loginUser();
                }
                else{
                    Toast.makeText(ActivityLogin.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();

                }
            }
        });

        /*
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent mainActivityIntent = new Intent(v.getContext(), MainActivityCuidadores.class);

                String id_user = mAuth.getUid();
                mDataBase.child("Usuarios").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            //Log.d("id ", ds.child(""))
                            if (ds.getKey().toString().equals(id_user)){
                                String is = ds.child("paciente").getValue().toString();
                                if (is.equals("true")){
                                    //Toast.makeText(ActivityLogin.this, "Has entrado como paciente", Toast.LENGTH_SHORT).show();
                                    //datos para confirmar en la BD
                                    String email = etEmailLogin.getText().toString();
                                    String pass = etPassLogin.getText().toString();
                                    startActivity(new Intent(ActivityLogin.this, MainActivity2.class));
                                    finish(); //para prohibir que se pueda volver a esa vista
                                }
                                else{
                                    //Toast.makeText(ActivityLogin.this, "Has entrado como cuidador", Toast.LENGTH_SHORT).show();
                                    //datos para confirmar en la BD
                                    String email = etEmailLogin.getText().toString();
                                    String pass = etPassLogin.getText().toString();
                                    startActivity(new Intent(ActivityLogin.this, MainActivityCuidadores.class));
                                    finish(); //para prohibir que se pueda volver a esa vista
                                }
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //startActivity(mainActivityIntent);
            }
        });

         */

    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    id_user = mAuth.getUid();
                    mDataBase.child("Usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                //Log.d("id ", ds.child(""))
                                if (ds.getKey().toString().equals(id_user)){
                                    String is = ds.child("paciente").getValue().toString();

                                    preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                    spEditor = preferences.edit();
                                    spEditor.putString("email", email);
                                    spEditor.putString("name", ds.child("name").getValue().toString());
                                    spEditor.apply();

                                    if (is.equals("true")){
                                        //Toast.makeText(ActivityLogin.this, "Has entrado como paciente", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ActivityLogin.this, MainActivity2.class));
                                        finish(); //para prohibir que se pueda volver a esa vista
                                    }
                                    else{
                                        //Toast.makeText(ActivityLogin.this, "Has entrado como cuidador", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ActivityLogin.this, MainActivityCuidadores .class));
                                        finish(); //para prohibir que se pueda volver a esa vista
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                }
                else{
                    Toast.makeText(ActivityLogin.this, "No se ha podido iniciar sesion, compruebe los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*private void instanceLoginElements () {

        etEmailLogin = (EditText) findViewById(R.id.editTextEmailLog);
        etPassLogin = (EditText) findViewById(R.id.editTextPasswordLog);
        btnIngresar = (Button) findViewById(R.id.cirLoginButtonLog);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){ //con esto se puede cerrar la app y aun asi la sesion sigue iniciada
            id_user = mAuth.getUid();
            mDataBase.child("Usuarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        //Log.d("id ", ds.child(""))
                        if (ds.getKey().toString().equals(id_user)){
                            String is = ds.child("paciente").getValue().toString();
                            if (is.equals("true")){
                                //Toast.makeText(ActivityLogin.this, "Has entrado como paciente", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ActivityLogin.this, MainActivity2.class));
                                finish(); //para prohibir que se pueda volver a esa vista
                            }
                            else{
                                //Toast.makeText(ActivityLogin.this, "Has entrado como cuidador", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ActivityLogin.this, MainActivityCuidadores .class));
                                finish(); //para prohibir que se pueda volver a esa vista
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}
