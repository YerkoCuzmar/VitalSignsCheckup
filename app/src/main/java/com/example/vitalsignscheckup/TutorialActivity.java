package com.example.vitalsignscheckup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TutorialActivity extends AppCompatActivity {

    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        type = getIntent().getIntExtra("type", 0);
        setToolbar(type);

        SharedPreferences preferences;
        TextView tutorialText = findViewById(R.id.textTutorial);
        ImageView tutorialImg = findViewById(R.id.imageTutorial);
        ImageView tutorialImg2 = findViewById(R.id.image2Tutorial);
        Button tutorialBtn = findViewById(R.id.buttonTutorial);
        final CheckBox tutorialChk = findViewById(R.id.checkTutorial);
        String text;
        String port;
        switch (type){
            case 1:
                preferences = this.getSharedPreferences("TempConfig", Context.MODE_PRIVATE);
                port = preferences.getString("port", null);
                text = "El sensor de Temperatura está conectado en el puerto " + port + ".\n" +
                        "Este sensor se posiciona preferentemente en la zona de la axila.";
                tutorialImg.setImageResource(R.drawable.cuerpo_temp);
                tutorialImg2.setVisibility(View.GONE);
                break;
            case 2:
                preferences = this.getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
                port = preferences.getString("port", null);
                text = "El sensor de Ritmo Cardíaco está conectado en el puerto " + port + ".\n" +
                        "Este sensor se posiciona en el dedo indice de la mano.";
                tutorialImg.setImageResource(R.drawable.cuerpo_bvp);
                tutorialImg2.setVisibility(View.GONE);
                break;
            case 3:
                preferences = this.getSharedPreferences("EDAConfig", Context.MODE_PRIVATE);
                port = preferences.getString("port", null);
                text = "El sensor de niveles de estrés está conectado en el puerto " + port + ".\n" +
                        "El sensor con la etiqueta roja se posiciona en el dedo indice de la mano.\n" +
                        "El sensor con la etiqueta negra se posiciona en el dedo medio de la mano.";
                tutorialImg.setImageResource(R.drawable.cuerpo_eda);
                tutorialImg2.setVisibility(View.GONE);
                break;
            case 4:
                preferences = this.getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
                String portEcg = preferences.getString("port", null);
                preferences = this.getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
                String portBvp = preferences.getString("port", null);
                text = "Los sensores para medir presión están conectados en los puertos " + portBvp + " y " + portEcg + ".\n" +
                        "El sensor del puerto " + portBvp + "se posiciona en el dedo indice de la mano." +
                        "El sensor del puerto " + portEcg + "se posiciona en la zona de las costillas como se muestra en la imagen.";
                tutorialImg.setImageResource(R.drawable.cuerpo_bvp);
                tutorialImg2.setImageResource(R.drawable.cuerpo_ecg);
                break;
            default:
                text = "";
                break;
        }
        tutorialText.setText(text);
        tutorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences;
                SharedPreferences.Editor spEditor;
                Intent monitorIntent;
                preferences = getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
                spEditor = preferences.edit();
                switch (type){
                    case 1:
                        spEditor.putInt("temperature", tutorialChk.isChecked() ? 0 : 1);
                        spEditor.apply();
                        monitorIntent = new Intent(v.getContext(), MonitorTemperature.class);
                        startActivity(monitorIntent);
                        break;
                    case 2:
                        spEditor.putInt("heartRate", tutorialChk.isChecked() ? 0 : 1);
                        spEditor.apply();
                        monitorIntent = new Intent(v.getContext(), MonitorHeartRate.class);
                        startActivity(monitorIntent);
                        break;
                    case 3:
                        spEditor.putInt("stress", tutorialChk.isChecked() ? 0 : 1);
                        spEditor.apply();
                        monitorIntent = new Intent(v.getContext(), MonitorStressLevel.class);
                        startActivity(monitorIntent);
                        break;
                    case 4:
                        spEditor.putInt("bloodPressure", tutorialChk.isChecked() ? 0 : 1);
                        spEditor.apply();
                        monitorIntent = new Intent(v.getContext(), MonitorBloodPressure.class);
                        startActivity(monitorIntent);
                        break;
                }
                finish();
            }
        });
    }

    private void setToolbar(int type) {
        String title;
        Toolbar toolbar = (Toolbar) findViewById(R.id.tutorialToolbar);
        setSupportActionBar(toolbar);
        switch (type) {
            case 1:
                title = "Temperatura";
                break;
            case 2:
                title = "Ritmo Cardíaco";
                break;
            case 3:
                title = "Niveles de estrés";
                break;
            case 4:
                title = "Presión Sanguinea";
                break;
            default:
                title = "";
                break;
        }
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}