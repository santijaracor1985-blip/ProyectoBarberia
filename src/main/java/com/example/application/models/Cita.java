package com.example.application.models;
import java.util.List;

import java.time.LocalDateTime;

public class Cita {

    private Cliente cliente;
    private Barbero barbero;
    private List<Servicio> servicios;
    private LocalDateTime fechaHora;

    public Cita(Cliente cliente2, Barbero barbero, List<Servicio> servicios, LocalDateTime fechaHora) {
        this.cliente = cliente2;
        this.barbero = barbero;
        this.servicios = servicios;
        this.fechaHora = fechaHora;
    }

    public Cliente getCliente() { return cliente; }
    public Barbero getBarbero() { return barbero; }
    public List<Servicio> getServicios() { return servicios; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}