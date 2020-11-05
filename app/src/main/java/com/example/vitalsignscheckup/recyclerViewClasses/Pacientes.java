package com.example.vitalsignscheckup.recyclerViewClasses;

public class Pacientes {

    private String id, name, place;
    private int notifications;
    private int image;

    public Pacientes(){
        this.id = "";
        this.name = "";
        this.place = "";
        this.notifications = 0;
        this.image = 0;
    }

    public Pacientes(String name, String place, int notifications, int image){
        this.name = name;
        this.place = place;
        this.notifications = notifications;
        this.image = image;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public void addNotification(){
        this.notifications += 1;
    }

    public void subNotification(){
        this.notifications -= 1;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
