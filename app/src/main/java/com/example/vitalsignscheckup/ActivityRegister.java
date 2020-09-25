package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class ActivityRegister extends AppCompatActivity {

    //private EditText mEditTextName;
    //private EditText mEditTextEmail;
    //private EditText mEditTextPassword;
    //private Button mButtonRegister;

    private EditText etName, etMobile, etEmail, etPass;
    private Button btnRegister;
    private TextView tvIngresar;
    private CheckBox cbPaciente;

    //variables datos a registrar

    //private String name = "";
    //private String email = "";
    //private String password = "";

    private String name = "";
    private String mobile = "";
    private String email = "";
    private String pass = "";
    private boolean isPaciente = false;


    //firebase

    FirebaseAuth mAuth;

    //BD

    DatabaseReference mDataBase;

    //aux
    //FirebaseDatabase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();  //nodo principal de la base de datos
        //fb = FirebaseDatabase.getInstance();
        //mDataBase = fb.getReference();
        //vitalsignscheckup-c8844 se llama

        //mEditTextName = (EditText) findViewById(R.id.editTextName);
        //mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        //mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //mButtonRegister = (Button) findViewById(R.id.btnRegister);

        etName = (EditText) findViewById(R.id.editTextName);
        etMobile = (EditText) findViewById(R.id.editTextMobile);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPass = (EditText) findViewById(R.id.editTextPassword);
        btnRegister = (Button) findViewById(R.id.cirLoginButton);
        tvIngresar = (TextView) findViewById(R.id.tvIngresar);
        cbPaciente = (CheckBox) findViewById(R.id.checkBox);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //name = mEditTextName.getText().toString();
                //email = mEditTextEmail.getText().toString();
                //password = mEditTextPassword.getText().toString();

                name = etName.getText().toString();
                mobile = etMobile.getText().toString();
                email = etEmail.getText().toString();
                pass = etPass.getText().toString();
                boolean isPaciente = cbPaciente.isChecked(); //true si es paciente

                if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !mobile.isEmpty()){

                    if(pass.length() >= 6) {

                        registerUser();
                    }
                    else{
                        Toast.makeText(ActivityRegister.this, "El password debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ActivityRegister.this, "Debes completar todos los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginActivityIntent = new Intent(v.getContext(), ActivityLogin.class);
                startActivity(loginActivityIntent);
            }
        });

    }


    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { //tarea es crear usuario
                if (task.isSuccessful()){ //almacenar datos en la base de datos

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", pass);
                    map.put("mobile", mobile);
                    map.put("paciente", isPaciente);

                    String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario nuevo

                    mDataBase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){ //tarea ahora es crear datos en la bd

                                startActivity(new Intent(ActivityRegister.this, MainActivity2.class));
                                finish();
                            }
                            else{
                                System.out.println("salleee");
                                Toast.makeText(ActivityRegister.this, "No se pudieron crear los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else{
                    Toast.makeText(ActivityRegister.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }/

    /*@Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }*/


}