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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MisPacientesCuidadoresAdapter extends RecyclerView.Adapter<MisPacientesCuidadoresAdapter.MisPacientesCuidadoresViewHolder>{

    private ArrayList<PacienteCuidador> data;
    private int isPaciente;
    private String dialogMsg;
    private DatabaseReference mDataBase;
    FirebaseAuth mAuth;

    public MisPacientesCuidadoresAdapter(ArrayList<PacienteCuidador> data, int isPaciente) {
        this.data = data;
        this.isPaciente = isPaciente;
        if(isPaciente == 1){
            dialogMsg = "Eliminar de mis cuidadores";
        }else{
            dialogMsg = "Eliminar de mis pacientes";
        }
    }

    @Override
    public MisPacientesCuidadoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MisPacientesCuidadoresViewHolder misPacientesCuidadoresViewHolder = new MisPacientesCuidadoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_mis_cuidadores_pacientes, parent, false));
        return misPacientesCuidadoresViewHolder;
    }

    @Override
    public void onBindViewHolder(MisPacientesCuidadoresViewHolder holder, int position) {
        PacienteCuidador miPacienteCuidador = data.get(position);
        //holder.ivProfile.setImageResource(miPacienteCuidador.getImage());
        holder.tvName.setText(miPacienteCuidador.getName());
        holder.tvEmail.setText(miPacienteCuidador.getEmail());

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        holder.ivMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ivMoreOptions.setSelected(true);
                new AlertDialog.Builder(view.getRootView().getContext())
                        .setTitle(dialogMsg)
                        .setMessage("¿Seguro quieres eliminar a " + miPacienteCuidador.getName() + "?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Result","Success");

                                //Eliminar de la BD

                                Log.i("Result","Success");
                                String name = miPacienteCuidador.getName();
                                String email = miPacienteCuidador.getEmail();
                                Log.d("name ", name);
                                Log.d("email ", email);
                                String id = mAuth.getCurrentUser().getUid(); //obtener id del usuario actual
                                mDataBase.child("Pacientes").child(id).child("cuidadores").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d("algo ", name);
                                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                                            Log.d("nada", name);
                                            //Iterable<DataSnapshot> list_ids = ds.child("cuidadores").child("correo").getChildren();

                                            //data.add(new PacienteCuidador(ds.child("Correo").getValue().toString(),
                                            //        ds.child("Nombre").getValue().toString(),
                                            //        R.drawable.ic_awesome_user_circle));
                                            Log.d("valor: ", ds.child("Nombre").getValue().toString());
                                            if (ds.child("Nombre").getValue().toString().equals(name)){
                                                Log.d("igual1", name);
                                                if(ds.child("Correo").getValue().toString().equals(email)){
                                                    Log.d("igual2", name);
                                                    ds.getRef().removeValue();
                                                    data.remove(miPacienteCuidador);
                                                    notifyDataSetChanged();
                                                    //notifyItemRemoved(position);
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
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
