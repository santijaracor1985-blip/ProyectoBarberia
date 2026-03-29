package com.example.application.models;

public class Cliente {
    protected String nombre;
    protected String telefono;
    protected String sexo; // NUEVO

    public Cliente(String nombre, String telefono, String sexo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.sexo = sexo;
    }

    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getSexo() { return sexo; } // NUEVO
}