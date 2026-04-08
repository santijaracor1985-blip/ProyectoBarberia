package com.example.application.models;

public class Cliente {


    private String nombre;
    private String telefono;
    private String sexo;

   
    public Cliente(String nombre, String telefono, String sexo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.sexo = sexo;
    }

   
    public Cliente(String nombre) {
        this.nombre = nombre;
        this.telefono = "No registrado";
        this.sexo = "No definido";
    }

   
    public Cliente(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.sexo = "No definido";
    }

   
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getSexo() { return sexo; }

    
}