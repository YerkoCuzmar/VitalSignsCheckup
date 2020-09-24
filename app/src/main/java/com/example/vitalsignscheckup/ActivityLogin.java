package com.example.vitalsignscheckup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ActivityLogin extends AppCompatActivity {

    //layout_login
    EditText etEmailLogin, etPassLogin;
    Button btnIngresar;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instanceLoginElements();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent mainActivityIntent = new Intent(v.getContext(), MainActivity2.class);

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

    }

    private void instanceLoginElements () {

        etEmailLogin = (EditText) findViewById(R.id.editTextEmailLog);
        etPassLogin = (EditText) findViewById(R.id.editTextPasswordLog);
        btnIngresar = (Button) findViewById(R.id.cirLoginButtonLog);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
    }

}
