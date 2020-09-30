package com.example.vitalsignscheckup;

import android.app.Activity;
import android.content.Intent;
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

import org.w3c.dom.Text;

public class ActivityLogin extends AppCompatActivity {

    //layout_login
    private EditText etEmailLogin, etPassLogin; //datos que se pide

    private Button btnIngresar; //para logearse
    private TextView tvRegister; //para ir a pantalla registrarse

    //datos que se llenan
    private String email = "";
    private String pass = "";

    private FirebaseAuth mAuth;

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

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent mainActivityIntent = new Intent(v.getContext(), MainActivityCuidadores.class);

                //datos para confirmar en la BD
                String email = etEmailLogin.getText().toString();
                String pass = etPassLogin.getText().toString();

                startActivity(mainActivityIntent);
            }
        });

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

    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(ActivityLogin.this, MainActivityCuidadores.class));
                    finish(); //para prohibir que se pueda volver a esa vista
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
            startActivity(new Intent(ActivityLogin.this, MainActivityCuidadores.class));
            finish(); //para no volver a la pantalla anterior
        }
    }
}
