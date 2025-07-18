package com.utp.biblioteca.model.usuario;

import java.util.List;
import java.util.ArrayList;

/**
 * Clase Profesor - HERENCIA
 * Especialización de Usuario con privilegios académicos
 */
public class Profesor extends Usuario {
    
    private String departamento;
    private String grado; // Dr., Mg., Lic., etc.
    private String areEspecializacion;
    private boolean coordinador;
    
    public Profesor(String nombre, String email, String password,
                   String departamento, String grado, String areaEspecializacion) {
        super(nombre, email, password);
        this.departamento = departamento;
        this.grado = grado;
        this.areEspecializacion = areaEspecializacion;
        this.coordinador = false;
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // ===============================================================
    
    @Override
    public int getLimitePrestamos() {
        return coordinador ? 15 : 10; // Coordinadores tienen más límite
    }
    
    @Override
    public int getDiasPrestamo() {
        return 15; // Profesores tienen 15 días de préstamo
    }
    
    @Override
    public boolean tienePermiso(String operacion) {
        switch (operacion) {
            case "PRESTAR_LIBRO":
            case "RENOVAR_PRESTAMO":
            case "RESERVAR_LIBRO":
            case "VER_CATALOGO":
            case "BUSCAR_RECURSOS":
            case "ACCESO_RECURSOS_ESPECIALIZADOS":
            case "SOLICITAR_ADQUISICIONES":
            case "GENERAR_BIBLIOGRAFIA":
                return true;
            case "ADMINISTRAR_USUARIOS":
            case "ADMINISTRAR_CATALOGO":
                return coordinador; // Solo coordinadores
            case "GENERAR_REPORTES":
                return true; // Pueden ver reportes académicos
            default:
                return false;
        }
    }
    
    @Override
    public int getPrioridad() {
        return coordinador ? 3 : 2; // Prioridad alta para reservas
    }
    
    // ===============================================================
    // MÉTODOS ESPECÍFICOS DEL PROFESOR
    // ===============================================================
    
    /**
     * Método específico para profesores: generar bibliografía
     */
    public List<String> generarBibliografia(String materia) {
        // Lógica para generar bibliografía específica
        List<String> bibliografia = new ArrayList<>();
        // Implementación específica del negocio
        return bibliografia;
    }
    
    /**
     * Solicitar adquisición de recursos especializados
     */
    public boolean solicitarAdquisicion(String tituloRecurso, String justificacion) {
        // Lógica para solicitudes de adquisición
        return tienePermiso("SOLICITAR_ADQUISICIONES");
    }
    
    // ===============================================================
    // GETTERS Y SETTERS ESPECÍFICOS
    // ===============================================================
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    
    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }
    
    public String getAreaEspecializacion() { return areEspecializacion; }
    public void setAreaEspecializacion(String areaEspecializacion) { 
        this.areEspecializacion = areaEspecializacion; 
    }
    
    public boolean isCoordinador() { return coordinador; }
    public void setCoordinador(boolean coordinador) { this.coordinador = coordinador; }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [%s, Depto: %s, Área: %s, Coord: %s]",
                grado, departamento, areEspecializacion, coordinador);
    }
}