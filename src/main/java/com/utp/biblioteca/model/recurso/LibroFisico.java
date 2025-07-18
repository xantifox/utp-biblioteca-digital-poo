package com.utp.biblioteca.model.recurso;

import com.utp.biblioteca.model.interfaces.Prestable;
import com.utp.biblioteca.model.interfaces.Reservable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * LibroFisico - HERENCIA + IMPLEMENTACIÓN MÚLTIPLE DE INTERFACES
 * Implementa: Prestable, Reservable, Buscable (heredada)
 * Representa recursos físicos con limitaciones de disponibilidad
 */
public class LibroFisico extends Recurso implements Prestable, Reservable {
    
    // Atributos específicos del libro físico
    private String isbn;
    private int numeroPaginas;
    private String editorial;
    private String ubicacion; // Estantería, sala, etc.
    private String estado; // Excelente, Bueno, Regular, Dañado
    
    // Gestión de reservas (solo para libros físicos)
    private ConcurrentLinkedQueue<Reserva> colaReservas;
    private int maxReservas;
    
    /**
     * Clase interna para manejar reservas
     */
    private static class Reserva {
        String usuarioId;
        LocalDateTime fechaReserva;
        LocalDateTime fechaExpiracion;
        int prioridad;
        
        Reserva(String usuarioId, int prioridad) {
            this.usuarioId = usuarioId;
            this.fechaReserva = LocalDateTime.now();
            this.fechaExpiracion = fechaReserva.plusHours(48); // 48 horas para confirmar
            this.prioridad = prioridad;
        }
    }
    
    public LibroFisico(String titulo, String autor, String categoria, LocalDate fechaPublicacion,
                      String isbn, int numeroPaginas, String editorial, String ubicacion) {
        super(titulo, autor, categoria, fechaPublicacion);
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
        this.editorial = editorial;
        this.ubicacion = ubicacion;
        this.estado = "Excelente";
        this.colaReservas = new ConcurrentLinkedQueue<>();
        this.maxReservas = 10;
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // ===============================================================
    
    @Override
    public double calcularMulta(int diasRetraso) {
        // Libros físicos: S/1.00 por día de retraso
        double multaBase = diasRetraso * 1.0;
        
        // Multa adicional si el libro está en mal estado
        if ("Dañado".equals(estado)) {
            multaBase += 5.0; // Multa adicional por daño
        }
        
        return multaBase;
    }
    
    @Override
    public int getTiempoPrestamoPorDefecto() {
        return 7; // 7 días para libros físicos
    }
    
    @Override
    public boolean puedeSerRenovado() {
        // No se puede renovar si hay reservas pendientes o está dañado
        return colaReservas.isEmpty() && !"Dañado".equals(estado);
    }
    
    @Override
    public String getInformacionEspecifica() {
        return String.format("ISBN: %s | Páginas: %d | Editorial: %s | Ubicación: %s | Estado: %s",
                isbn, numeroPaginas, editorial, ubicacion, estado);
    }
    
    @Override
    public boolean validarCondicionPrestamo() {
        return disponible && !"Dañado".equals(estado);
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE INTERFACE PRESTABLE
    // ===============================================================
    
    @Override
    public boolean prestar() {
        if (!validarCondicionPrestamo()) {
            return false;
        }
        
        registrarPrestamo();
        return true;
    }
    
    @Override
    public boolean devolver() {
        if (!disponible) {
            registrarDevolucion();
            // Notificar al siguiente en la cola de reservas
            if (!colaReservas.isEmpty()) {
                notificarSiguienteEnCola();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean puedeRenovar() {
        return puedeSerRenovado();
    }
    
    @Override
    public int calcularDiasPrestamo() {
        return getTiempoPrestamoPorDefecto();
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE INTERFACE RESERVABLE
    // ===============================================================
    
    @Override
    public String reservar(String usuarioId, int prioridad) {
        if (disponible || colaReservas.size() >= maxReservas) {
            return null; // No se puede reservar si está disponible o cola llena
        }
        
        // Verificar que el usuario no tenga ya una reserva
        for (Reserva reserva : colaReservas) {
            if (reserva.usuarioId.equals(usuarioId)) {
                return null; // Usuario ya tiene reserva
            }
        }
        
        Reserva nuevaReserva = new Reserva(usuarioId, prioridad);
        colaReservas.offer(nuevaReserva);
        
        // Reordenar por prioridad (Profesor > Estudiante)
        reordenarColaPorPrioridad();
        
        return nuevaReserva.usuarioId + "_" + nuevaReserva.fechaReserva.toString();
    }
    
    @Override
    public boolean cancelarReserva(String reservaId) {
        String[] partes = reservaId.split("_");
        if (partes.length != 2) return false;
        
        String usuarioId = partes[0];
        colaReservas.removeIf(reserva -> reserva.usuarioId.equals(usuarioId));
        return true;
    }
    
    @Override
    public boolean tieneReservasPendientes() {
        // Limpiar reservas expiradas primero
        procesarReservasExpiradas(48);
        return !colaReservas.isEmpty();
    }
    
    @Override
    public int getPosicionEnCola(String usuarioId) {
        int posicion = 1;
        for (Reserva reserva : colaReservas) {
            if (reserva.usuarioId.equals(usuarioId)) {
                return posicion;
            }
            posicion++;
        }
        return 0; // No encontrado
    }
    
    @Override
    public String notificarSiguienteEnCola() {
        procesarReservasExpiradas(48); // Limpiar expiradas
        
        Reserva siguiente = colaReservas.poll(); // Obtener y remover el primero
        if (siguiente != null) {
            // Aquí se implementaría la lógica de notificación real
            // Por ahora retornamos el ID del usuario a notificar
            return siguiente.usuarioId;
        }
        return null;
    }
    
    @Override
    public int procesarReservasExpiradas(int horasExpiracion) {
        int expiradas = 0;
        LocalDateTime limite = LocalDateTime.now().minusHours(horasExpiracion);
        
        List<Reserva> reservasValidas = new ArrayList<>();
        for (Reserva reserva : colaReservas) {
            if (reserva.fechaExpiracion.isAfter(limite)) {
                reservasValidas.add(reserva);
            } else {
                expiradas++;
            }
        }
        
        colaReservas.clear();
        colaReservas.addAll(reservasValidas);
        
        return expiradas;
    }
    
    // ===============================================================
    // MÉTODOS AUXILIARES
    // ===============================================================
    
    /**
     * Reordena la cola de reservas por prioridad (Profesor > Estudiante)
     */
    private void reordenarColaPorPrioridad() {
        List<Reserva> lista = new ArrayList<>(colaReservas);
        lista.sort((r1, r2) -> {
            // Primero por prioridad (mayor a menor)
            int compPrioridad = Integer.compare(r2.prioridad, r1.prioridad);
            if (compPrioridad != 0) return compPrioridad;
            
            // Luego por fecha (FIFO para misma prioridad)
            return r1.fechaReserva.compareTo(r2.fechaReserva);
        });
        
        colaReservas.clear();
        colaReservas.addAll(lista);
    }
    
    // ===============================================================
    // GETTERS Y SETTERS ESPECÍFICOS
    // ===============================================================
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public int getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(int numeroPaginas) { this.numeroPaginas = numeroPaginas; }
    
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public int getNumeroReservas() { 
        procesarReservasExpiradas(48);
        return colaReservas.size(); 
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [ISBN: %s, Páginas: %d, Editorial: %s, Ubicación: %s, Reservas: %d]",
                isbn, numeroPaginas, editorial, ubicacion, getNumeroReservas());
    }
}