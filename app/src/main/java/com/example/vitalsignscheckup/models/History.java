package com.example.vitalsignscheckup.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class History {
    private String date;
    private String time;
    private int read;

    public History(){}

    public History(int read){
        this.read = read;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = new Date();
        this.date = dateFormat.format(datetime);
        this.time = timeFormat.format(datetime);
    }

    public String getDate(){ return this.date; }

    public String getTime(){ return this.time; }

    public int getRead(){ return this.read; }

}
