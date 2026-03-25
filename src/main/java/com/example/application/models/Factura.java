package com.example.application.models;

public class Factura {

    private String cliente;
    private String servicio;
    private String barbero;
    private double precio;
    private String fecha;

    public Factura(String cliente, String servicio, String barbero, double precio, String fecha) {
        this.cliente = cliente;
        this.servicio = servicio;
        this.barbero = barbero;
        this.precio = precio;
        this.fecha = fecha;
    }

    public String generarHTML() {
        return "<div style='padding:25px;font-family:sans-serif;background:#111;color:white;border-radius:15px'>" +
               "<h2 style='text-align:center'>💈 BARBERÍA PREMIUM</h2>" +
               "<hr style='border:1px solid gray'>" +

               "<p><b>Cliente:</b> " + cliente + "</p>" +
               "<p><b>Barbero:</b> " + barbero + "</p>" +
               "<p><b>Servicio:</b> " + servicio + "</p>" +
               "<p><b>Fecha:</b> " + fecha + "</p>" +

               "<h3 style='color:#00ffcc'>Total: $" + precio + "</h3>" +

               "<hr style='border:1px solid gray'>" +
               "<p style='text-align:center'>Gracias por tu visita ✂️</p>" +
               "</div>";
    }

    public String getCliente() { return cliente; }
    public String getServicio() { return servicio; }
    public double getPrecio() { return precio; }
}