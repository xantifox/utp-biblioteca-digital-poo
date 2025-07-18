package com.utp.biblioteca.model.recurso;

import com.utp.biblioteca.model.interfaces.Buscable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Clase abstracta Recurso
 * Aplica ABSTRACCIÓN y define el contrato base para todos los recursos bibliográficos
 * Base para la jerarquía: LibroFisico, EBook, AudioLibro
 */
public abstract class Recurso implements Buscable {
    
    // ENCAPSULAMIENTO: atributos protegidos (accesibles por subclases)
    protected String id;
    protected String titulo;
    protected String autor;
    protected String categoria;
    protected LocalDate fechaPublicacion;
    protected boolean disponible;
    protected String descripcion;
    protected List<String> palabrasClave;
    
    // Estadísticas de uso
    protected int numeroVecesPrestado;
    protected LocalDate fechaUltimoPrestamo;
    
    /**
     * Constructor protegido (solo subclases pueden instanciar)
     * Aplica principio de ABSTRACCIÓN
     */
    protected Recurso(String titulo, String autor, String categoria, LocalDate fechaPublicacion) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.fechaPublicacion = fechaPublicacion;
        this.disponible = true;
        this.palabrasClave = new ArrayList<>();
        this.numeroVecesPrestado = 0;
    }
    
    // ===============================================================
    // MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // Cada tipo de recurso implementa según su naturaleza
    // ===============================================================
    
    /**
     * Calcula la multa por retraso específica del tipo de recurso
     * POLIMORFISMO: cada tipo puede tener tarifas diferentes
     */
    public abstract double calcularMulta(int diasRetraso);
    
    /**
     * Obtiene el tiempo de préstamo por defecto para este tipo de recurso
     * POLIMORFISMO: físicos vs digitales pueden tener tiempos diferentes
     */
    public abstract int getTiempoPrestamoPorDefecto();
    
    /**
     * Verifica si el recurso puede ser renovado
     * POLIMORFISMO: reglas diferentes según el tipo
     */
    public abstract boolean puedeSerRenovado();
    
    /**
     * Obtiene información específica del tipo de recurso para mostrar
     * POLIMORFISMO: cada tipo muestra información relevante
     */
    public abstract String getInformacionEspecifica();
    
    /**
     * Valida si el recurso está en condición de ser prestado
     * POLIMORFISMO: validaciones específicas por tipo
     */
    public abstract boolean validarCondicionPrestamo();
    
    // ===============================================================
    // MÉTODOS CONCRETOS (COMPORTAMIENTO COMÚN)
    // ===============================================================
    
    /**
     * Registra un préstamo del recurso
     */
    public void registrarPrestamo() {
        this.disponible = false;
        this.numeroVecesPrestado++;
        this.fechaUltimoPrestamo = LocalDate.now();
    }
    
    /**
     * Registra la devolución del recurso
     */
    public void registrarDevolucion() {
        this.disponible = true;
    }
    
    /**
     * Agrega palabras clave para mejorar búsquedas
     */
    public void agregarPalabraClave(String palabra) {
        if (!palabrasClave.contains(palabra.toLowerCase())) {
            palabrasClave.add(palabra.toLowerCase());
        }
    }
    
    /**
     * Calcula la popularidad del recurso basada en préstamos
     */
    public double calcularPopularidad() {
        // Algoritmo simple: más préstamos = más popular
        return Math.min(numeroVecesPrestado / 10.0, 1.0);
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE INTERFACE BUSCABLE (COMÚN PARA TODOS)
    // ===============================================================
    
    @Override
    public boolean buscarPorTitulo(String titulo) {
        return this.titulo.toLowerCase().contains(titulo.toLowerCase());
    }
    
    @Override
    public boolean buscarPorAutor(String autor) {
        return this.autor.toLowerCase().contains(autor.toLowerCase());
    }
    
    @Override
    public boolean buscarPorCategoria(String categoria) {
        return this.categoria.toLowerCase().equals(categoria.toLowerCase());
    }
    
    @Override
    public boolean buscarPorPalabrasClave(List<String> palabrasClave) {
        String textoBusqueda = (titulo + " " + autor + " " + descripcion + " " + 
                               String.join(" ", this.palabrasClave)).toLowerCase();
        return palabrasClave.stream()
                .anyMatch(palabra -> textoBusqueda.contains(palabra.toLowerCase()));
    }
    
    @Override
    public boolean aplicarFiltros(Map<String, Object> filtros) {
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            switch (filtro.getKey()) {
                case "disponible":
                    if (disponible != (Boolean) filtro.getValue()) return false;
                    break;
                case "categoria":
                    if (!categoria.equals(filtro.getValue())) return false;
                    break;
                case "tipo":
                    if (!getClass().getSimpleName().equals(filtro.getValue())) return false;
                    break;
                case "añoPublicacion":
                    if (fechaPublicacion.getYear() != (Integer) filtro.getValue()) return false;
                    break;
            }
        }
        return true;
    }
    
    @Override
    public double calcularRelevancia(List<String> terminosBusqueda) {
        double relevancia = 0.0;
        String textoCompleto = (titulo + " " + autor + " " + descripcion).toLowerCase();
        
        for (String termino : terminosBusqueda) {
            String terminoLower = termino.toLowerCase();
            // Título tiene más peso
            if (titulo.toLowerCase().contains(terminoLower)) {
                relevancia += 0.5;
            }
            // Autor tiene peso medio
            if (autor.toLowerCase().contains(terminoLower)) {
                relevancia += 0.3;
            }
            // Descripción tiene menos peso
            if (descripcion != null && descripcion.toLowerCase().contains(terminoLower)) {
                relevancia += 0.2;
            }
        }
        
        // Bonus por popularidad
        relevancia += calcularPopularidad() * 0.1;
        
        return Math.min(relevancia, 1.0);
    }
    
    @Override
    public String getResumenParaBusqueda() {
        return String.format("%s por %s (%s) - %s [%s]", 
                titulo, autor, fechaPublicacion.getYear(), 
                categoria, disponible ? "Disponible" : "No disponible");
    }
    
    @Override
    public List<String> getPalabrasClaveIndexables() {
        List<String> palabras = new ArrayList<>();
        palabras.add(titulo.toLowerCase());
        palabras.add(autor.toLowerCase());
        palabras.add(categoria.toLowerCase());
        palabras.addAll(this.palabrasClave);
        return palabras;
    }
    
    // ===============================================================
    // GETTERS Y SETTERS (ENCAPSULAMIENTO)
    // ===============================================================
    
    public String getId() { return id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { 
        this.fechaPublicacion = fechaPublicacion; 
    }
    
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public List<String> getPalabrasClave() { 
        return new ArrayList<>(palabrasClave); // Copia defensiva
    }
    
    public int getNumeroVecesPrestado() { return numeroVecesPrestado; }
    public LocalDate getFechaUltimoPrestamo() { return fechaUltimoPrestamo; }
    
    @Override
    public String toString() {
        return String.format("%s{id='%s', titulo='%s', autor='%s', categoria='%s', disponible=%s, préstamos=%d}",
                getClass().getSimpleName(), id, titulo, autor, categoria, disponible, numeroVecesPrestado);
    }
}