package com.example.vitalsignscheckup.recyclerViewClasses;

public class PacienteCuidador {

    private String id;
    private String name;
    private String email;
    private int image;

    public PacienteCuidador(){

    }

    public PacienteCuidador(String name, String email, int image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public PacienteCuidador(String id, String name, String email, int image) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
