package com.example.application.models;

public class Servicio {

    private String nombre;
    private int duracion;
    private double precio;
    private String tipo;

    public Servicio(String nombre, int duracion, double precio, String tipo) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.precio = precio;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public int getDuracion() { return duracion; }
    public double getPrecio() { return precio; }
    public String getTipo() { return tipo; }
}