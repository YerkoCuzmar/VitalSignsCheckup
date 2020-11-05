package com.example.vitalsignscheckup.recyclerViewClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.R;
import com.example.vitalsignscheckup.models.Notificaciones;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>{

    private ArrayList<Notificaciones> data;
    private OnNotificacionListener mOnNotifcacionListener;


    public NotificacionAdapter() {
        this.data = new ArrayList<>();
    }

    public NotificacionAdapter(OnNotificacionListener onNotificacionListener) {
        this.data = new ArrayList<>();
        this.mOnNotifcacionListener = onNotificacionListener;
    }

    public NotificacionAdapter(ArrayList<Notificaciones> data, OnNotificacionListener onNotificacionListener) {
        this.data = data;
        this.mOnNotifcacionListener = onNotificacionListener;
    }

    public NotificacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotificacionViewHolder mainCuidadoresViewHolder = new NotificacionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_notificaciones_list, parent, false), mOnNotifcacionListener);
        return mainCuidadoresViewHolder;
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder holder, int position) {
        Notificaciones notificacion = data.get(position);
        holder.tvType.setText(notificacion.getType());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        try {
            holder.tvDateTime.setText(format.format(notificacion.getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addNotificacion(Notificaciones notificacion){
        if(!data.contains(notificacion)){
            data.add(notificacion);
        }
    }

    public Notificaciones getPaciente(int position){
        return data.get(position);
    }

    class NotificacionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnNotificacionListener onNotificacionListener;

        TextView tvType, tvDateTime;
        LinearLayout llNotificacion;

        public NotificacionViewHolder(View itemView, OnNotificacionListener onNotificacionListener) {
            super(itemView);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            llNotificacion = (LinearLayout) itemView.findViewById(R.id.llNotificacion);
            this.onNotificacionListener = onNotificacionListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                onNotificacionListener.onNotificationClick(getAdapterPosition());
            } catch (ParseException e) {
                {e.printStackTrace();}
            }
        }
    }

    public interface OnNotificacionListener{
        void onNotificationClick(int position) throws ParseException;
    }
}