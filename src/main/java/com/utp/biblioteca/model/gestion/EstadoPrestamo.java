package com.utp.biblioteca.model.gestion;

/**
 * Enumeración EstadoPrestamo
 * Define los estados válidos del ciclo de vida de un préstamo
 */
public enum EstadoPrestamo {
    ACTIVO("Préstamo activo"),
    VENCIDO("Préstamo vencido"),
    DEVUELTO("Préstamo devuelto"),
    RENOVADO("Préstamo renovado"),
    CANCELADO("Préstamo cancelado");
    
    private final String descripcion;
    
    EstadoPrestamo(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}