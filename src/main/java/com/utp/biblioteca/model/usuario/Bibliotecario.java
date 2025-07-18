package com.utp.biblioteca.model.usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase Bibliotecario - HERENCIA
 * Especialización de Usuario con privilegios administrativos completos
 */
public class Bibliotecario extends Usuario {
    
    private String area; // Catalogación, Servicios, Sistemas, etc.
    private String turno; // Mañana, Tarde, Noche
    private boolean esAdministrador;
    private List<String> especialidades;
    
    public Bibliotecario(String nombre, String email, String password,
                        String area, String turno) {
        super(nombre, email, password);
        this.area = area;
        this.turno = turno;
        this.esAdministrador = false;
        this.especialidades = new ArrayList<>();
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // ===============================================================
    
    @Override
    public int getLimitePrestamos() {
        return Integer.MAX_VALUE; // Sin límite para bibliotecarios
    }
    
    @Override
    public int getDiasPrestamo() {
        return 30; // 30 días para bibliotecarios
    }
    
    @Override
    public boolean tienePermiso(String operacion) {
        // Bibliotecarios tienen acceso completo
        return true;
    }
    
    @Override
    public int getPrioridad() {
        return 0; // Prioridad mínima (no necesitan reservar, pueden acceder directamente)
    }
    
    // ===============================================================
    // MÉTODOS ESPECÍFICOS DEL BIBLIOTECARIO
    // ===============================================================
    
    /**
     * Procesar préstamo manual (función administrativa)
     */
    public boolean procesarPrestamoManual(String usuarioId, String recursoId) {
        return tienePermiso("ADMINISTRAR_PRESTAMOS");
    }
    
    /**
     * Generar reporte del sistema
     */
    public String generarReporte(String tipoReporte, LocalDate fechaInicio, LocalDate fechaFin) {
        if (!tienePermiso("GENERAR_REPORTES")) {
            return null;
        }
        // Lógica de generación de reportes
        return String.format("Reporte %s generado del %s al %s", 
                            tipoReporte, fechaInicio, fechaFin);
    }
    
    /**
     * Administrar usuario del sistema
     */
    public boolean administrarUsuario(String usuarioId, String accion) {
        return tienePermiso("ADMINISTRAR_USUARIOS");
    }
    
    /**
     * Agregar especialidad al bibliotecario
     */
    public void agregarEspecialidad(String especialidad) {
        if (!especialidades.contains(especialidad)) {
            especialidades.add(especialidad);
        }
    }
    
    // ===============================================================
    // GETTERS Y SETTERS ESPECÍFICOS
    // ===============================================================
    
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    
    public boolean isEsAdministrador() { return esAdministrador; }
    public void setEsAdministrador(boolean esAdministrador) { 
        this.esAdministrador = esAdministrador; 
    }
    
    public List<String> getEspecialidades() { 
        return new ArrayList<>(especialidades); // Copia defensiva
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [Área: %s, Turno: %s, Admin: %s, Especialidades: %d]",
                area, turno, esAdministrador, especialidades.size());
    }
}