package com.utp.biblioteca.model.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Interface Buscable
 * Define el contrato para entidades que pueden ser buscadas en el catálogo.
 * Implementado por: LibroFisico, EBook, AudioLibro, Usuario
 */
public interface Buscable {
    
    /**
     * Busca por título (búsqueda parcial, case-insensitive)
     * @param titulo término de búsqueda en el título
     * @return true si coincide con el criterio
     */
    boolean buscarPorTitulo(String titulo);
    
    /**
     * Busca por autor (búsqueda parcial, case-insensitive)
     * @param autor término de búsqueda en el autor
     * @return true si coincide con el criterio
     */
    boolean buscarPorAutor(String autor);
    
    /**
     * Busca por categoría exacta
     * @param categoria categoría específica a buscar
     * @return true si pertenece a esa categoría
     */
    boolean buscarPorCategoria(String categoria);
    
    /**
     * Búsqueda por palabras clave en campos de texto
     * @param palabrasClave lista de términos a buscar
     * @return true si contiene alguna de las palabras clave
     */
    boolean buscarPorPalabrasClave(List<String> palabrasClave);
    
    /**
     * Aplica múltiples filtros de búsqueda simultáneamente
     * @param filtros mapa de criterios (campo -> valor)
     * @return true si cumple todos los criterios especificados
     */
    boolean aplicarFiltros(Map<String, Object> filtros);
    
    /**
     * Calcula la relevancia del resultado basado en los términos de búsqueda
     * @param terminosBusqueda términos utilizados en la búsqueda
     * @return puntuación de relevancia (0.0 a 1.0)
     */
    double calcularRelevancia(List<String> terminosBusqueda);
    
    /**
     * Obtiene un resumen del objeto para mostrar en resultados de búsqueda
     * @return string con información resumida para visualización
     */
    String getResumenParaBusqueda();
    
    /**
     * Obtiene las palabras clave indexables del objeto
     * @return lista de términos que se deben indexar para búsquedas
     */
    List<String> getPalabrasClaveIndexables();
}