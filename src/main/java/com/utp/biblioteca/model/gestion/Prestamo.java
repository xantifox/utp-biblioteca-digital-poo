package com.utp.biblioteca.model.gestion;

import com.utp.biblioteca.model.usuario.Usuario;
import com.utp.biblioteca.model.recurso.Recurso;
import com.utp.biblioteca.model.interfaces.Prestable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Clase Préstamo - ASOCIACIÓN y COMPOSICIÓN
 * Representa la relación entre un Usuario y un Recurso durante un préstamo
 * Aplica COMPOSICIÓN con Multa (un préstamo puede generar una multa)
 */
public class Prestamo {
    
    // ASOCIACIÓN: Préstamo está asociado con Usuario y Recurso
    private String id;
    private String usuarioId;
    private String recursoId;
    
    // Datos temporales del préstamo
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;
    private EstadoPrestamo estado;
    
    // Control de renovaciones
    private int numeroRenovaciones;
    private int maxRenovaciones;
    
    // COMPOSICIÓN: Un préstamo puede tener una multa
    private Multa multa;
    
    // Referencias para polimorfismo
    private Usuario usuario;
    private Recurso recurso;
    
    /**
     * Constructor para crear un nuevo préstamo
     * Aplica POLIMORFISMO al calcular días según el tipo de usuario y recurso
     */
    public Prestamo(Usuario usuario, Recurso recurso) {
        this.id = UUID.randomUUID().toString();
        this.usuarioId = usuario.getId();
        this.recursoId = recurso.getId();
        this.usuario = usuario;
        this.recurso = recurso;
        
        this.fechaPrestamo = LocalDate.now();
        this.estado = EstadoPrestamo.ACTIVO;
        this.numeroRenovaciones = 0;
        this.maxRenovaciones = 2; // Máximo 2 renovaciones por defecto
        
        // POLIMORFISMO: calcular fecha de vencimiento según tipo de usuario y recurso
        calcularFechaVencimiento();
    }
    
    // ===============================================================
    // LÓGICA DE NEGOCIO PRINCIPAL
    // ===============================================================
    
    /**
     * Calcula la fecha de vencimiento usando POLIMORFISMO
     * Combina los días del usuario y del recurso
     */
    private void calcularFechaVencimiento() {
        // POLIMORFISMO: cada tipo de usuario y recurso tiene sus propios días
        int diasUsuario = usuario.getDiasPrestamo();
        int diasRecurso = ((Prestable) recurso).calcularDiasPrestamo();
        
        // Usar el menor entre ambos (regla de negocio)
        int diasPrestamo = Math.min(diasUsuario, diasRecurso);
        
        this.fechaVencimiento = fechaPrestamo.plusDays(diasPrestamo);
    }
    
    /**
     * Procesa la devolución del préstamo
     * Aplica POLIMORFISMO y puede generar multa (COMPOSICIÓN)
     */
    public boolean procesarDevolucion() {
        if (estado != EstadoPrestamo.ACTIVO && estado != EstadoPrestamo.VENCIDO) {
            return false; // Solo se pueden devolver préstamos activos o vencidos
        }
        
        LocalDate hoy = LocalDate.now();
        this.fechaDevolucion = hoy;
        
        // POLIMORFISMO: devolver recurso según su tipo
        if (recurso instanceof Prestable) {
            ((Prestable) recurso).devolver();
        }
        
        // Remover préstamo de la lista del usuario
        usuario.removerPrestamo(this.id);
        
        // Verificar si hay multa por retraso
        if (hoy.isAfter(fechaVencimiento)) {
            generarMultaPorRetraso();
        }
        
        this.estado = EstadoPrestamo.DEVUELTO;
        return true;
    }
    
    /**
     * Renueva el préstamo si es posible
     * Aplica POLIMORFISMO para verificar condiciones
     */
    public boolean renovar() {
        if (estado != EstadoPrestamo.ACTIVO) {
            return false;
        }
        
        if (numeroRenovaciones >= maxRenovaciones) {
            return false; // Máximo de renovaciones alcanzado
        }
        
        // POLIMORFISMO: verificar si el recurso puede ser renovado
        if (recurso instanceof Prestable) {
            if (!((Prestable) recurso).puedeRenovar()) {
                return false; // El recurso no permite renovación
            }
        }
        
        // Procesar renovación
        this.numeroRenovaciones++;
        this.estado = EstadoPrestamo.RENOVADO;
        
        // Recalcular fecha de vencimiento
        calcularFechaVencimiento();
        
        return true;
    }
    
    /**
     * Genera una multa por retraso usando COMPOSICIÓN
     */
    private void generarMultaPorRetraso() {
        long diasRetraso = ChronoUnit.DAYS.between(fechaVencimiento, LocalDate.now());
        
        if (diasRetraso > 0) {
            // POLIMORFISMO: calcular multa según el tipo de recurso
            double montoMulta = 0.0;
            if (recurso instanceof Prestable) {
                montoMulta = ((Prestable) recurso).calcularMulta((int) diasRetraso);
            }
            
            if (montoMulta > 0) {
                // COMPOSICIÓN: crear multa asociada al préstamo
                this.multa = new Multa(this.id, montoMulta, "Retraso en devolución");
                
                // Agregar multa al usuario
                usuario.agregarMulta(montoMulta);
            }
        }
    }
    
    /**
     * Verifica si el préstamo está vencido
     */
    public boolean estaVencido() {
        if (estado == EstadoPrestamo.DEVUELTO) {
            return false;
        }
        
        boolean vencido = LocalDate.now().isAfter(fechaVencimiento);
        if (vencido && estado == EstadoPrestamo.ACTIVO) {
            this.estado = EstadoPrestamo.VENCIDO;
        }
        
        return vencido;
    }
    
    /**
     * Calcula los días restantes hasta el vencimiento
     */
    public int getDiasRestantes() {
        if (estado == EstadoPrestamo.DEVUELTO) {
            return 0;
        }
        
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
        return (int) Math.max(0, dias);
    }
    
    /**
     * Calcula los días de retraso si está vencido
     */
    public int getDiasRetraso() {
        if (!estaVencido()) {
            return 0;
        }
        
        LocalDate fechaComparacion = fechaDevolucion != null ? fechaDevolucion : LocalDate.now();
        long dias = ChronoUnit.DAYS.between(fechaVencimiento, fechaComparacion);
        return (int) Math.max(0, dias);
    }
    
    // ===============================================================
    // GETTERS Y SETTERS
    // ===============================================================
    
    public String getId() { return id; }
    
    public String getUsuarioId() { return usuarioId; }
    
    public String getRecursoId() { return recursoId; }
    
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    
    public EstadoPrestamo getEstado() { return estado; }
    
    public int getNumeroRenovaciones() { return numeroRenovaciones; }
    
    public int getMaxRenovaciones() { return maxRenovaciones; }
    public void setMaxRenovaciones(int maxRenovaciones) { this.maxRenovaciones = maxRenovaciones; }
    
    public Multa getMulta() { return multa; }
    
    public Usuario getUsuario() { return usuario; }
    
    public Recurso getRecurso() { return recurso; }
    
    @Override
    public String toString() {
        return String.format("Préstamo{id='%s', usuario='%s', recurso='%s', estado=%s, vencimiento=%s, renovaciones=%d}",
                id, usuarioId, recursoId, estado, fechaVencimiento, numeroRenovaciones);
    }
}