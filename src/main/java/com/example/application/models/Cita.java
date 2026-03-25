package com.example.application.models;

import java.time.LocalDateTime;

public class Cita {

    private Cliente cliente;
    private Barbero barbero;
    private Servicio servicio;
    private LocalDateTime fechaHora;

    public Cita(Cliente cliente2, Barbero barbero, Servicio servicio, LocalDateTime fechaHora) {
        this.cliente = cliente2;
        this.barbero = barbero;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
    }

    public Cliente getCliente() { return cliente; }
    public Barbero getBarbero() { return barbero; }
    public Servicio getServicio() { return servicio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}