package com.utp.biblioteca.model.usuario;

/**
 * Clase Estudiante - HERENCIA
 * Especialización de Usuario con reglas específicas para estudiantes
 */
public class Estudiante extends Usuario {
    
    // Atributos específicos del estudiante
    private String carrera;
    private int semestre;
    private String codigoEstudiante;
    
    /**
     * Constructor específico para Estudiante
     */
    public Estudiante(String nombre, String email, String password, 
                     String carrera, int semestre, String codigoEstudiante) {
        super(nombre, email, password); // Llamada al constructor padre
        this.carrera = carrera;
        this.semestre = semestre;
        this.codigoEstudiante = codigoEstudiante;
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // ===============================================================
    
    @Override
    public int getLimitePrestamos() {
        return 3; // Estudiantes pueden tener máximo 3 préstamos
    }
    
    @Override
    public int getDiasPrestamo() {
        return 7; // Estudiantes tienen 7 días de préstamo
    }
    
    @Override
    public boolean tienePermiso(String operacion) {
        switch (operacion) {
            case "PRESTAR_LIBRO":
            case "RENOVAR_PRESTAMO":
            case "RESERVAR_LIBRO":
            case "VER_CATALOGO":
            case "BUSCAR_RECURSOS":
                return true;
            case "ADMINISTRAR_USUARIOS":
            case "ADMINISTRAR_CATALOGO":
            case "GENERAR_REPORTES":
                return false;
            default:
                return false;
        }
    }
    
    @Override
    public int getPrioridad() {
        return 1; // Prioridad baja para reservas
    }
    
    // ===============================================================
    // GETTERS Y SETTERS ESPECÍFICOS
    // ===============================================================
    
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { 
        this.codigoEstudiante = codigoEstudiante; 
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [Carrera: %s, Semestre: %d, Código: %s]",
                carrera, semestre, codigoEstudiante);
    }
}