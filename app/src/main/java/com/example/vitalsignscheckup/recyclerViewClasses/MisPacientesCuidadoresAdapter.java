package com.example.vitalsignscheckup.recyclerViewClasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.R;

import java.util.ArrayList;

public class MisPacientesCuidadoresAdapter extends RecyclerView.Adapter<MisPacientesCuidadoresAdapter.MisPacientesCuidadoresViewHolder>{

    private ArrayList<PacienteCuidador> data;
    private int isPaciente;

    public MisPacientesCuidadoresAdapter(ArrayList<PacienteCuidador> data, int isPaciente) {
        this.data = data;
        this.isPaciente = isPaciente;
    }

    @Override
    public MisPacientesCuidadoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MisPacientesCuidadoresViewHolder misPacientesCuidadoresViewHolder = new MisPacientesCuidadoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_mis_cuidadores_pacientes, parent, false));
        return misPacientesCuidadoresViewHolder;
    }

    @Override
    public void onBindViewHolder(MisPacientesCuidadoresViewHolder holder, int position) {
        PacienteCuidador miPacienteCuidador = data.get(position);
        holder.ivProfile.setImageResource(miPacienteCuidador.getImage());
        holder.tvName.setText(miPacienteCuidador.getName());
        holder.tvEmail.setText(miPacienteCuidador.getEmail());

        holder.ivMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ivMoreOptions.setSelected(true);
                new AlertDialog.Builder(view.getRootView().getContext())
                        .setTitle("Eliminar de mis cuidadores")
                        .setMessage("¿Seguro quieres eliminar a " + miPacienteCuidador.getName() + "?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Result","Success");

                                //Eliminar de la BD

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Result","Success");
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MisPacientesCuidadoresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfile, ivMoreOptions;
        TextView tvName, tvEmail;

        public MisPacientesCuidadoresViewHolder(View itemView) {
            super(itemView);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            ivMoreOptions = (ImageView) itemView.findViewById(R.id.ivMoreOptions);

            //ivMoreOptions.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == ivMoreOptions.getId()){
                Log.d("Mas opciones", tvName.getText().toString());
                Log.d("Mas opciones", tvEmail.getText().toString());
            }
        }
    }
}
