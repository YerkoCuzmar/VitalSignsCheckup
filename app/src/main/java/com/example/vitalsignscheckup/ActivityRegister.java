package com.example.vitalsignscheckup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ActivityRegister extends AppCompatActivity {

    // layout_register
    EditText etName, etMobile, etEmail, etPass;
    Button btnRegister;
    TextView tvIngresar;
    CheckBox cbPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        instanceRegisterElements();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent mainActivityIntent = new Intent(v.getContext(), MainActivity2.class);

                //datos para ingresar a la BD
                String name = etName.getText().toString();
                String mobile = etMobile.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                boolean isPaciente = cbPaciente.isChecked(); //true si es paciente

                startActivity(mainActivityIntent);
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

    private void instanceRegisterElements () {

        etName = (EditText) findViewById(R.id.editTextName);
        etMobile = (EditText) findViewById(R.id.editTextMobile);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPass = (EditText) findViewById(R.id.editTextPassword);
        btnRegister = (Button) findViewById(R.id.cirLoginButton);
        tvIngresar = (TextView) findViewById(R.id.tvIngresar);
        cbPaciente = (CheckBox) findViewById(R.id.checkBox);
    }

}
