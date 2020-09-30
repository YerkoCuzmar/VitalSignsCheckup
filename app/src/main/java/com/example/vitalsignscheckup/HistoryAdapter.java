package com.example.vitalsignscheckup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitalsignscheckup.models.Mediciones;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<Mediciones> dataset;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView time;
        public TextView read;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.history_date);
            time = itemView.findViewById(R.id.history_time);
            read = itemView.findViewById(R.id.history_read);
        }
    }

    public HistoryAdapter() { dataset = new ArrayList<>(); }
    public HistoryAdapter(ArrayList<Mediciones> myDataSet) { dataset = myDataSet; }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.date.setText(dataset.get(position).getDate());
        holder.time.setText(dataset.get(position).getTime());
        holder.read.setText(String.valueOf(dataset.get(position).getMedicion()));
    }

    @Override
    public int getItemCount() { return dataset.size(); }

    public void addNewHistory(Mediciones medicion){
        dataset.add(0, medicion);
        notifyDataSetChanged();
    }

}
