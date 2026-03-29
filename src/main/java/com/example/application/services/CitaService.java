package com.example.application.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.application.interfaces.Agendable;
import com.example.application.models.Barbero;
import com.example.application.models.Cita;
import com.example.application.models.Cliente;
import com.example.application.models.Factura;
import com.example.application.models.Servicio;

public class CitaService implements Agendable {

    private List<Cita> citas = new ArrayList<>();
    private List<Factura> facturas = new ArrayList<>();
    private List<Barbero> barberos = new ArrayList<>();

    public CitaService() {
        barberos.add(new Barbero("Carlos Freestyle", "111", List.of("freestyle")));
        barberos.add(new Barbero("Andrés Clásico", "222", List.of("corte")));
        barberos.add(new Barbero("Luis Colorista", "333", List.of("tinte")));
        barberos.add(new Barbero("Miguel Full", "444", List.of("corte","barba","cejas")));
    }

    public List<Cita> obtenerCitas() { return citas; }
    public List<Factura> obtenerFacturas() { return facturas; }
    public List<Barbero> obtenerBarberos() { return barberos; }

    public List<Barbero> obtenerBarberosPorServicio(String tipo) {
        List<Barbero> disponibles = new ArrayList<>();
        for (Barbero b : barberos) {
            if (b.puedeHacer(tipo)) disponibles.add(b);
        }
        return disponibles;
    }

   public Factura agendar(String nombre, String telefono, String sexo, String tipo, LocalDateTime fechaHora) {

    if (nombre == null || nombre.isEmpty())
        throw new RuntimeException("Nombre requerido");

    if (sexo == null || sexo.isEmpty())
        throw new RuntimeException("Sexo requerido");

    Servicio servicio = obtenerServicio(tipo);
    Barbero barbero = buscarBarberoDisponible(tipo, fechaHora);

    if (barbero == null)
        throw new RuntimeException("No hay barberos disponibles en esa fecha y hora");

    Cliente cliente = new Cliente(nombre, telefono, sexo);
    Cita cita = new Cita(cliente, barbero, servicio, fechaHora);
    citas.add(cita);

    Factura factura = new Factura(
            cliente.getNombre(),
            cliente.getSexo(),
            servicio.getNombre(),
            barbero.getNombre(),
            servicio.getPrecio(),
            fechaHora.toString()
    );

    facturas.add(factura);
    return factura;
}

    private Servicio obtenerServicio(String tipo) {
        switch (tipo) {
            case "corte": return new Servicio("Corte", 20, 10000, "corte");
            case "barba": return new Servicio("Barba", 15, 8000, "barba");
            case "cejas": return new Servicio("Cejas", 10, 5000, "cejas");
            case "tinte": return new Servicio("Tinte", 40, 30000, "tinte");
            case "freestyle": return new Servicio("Freestyle", 30, 20000, "freestyle");
            default: throw new RuntimeException("Servicio no válido");
        }
    }

    private Barbero buscarBarberoDisponible(String tipo, LocalDateTime fechaHora) {
        for (Barbero b : barberos) {
            boolean ocupado = citas.stream()
                    .anyMatch(c -> c.getBarbero().equals(b) &&
                                   c.getFechaHora().equals(fechaHora));
            if (b.puedeHacer(tipo) && !ocupado) return b;
        }
        return null;
    }

    @Override
    public void agendarCita(Cita cita) {
        citas.add(cita);
    }

    public List<Cita> obtenerCitasPorBarbero(Barbero barbero) {
        return citas.stream()
                .filter(c -> c.getBarbero().equals(barbero))
                .collect(Collectors.toList());
    }
}