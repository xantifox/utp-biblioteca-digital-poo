package com.utp.biblioteca.model.gestion;

/**
 * Enumeración EstadoReserva
 * Define los estados válidos del ciclo de vida de una reserva
 */
public enum EstadoReserva {
    PENDIENTE("Reserva pendiente"),
    CONFIRMADA("Reserva confirmada - recurso disponible"),
    EXPIRADA("Reserva expirada por tiempo"),
    CANCELADA("Reserva cancelada por usuario"),
    COMPLETADA("Reserva completada - préstamo realizado");
    
    private final String descripcion;
    
    EstadoReserva(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}