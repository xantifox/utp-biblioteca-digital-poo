package com.utp.biblioteca.model.gestion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clase utilitaria para generar estadísticas del sistema
 * Demuestra uso de STREAM API y LAMBDA EXPRESSIONS
 */
public class EstadisticasGestion {
    
    /**
     * Genera estadísticas de préstamos usando STREAM API
     */
    public static Map<String, Object> generarEstadisticasPrestamos(List<Prestamo> prestamos) {
        return prestamos.stream()
            .collect(Collectors.groupingBy(
                p -> p.getEstado().name(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> (Object) entry.getValue()
            ));
    }
    
    /**
     * Encuentra préstamos vencidos usando STREAM API y LAMBDA
     */
    public static List<Prestamo> encontrarPrestamosVencidos(List<Prestamo> prestamos) {
        return prestamos.stream()
            .filter(p -> p.getEstado() == EstadoPrestamo.ACTIVO)
            .filter(Prestamo::estaVencido)
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula multas totales pendientes usando STREAM API
     */
    public static double calcularMultasTotales(List<Multa> multas) {
        return multas.stream()
            .filter(m -> !m.isPagada())
            .mapToDouble(Multa::getMonto)
            .sum();
    }
    
    /**
     * Encuentra reservas por expirar usando STREAM API
     */
    public static List<Reserva> encontrarReservasPorExpirar(List<Reserva> reservas, int horasAnticipacion) {
        LocalDateTime limite = LocalDateTime.now().plusHours(horasAnticipacion);
        
        return reservas.stream()
            .filter(r -> r.getEstado() == EstadoReserva.PENDIENTE || r.getEstado() == EstadoReserva.CONFIRMADA)
            .filter(r -> r.getFechaExpiracion().isBefore(limite))
            .collect(Collectors.toList());
    }
}