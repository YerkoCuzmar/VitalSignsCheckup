package com.example.vitalsignscheckup.recyclerViewClasses;

public class Pacientes {

    private String name, lastTemp, lastHeartRate, lastPressure, lastStress;
    private int image;

    public Pacientes(){

    }

    public Pacientes(String name, String lastTemp, String lastHeartRate, String lastPressure, String lastStress, int image){
        this.name = name;
        this.lastTemp = lastTemp;
        this.lastHeartRate = lastHeartRate;
        this.lastPressure = lastPressure;
        this.lastStress = lastStress;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastTemp() {
        return lastTemp;
    }

    public void setLastTemp(String lastTemp) {
        this.lastTemp = lastTemp;
    }

    public String getLastHeartRate() {
        return lastHeartRate;
    }

    public void setLastHeartRate(String lastHeartRate) {
        this.lastHeartRate = lastHeartRate;
    }

    public String getLastPressure() {
        return lastPressure;
    }

    public void setLastPressure(String lastPressure) {
        this.lastPressure = lastPressure;
    }

    public String getLastStress() {
        return lastStress;
    }

    public void setLastStress(String lastStress) {
        this.lastStress = lastStress;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
