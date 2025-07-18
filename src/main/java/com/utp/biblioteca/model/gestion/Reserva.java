package com.utp.biblioteca.model.gestion;

import com.utp.biblioteca.model.usuario.Usuario;
import com.utp.biblioteca.model.recurso.Recurso;
import com.utp.biblioteca.model.interfaces.Reservable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Clase Reserva - ASOCIACIÓN con control temporal
 * Gestiona las reservas de recursos no disponibles
 * Aplica POLIMORFISMO para prioridades según tipo de usuario
 */
public class Reserva {
    
    // ASOCIACIÓN: Reserva conecta Usuario con Recurso
    private String id;
    private String usuarioId;
    private String recursoId;
    
    // Control temporal
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaExpiracion;
    private EstadoReserva estado;
    
    // Control de prioridad y posición
    private int prioridad;
    private int posicionCola;
    
    // Referencias para polimorfismo
    private Usuario usuario;
    private Recurso recurso;
    
    // Configuración del sistema
    private static final int HORAS_EXPIRACION_DEFAULT = 48;
    private static final int HORAS_CONFIRMACION = 24;
    
    /**
     * Constructor para crear una nueva reserva
     * Aplica POLIMORFISMO para asignar prioridad según tipo de usuario
     */
    public Reserva(Usuario usuario, Recurso recurso) {
        this.id = UUID.randomUUID().toString();
        this.usuarioId = usuario.getId();
        this.recursoId = recurso.getId();
        this.usuario = usuario;
        this.recurso = recurso;
        
        this.fechaReserva = LocalDateTime.now();
        this.fechaExpiracion = fechaReserva.plusHours(HORAS_EXPIRACION_DEFAULT);
        this.estado = EstadoReserva.PENDIENTE;
        
        // POLIMORFISMO: asignar prioridad según tipo de usuario
        this.prioridad = usuario.getPrioridad();
        
        // Intentar crear la reserva en el recurso
        crearReservaEnRecurso();
    }
    
    // ===============================================================
    // LÓGICA DE NEGOCIO PRINCIPAL
    // ===============================================================
    
    /**
     * Crea la reserva en el recurso si implementa Reservable
     */
    private void crearReservaEnRecurso() {
        if (recurso instanceof Reservable) {
            String reservaId = ((Reservable) recurso).reservar(usuarioId, prioridad);
            if (reservaId != null) {
                // Obtener posición en cola
                this.posicionCola = ((Reservable) recurso).getPosicionEnCola(usuarioId);
            } else {
                // No se pudo crear la reserva
                this.estado = EstadoReserva.CANCELADA;
            }
        } else {
            // El recurso no soporta reservas (ej: eBooks)
            this.estado = EstadoReserva.CANCELADA;
        }
    }
    
    /**
     * Confirma la reserva cuando el recurso está disponible
     */
    public boolean confirmar() {
        if (estado != EstadoReserva.PENDIENTE) {
            return false;
        }
        
        if (estaExpirada()) {
            cancelar();
            return false;
        }
        
        this.estado = EstadoReserva.CONFIRMADA;
        // Extender tiempo para completar el préstamo
        this.fechaExpiracion = LocalDateTime.now().plusHours(HORAS_CONFIRMACION);
        
        return true;
    }
    
    /**
     * Cancela la reserva
     */
    public boolean cancelar() {
        if (estado == EstadoReserva.COMPLETADA) {
            return false; // No se puede cancelar una reserva completada
        }
        
        // Remover de la cola del recurso
        if (recurso instanceof Reservable) {
            ((Reservable) recurso).cancelarReserva(this.id);
        }
        
        this.estado = EstadoReserva.CANCELADA;
        return true;
    }
    
    /**
     * Marca la reserva como completada (préstamo realizado)
     */
    public boolean completar() {
        if (estado != EstadoReserva.CONFIRMADA) {
            return false;
        }
        
        this.estado = EstadoReserva.COMPLETADA;
        return true;
    }
    
    /**
     * Verifica si la reserva está expirada
     */
    public boolean estaExpirada() {
        if (estado == EstadoReserva.COMPLETADA || estado == EstadoReserva.CANCELADA) {
            return false;
        }
        
        boolean expirada = LocalDateTime.now().isAfter(fechaExpiracion);
        if (expirada && estado != EstadoReserva.EXPIRADA) {
            this.estado = EstadoReserva.EXPIRADA;
        }
        
        return expirada;
    }
    
    /**
     * Calcula las horas restantes hasta la expiración
     */
    public long getHorasRestantes() {
        if (estado == EstadoReserva.COMPLETADA || estado == EstadoReserva.CANCELADA) {
            return 0;
        }
        
        long horas = ChronoUnit.HOURS.between(LocalDateTime.now(), fechaExpiracion);
        return Math.max(0, horas);
    }
    
    /**
     * Actualiza la posición en la cola
     */
    public void actualizarPosicion() {
        if (recurso instanceof Reservable) {
            this.posicionCola = ((Reservable) recurso).getPosicionEnCola(usuarioId);
        }
    }
    
    /**
     * Notifica al usuario sobre cambios en la reserva
     */
    public String generarNotificacion() {
        switch (estado) {
            case PENDIENTE:
                return String.format("Reserva creada. Posición en cola: %d. Expira en %d horas.",
                        posicionCola, getHorasRestantes());
            case CONFIRMADA:
                return String.format("¡Recurso disponible! Tienes %d horas para retirar tu préstamo.",
                        getHorasRestantes());
            case EXPIRADA:
                return "Tu reserva ha expirado. Puedes crear una nueva si lo deseas.";
            case CANCELADA:
                return "Reserva cancelada exitosamente.";
            case COMPLETADA:
                return "Reserva completada. Disfruta tu préstamo.";
            default:
                return "Estado de reserva desconocido.";
        }
    }
    
    // ===============================================================
    // GETTERS Y SETTERS
    // ===============================================================
    
    public String getId() { return id; }
    
    public String getUsuarioId() { return usuarioId; }
    
    public String getRecursoId() { return recursoId; }
    
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    
    public EstadoReserva getEstado() { return estado; }
    
    public int getPrioridad() { return prioridad; }
    
    public int getPosicionCola() { return posicionCola; }
    
    public Usuario getUsuario() { return usuario; }
    
    public Recurso getRecurso() { return recurso; }
    
    @Override
    public String toString() {
        return String.format("Reserva{id='%s', usuario='%s', recurso='%s', estado=%s, posición=%d, expira=%s}",
                id, usuarioId, recursoId, estado, posicionCola, fechaExpiracion);
    }
}