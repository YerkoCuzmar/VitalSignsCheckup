package com.example.vitalsignscheckup.recyclerViewClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.R;

import java.util.ArrayList;

public class MainCuidadoresAdapter extends RecyclerView.Adapter<MainCuidadoresAdapter.MainCuidadoresViewHolder>{

    private ArrayList<Pacientes> data;

    public MainCuidadoresAdapter() {
        this.data = new ArrayList<>();
    }

    public MainCuidadoresAdapter(ArrayList<Pacientes> data) {
        this.data = data;
    }

    @Override
    public MainCuidadoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainCuidadoresViewHolder mainCuidadoresViewHolder = new MainCuidadoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main_cuidadores_fragment, parent, false));
        return mainCuidadoresViewHolder;
    }

    @Override
    public void onBindViewHolder(MainCuidadoresViewHolder holder, int position) {
        Pacientes paciente = data.get(position);
        holder.ivProfile.setImageResource(paciente.getImage());
        holder.tvName.setText(paciente.getName());
        holder.tvTemperature.setText(paciente.getLastTemp());
        holder.tvHeartRate.setText(paciente.getLastHeartRate());
        holder.tvPressure.setText(paciente.getLastPressure());
        holder.tvStress.setText(paciente.getLastStress());
        holder.llPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Ver parámetros en tiempo real de " + paciente.getName(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addPaciente(Pacientes paciente){
        if(!data.contains(paciente)){
            data.add(paciente);
        }
    }

    class MainCuidadoresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfile;
        TextView tvName, tvTemperature, tvHeartRate, tvPressure, tvStress;
        LinearLayout llPaciente;

        public MainCuidadoresViewHolder(View itemView) {
            super(itemView);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTemperature = (TextView) itemView.findViewById(R.id.tvTemperature);
            tvHeartRate = (TextView) itemView.findViewById(R.id.tvHeartRate);
            tvPressure = (TextView) itemView.findViewById(R.id.tvBloodPressure);
            tvStress = (TextView) itemView.findViewById(R.id.tvStress);
            llPaciente = (LinearLayout) itemView.findViewById(R.id.llPacientes);
        }

        @Override
        public void onClick(View view) {

        }

    }
}
