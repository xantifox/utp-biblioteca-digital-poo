package com.utp.biblioteca.model.interfaces;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface Prestable
 * Define el contrato para recursos que pueden ser prestados.
 * Implementado por: LibroFisico, EBook, AudioLibro
 */
public interface Prestable {
    
    /**
     * Procesa el préstamo del recurso
     * @return true si el préstamo fue exitoso, false si no está disponible
     */
    boolean prestar();
    
    /**
     * Procesa la devolución del recurso
     * @return true si la devolución fue exitosa
     */
    boolean devolver();
    
    /**
     * Verifica si el recurso puede ser renovado
     * @return true si no hay reservas pendientes y cumple condiciones
     */
    boolean puedeRenovar();
    
    /**
     * Calcula los días estándar de préstamo según el tipo de recurso
     * @return número de días de préstamo permitidos
     */
    int calcularDiasPrestamo();
    
    /**
     * Calcula la multa por retraso en la devolución
     * @param diasRetraso días transcurridos después de la fecha de vencimiento
     * @return monto de la multa calculada
     */
    double calcularMulta(int diasRetraso);
}