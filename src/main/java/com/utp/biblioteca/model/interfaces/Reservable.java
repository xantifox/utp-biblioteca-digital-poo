package com.utp.biblioteca.model.interfaces;

import java.time.LocalDateTime;

/**
 * Interface Reservable
 * Define el contrato para recursos que pueden ser reservados.
 * Implementado por: LibroFisico (NO por eBooks ni AudioLibros - recursos digitales ilimitados)
 */
public interface Reservable {
    
    /**
     * Procesa una nueva reserva del recurso
     * @param usuarioId ID del usuario que realiza la reserva
     * @param prioridad prioridad basada en el tipo de usuario (Profesor > Estudiante)
     * @return ID de la reserva creada, null si no se pudo crear
     */
    String reservar(String usuarioId, int prioridad);
    
    /**
     * Cancela una reserva existente
     * @param reservaId ID de la reserva a cancelar
     * @return true si la cancelación fue exitosa
     */
    boolean cancelarReserva(String reservaId);
    
    /**
     * Verifica si el recurso tiene reservas pendientes
     * @return true si existen reservas en cola
     */
    boolean tieneReservasPendientes();
    
    /**
     * Obtiene la posición en la cola de reservas para un usuario
     * @param usuarioId ID del usuario
     * @return posición en la cola (1 = primero), 0 si no tiene reserva
     */
    int getPosicionEnCola(String usuarioId);
    
    /**
     * Notifica al siguiente usuario en la cola cuando el recurso esté disponible
     * @return ID del usuario notificado, null si no hay cola
     */
    String notificarSiguienteEnCola();
    
    /**
     * Procesa automáticamente las reservas expiradas
     * @param horasExpiracion horas para considerar una reserva como expirada
     * @return número de reservas expiradas procesadas
     */
    int procesarReservasExpiradas(int horasExpiracion);
}