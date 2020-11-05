package com.example.vitalsignscheckup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
    private RadioButton rbPaciente;
    private RadioButton rbCuidador;
    private RadioButton rbSalud;
    private RadioButton rbParticular;
    private RadioButton rbReposo;

    //variables datos a registrar

    //private String name = "";
    //private String email = "";
    //private String password = "";

    private String name = "";
    private String mobile = "";
    private String email = "";
    private String pass = "";
    private boolean isPaciente = false;
    private String ubicacionPaciente = "";

    SharedPreferences preferences;
    SharedPreferences.Editor spEditor;

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
        rbPaciente = (RadioButton) findViewById(R.id.radiobuttonPaciente);
        rbCuidador = (RadioButton) findViewById(R.id.radiobuttonCuidador);
        rbSalud = (RadioButton) findViewById(R.id.radiobuttonSalud);
        rbParticular = (RadioButton) findViewById(R.id.radiobuttonParticular);
        rbReposo = (RadioButton) findViewById(R.id.radiobuttonReposo);

        btnRegister = (Button) findViewById(R.id.cirLoginButton);
        tvIngresar = (TextView) findViewById(R.id.tvIngresar);

        rbCuidador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbSalud.setChecked(false);
                    rbParticular.setChecked(false);
                    rbReposo.setChecked(false);

                    rbSalud.setEnabled(false);
                    rbParticular.setEnabled(false);
                    rbReposo.setEnabled(false);
                }
                else {
                    rbSalud.setEnabled(true);
                    rbParticular.setEnabled(true);
                    rbReposo.setEnabled(true);
                }
            }
        });

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
                isPaciente = rbPaciente.isChecked();
                if(rbSalud.isChecked()){
                    ubicacionPaciente = "Centro de salud";
                }
                else if (rbParticular.isChecked()){
                    ubicacionPaciente = "Casa particular";
                }
                else if (rbReposo.isChecked()){
                    ubicacionPaciente = "Casa de reposo";
                }

                Boolean tipoPacienteSeleccionado = rbPaciente.isChecked() || rbCuidador.isChecked();
                Boolean ubicacionPacienteSeleccionado = rbPaciente.isChecked() && (rbSalud.isChecked() || rbParticular.isChecked() || rbReposo.isChecked());

                if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !mobile.isEmpty() && tipoPacienteSeleccionado && ubicacionPacienteSeleccionado){

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
                    //map.put("password", pass);
                    map.put("mobile", mobile);
                    map.put("paciente", isPaciente);
                    map.put("ubicacion", ubicacionPaciente);
                    Log.d("Registro", "onComplete: " + map);

                    preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    spEditor = preferences.edit();
                    spEditor.putString("email", email);
                    spEditor.putString("name", name);
                    spEditor.apply();

                    String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario nuevo

                    if (isPaciente){
                        mDataBase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if(task2.isSuccessful()){ //tarea ahora es crear datos en la bd
                                    startActivity(new Intent(ActivityRegister.this, MainActivity2.class));
                                    //Toast.makeText(ActivityRegister.this, "Has entrado como paciente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    //System.out.println("salleee");
                                    Toast.makeText(ActivityRegister.this, "No se pudieron crear los datos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        mDataBase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if(task2.isSuccessful()){ //tarea ahora es crear datos en la bd
                                    startActivity(new Intent(ActivityRegister.this, MainActivityCuidadores.class));
                                    //Toast.makeText(ActivityRegister.this, "Has entrado como cuidador", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    //System.out.println("salleee");
                                    Toast.makeText(ActivityRegister.this, "No se pudieron crear los datos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
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