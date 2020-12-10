package com.example.vitalsignscheckup.recyclerViewClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.models.Notificaciones;

import java.util.ArrayList;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.ViewHolderNotificaciones> {

    private ArrayList<Notificaciones> listNotificaciones;
    Notificaciones notificacion;

    public NotificacionesAdapter(){

        this.listNotificaciones = new ArrayList<>();
        //this.mOnPacienteListener = onPacienteListener();
    }

    @NonNull
    @Override
    public ViewHolderNotificaciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
        return new ViewHolderNotificaciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotificaciones holder, int position) {
        notificacion = listNotificaciones.get(position);
        switch (notificacion.getType()){
            case 1:
                holder.textMsg.setText("Temperatura");
                holder.notificationIcon.setImageResource(R.drawable.ic_thermometer);
                break;
            case 2:
                holder.textMsg.setText("Ritmo Cardiaco");
                holder.notificationIcon.setImageResource(R.drawable.ic_heart);
                break;
            case 3:
                holder.textMsg.setText("Estres");
                holder.notificationIcon.setImageResource(R.drawable.ic_stress);
                break;
            case 4:
                holder.textMsg.setText("Presion");
                holder.notificationIcon.setImageResource(R.drawable.ic_blood_pressure);
                break;
            case 5:
                holder.textMsg.setText("SOS");
                holder.notificationIcon.setImageResource(R.drawable.sos_icon);
                break;
        }
        holder.textDate.setText(notificacion.getDate());
        holder.textTime.setText(notificacion.getTime());
    }

    @Override
    public int getItemCount() {
        return listNotificaciones.size();
    }

    public void addNotificacion(Notificaciones notificacion){
        if(!listNotificaciones.contains(notificacion)){
            listNotificaciones.add(notificacion);
            notifyDataSetChanged();
        }
    }

    public Notificaciones getNotificacion(int position){

        return listNotificaciones.get(position);
    }

    public class ViewHolderNotificaciones extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPacienteListener onPacienteListener;

        ImageView notificationIcon;
        TextView textMsg;
        TextView textDate;
        TextView textTime;

        public ViewHolderNotificaciones(@NonNull View itemView) {
            super(itemView);

            notificationIcon = itemView.findViewById(R.id.notification_icon);
            textMsg = itemView.findViewById(R.id.notification_message);
            textDate = itemView.findViewById(R.id.notification_date);
            textTime = itemView.findViewById(R.id.notification_time);

        }

        @Override
        public void onClick(View view) {
            onPacienteListener.onPacienteClick(getAdapterPosition());
        }

    }

    public interface OnPacienteListener{
        void onPacienteClick(int position);
    }
}
