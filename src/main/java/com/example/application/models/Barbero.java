package com.example.application.models;

import java.util.List;

public class Barbero extends Persona {

    private List<String> especialidades;

    public Barbero(String nombre, String telefono, List<String> especialidades) {
        super(nombre, telefono);
        this.especialidades = especialidades;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public boolean puedeHacer(String tipo) {
        return especialidades.contains(tipo);
    }
}