package com.utp.biblioteca.model.usuario;

import com.utp.biblioteca.model.interfaces.Buscable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Clase abstracta Usuario
 * Aplica ABSTRACCIÓN y ENCAPSULAMIENTO
 * Base para la jerarquía de usuarios del sistema bibliotecario
 */
public abstract class Usuario implements Buscable {
    
    // ENCAPSULAMIENTO: atributos privados
    private String id;
    private String nombre;
    private String email;
    private String password;
    private LocalDate fechaRegistro;
    private boolean activo;
    
    // Agregación: Usuario TIENE-UNA lista de préstamos
    private List<String> prestamosActivos;
    private List<String> historialPrestamos;
    private double multasPendientes;
    
    /**
     * Constructor protegido (solo subclases pueden instanciar)
     * Aplica principio de ABSTRACCIÓN
     */
    protected Usuario(String nombre, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.fechaRegistro = LocalDate.now();
        this.activo = true;
        this.prestamosActivos = new ArrayList<>();
        this.historialPrestamos = new ArrayList<>();
        this.multasPendientes = 0.0;
    }
    
    // ===============================================================
    // MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // Cada subclase implementa según sus reglas específicas
    // ===============================================================
    
    /**
     * Obtiene el límite de préstamos simultáneos según el tipo de usuario
     * POLIMORFISMO: cada subclase implementa su lógica específica
     */
    public abstract int getLimitePrestamos();
    
    /**
     * Obtiene los días de préstamo permitidos según el tipo de usuario
     * POLIMORFISMO: Estudiante=7, Profesor=15, Bibliotecario=ilimitado
     */
    public abstract int getDiasPrestamo();
    
    /**
     * Verifica si el usuario puede realizar un tipo específico de operación
     * POLIMORFISMO: cada tipo tiene permisos diferentes
     */
    public abstract boolean tienePermiso(String operacion);
    
    /**
     * Obtiene la prioridad del usuario para reservas
     * POLIMORFISMO: Profesor > Estudiante > Bibliotecario
     */
    public abstract int getPrioridad();
    
    // ===============================================================
    // MÉTODOS CONCRETOS (COMPORTAMIENTO COMÚN)
    // ===============================================================
    
    /**
     * Verifica si el usuario puede tomar un préstamo
     * Lógica común para todos los tipos de usuario
     */
    public boolean puedeTomarPrestamo() {
        return activo && 
               prestamosActivos.size() < getLimitePrestamos() && 
               multasPendientes == 0.0;
    }
    
    /**
     * Agrega un préstamo a la lista de préstamos activos
     */
    public boolean agregarPrestamo(String prestamoId) {
        if (puedeTomarPrestamo()) {
            prestamosActivos.add(prestamoId);
            historialPrestamos.add(prestamoId);
            return true;
        }
        return false;
    }
    
    /**
     * Remueve un préstamo de la lista activa al devolverlo
     */
    public boolean removerPrestamo(String prestamoId) {
        return prestamosActivos.remove(prestamoId);
    }
    
    /**
     * Agrega una multa al usuario
     */
    public void agregarMulta(double monto) {
        this.multasPendientes += monto;
    }
    
    /**
     * Procesa el pago de multas
     */
    public boolean pagarMultas(double montoPago) {
        if (montoPago >= multasPendientes) {
            multasPendientes = 0.0;
            return true;
        }
        multasPendientes -= montoPago;
        return false;
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE INTERFACE BUSCABLE
    // ===============================================================
    
    @Override
    public boolean buscarPorTitulo(String titulo) {
        // Para usuarios, búsqueda por nombre
        return this.nombre.toLowerCase().contains(titulo.toLowerCase());
    }
    
    @Override
    public boolean buscarPorAutor(String autor) {
        // No aplica para usuarios
        return false;
    }
    
    @Override
    public boolean buscarPorCategoria(String categoria) {
        // Buscar por tipo de usuario
        return this.getClass().getSimpleName().toLowerCase().contains(categoria.toLowerCase());
    }
    
    @Override
    public boolean buscarPorPalabrasClave(List<String> palabrasClave) {
        String textoBusqueda = (nombre + " " + email + " " + getClass().getSimpleName()).toLowerCase();
        return palabrasClave.stream()
                .anyMatch(palabra -> textoBusqueda.contains(palabra.toLowerCase()));
    }
    
    @Override
    public boolean aplicarFiltros(Map<String, Object> filtros) {
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            switch (filtro.getKey()) {
                case "activo":
                    if (!activo == (Boolean) filtro.getValue()) return false;
                    break;
                case "tipo":
                    if (!getClass().getSimpleName().equals(filtro.getValue())) return false;
                    break;
                case "conMultas":
                    boolean tieneMultas = multasPendientes > 0;
                    if (tieneMultas != (Boolean) filtro.getValue()) return false;
                    break;
            }
        }
        return true;
    }
    
    @Override
    public double calcularRelevancia(List<String> terminosBusqueda) {
        double relevancia = 0.0;
        String textoCompleto = (nombre + " " + email).toLowerCase();
        
        for (String termino : terminosBusqueda) {
            if (textoCompleto.contains(termino.toLowerCase())) {
                relevancia += 1.0 / terminosBusqueda.size();
            }
        }
        return Math.min(relevancia, 1.0);
    }
    
    @Override
    public String getResumenParaBusqueda() {
        return String.format("%s - %s (%s) - Préstamos: %d/%d", 
                nombre, email, getClass().getSimpleName(), 
                prestamosActivos.size(), getLimitePrestamos());
    }
    
    @Override
    public List<String> getPalabrasClaveIndexables() {
        List<String> palabras = new ArrayList<>();
        palabras.add(nombre.toLowerCase());
        palabras.add(email.toLowerCase());
        palabras.add(getClass().getSimpleName().toLowerCase());
        return palabras;
    }
    
    // ===============================================================
    // GETTERS Y SETTERS (ENCAPSULAMIENTO)
    // ===============================================================
    
    public String getId() { return id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    // Password encapsulado - no getter directo
    public boolean verificarPassword(String password) {
        return this.password.equals(password);
    }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public List<String> getPrestamosActivos() { 
        return new ArrayList<>(prestamosActivos); // Copia defensiva
    }
    
    public List<String> getHistorialPrestamos() { 
        return new ArrayList<>(historialPrestamos); // Copia defensiva
    }
    
    public double getMultasPendientes() { return multasPendientes; }
    
    @Override
    public String toString() {
        return String.format("%s{id='%s', nombre='%s', email='%s', activo=%s, préstamos=%d/%d, multas=%.2f}",
                getClass().getSimpleName(), id, nombre, email, activo, 
                prestamosActivos.size(), getLimitePrestamos(), multasPendientes);
    }
}