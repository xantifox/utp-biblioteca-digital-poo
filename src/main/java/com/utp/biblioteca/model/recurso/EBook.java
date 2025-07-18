package com.utp.biblioteca.model.recurso;

import com.utp.biblioteca.model.interfaces.Prestable;
import java.time.LocalDate;

/**
 * EBook - HERENCIA + IMPLEMENTACIÓN SELECTIVA
 * Implementa: Prestable, Buscable (heredada)
 * NO implementa Reservable - Los eBooks son recursos digitales ilimitados
 */
public class EBook extends Recurso implements Prestable {
    
    private String formato; // PDF, EPUB, MOBI, etc.
    private double tamanoMB;
    private String urlDescarga;
    private boolean requiereDRM; // Digital Rights Management
    private int limiteDescargas;
    private int descargasRealizadas;
    
    public EBook(String titulo, String autor, String categoria, LocalDate fechaPublicacion,
                String formato, double tamanoMB, String urlDescarga) {
        super(titulo, autor, categoria, fechaPublicacion);
        this.formato = formato;
        this.tamanoMB = tamanoMB;
        this.urlDescarga = urlDescarga;
        this.requiereDRM = true;
        this.limiteDescargas = 100; // Límite institucional
        this.descargasRealizadas = 0;
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // ===============================================================
    
    @Override
    public double calcularMulta(int diasRetraso) {
        // Los eBooks no generan multas por retraso (se "autodevuelven")
        return 0.0;
    }
    
    @Override
    public int getTiempoPrestamoPorDefecto() {
        return 14; // 14 días para eBooks (más tiempo que físicos)
    }
    
    @Override
    public boolean puedeSerRenovado() {
        return true; // Los eBooks siempre se pueden renovar
    }
    
    @Override
    public String getInformacionEspecifica() {
        return String.format("Formato: %s | Tamaño: %.2f MB | DRM: %s | Descargas: %d/%d",
                formato, tamanoMB, requiereDRM ? "Sí" : "No", descargasRealizadas, limiteDescargas);
    }
    
    @Override
    public boolean validarCondicionPrestamo() {
        return descargasRealizadas < limiteDescargas;
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE INTERFACE PRESTABLE
    // ===============================================================
    
    @Override
    public boolean prestar() {
        if (!validarCondicionPrestamo()) {
            return false;
        }
        
        // Los eBooks no se marcan como "no disponibles"
        // Solo incrementamos el contador de descargas
        descargasRealizadas++;
        numeroVecesPrestado++;
        fechaUltimoPrestamo = LocalDate.now();
        
        return true;
    }
    
    @Override
    public boolean devolver() {
        // Los eBooks se "devuelven" automáticamente (no hay acción real)
        // En un sistema real, se revocaría el acceso DRM
        return true;
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
    // MÉTODOS ESPECÍFICOS DEL EBOOK
    // ===============================================================
    
    /**
     * Genera un enlace temporal de descarga con token de seguridad
     */
    public String generarEnlaceDescarga(String usuarioId) {
        if (!validarCondicionPrestamo()) {
            return null;
        }
        
        // En un sistema real, esto generaría un token temporal
        String token = usuarioId + "_" + System.currentTimeMillis();
        return urlDescarga + "?token=" + token + "&user=" + usuarioId;
    }
    
    /**
     * Verifica la compatibilidad del formato con un dispositivo
     */
    public boolean esCompatibleCon(String dispositivo) {
        switch (dispositivo.toLowerCase()) {
            case "kindle":
                return formato.equals("MOBI") || formato.equals("AZW");
            case "ipad":
            case "iphone":
                return formato.equals("EPUB") || formato.equals("PDF");
            case "android":
                return !formato.equals("AZW"); // Android lee casi todo excepto AZW
            default:
                return formato.equals("PDF"); // PDF es universal
        }
    }
    
    // ===============================================================
    // GETTERS Y SETTERS ESPECÍFICOS
    // ===============================================================
    
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
    
    public double getTamanoMB() { return tamanoMB; }
    public void setTamanoMB(double tamanoMB) { this.tamanoMB = tamanoMB; }
    
    public String getUrlDescarga() { return urlDescarga; }
    public void setUrlDescarga(String urlDescarga) { this.urlDescarga = urlDescarga; }
    
    public boolean isRequiereDRM() { return requiereDRM; }
    public void setRequiereDRM(boolean requiereDRM) { this.requiereDRM = requiereDRM; }
    
    public int getLimiteDescargas() { return limiteDescargas; }
    public void setLimiteDescargas(int limiteDescargas) { this.limiteDescargas = limiteDescargas; }
    
    public int getDescargasRealizadas() { return descargasRealizadas; }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [Formato: %s, %.2f MB, Descargas: %d/%d]",
                formato, tamanoMB, descargasRealizadas, limiteDescargas);
    }
}