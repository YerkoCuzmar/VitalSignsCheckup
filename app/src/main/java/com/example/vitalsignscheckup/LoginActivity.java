package com.example.vitalsignscheckup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 123;
    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor desconocido";

    TextView tvname;
    TextView tvemail;

    TextInputLayout tiName, tiMobile, tiEmail, tiPass;
    EditText etName, etMobile, etEmail, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //se ejecuta cada vez que se inicia o cierra sesion
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    // hay inicio de sesion exitoso
                    onSetDataUser(user.getDisplayName(), user.getEmail());
                }
                else{
                    //no hay sesion activa o se cerro la sesion
                    startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().
                            setIsSmartLockEnabled(false)   //false: recuerda contraseñas y cuentas logeadas
                            /*.setTosAndPrivacyPolicyUrls()*/
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(), RC_SIGN_IN );
                }

            }
        };
    }

    private void onSetDataUser(String userDisplayName, String email){
        tvname = (TextView)findViewById(R.id.tvUserView);
        tvname.setText(userDisplayName);
        tvemail = (TextView)findViewById(R.id.tvEmail);
        tvemail.setText(email);

        tiName = (TextInputLayout) findViewById(R.id.textInputName);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){ //aqui ya esta iniciada la sesion y se cambia de actividad o se manda msje de bienvenida
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Error, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
