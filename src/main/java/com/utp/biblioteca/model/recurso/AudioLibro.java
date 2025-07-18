package com.utp.biblioteca.model.recurso;

import com.utp.biblioteca.model.interfaces.Prestable;
import java.time.LocalDate;

/**
 * AudioLibro - HERENCIA + IMPLEMENTACIÓN SELECTIVA
 * Implementa: Prestable, Buscable (heredada)
 * NO implementa Reservable - Los AudioLibros son recursos digitales ilimitados
 */
public class AudioLibro extends Recurso implements Prestable {
    
    private int duracionMinutos;
    private String formatoAudio; // MP3, M4A, WAV, etc.
    private String narrador;
    private String calidad; // Alta, Media, Baja
    private double tamanoMB;
    private String urlStreaming;
    private boolean permiteDescarga;
    
    public AudioLibro(String titulo, String autor, String categoria, LocalDate fechaPublicacion,
                     int duracionMinutos, String formatoAudio, String narrador, String calidad) {
        super(titulo, autor, categoria, fechaPublicacion);
        this.duracionMinutos = duracionMinutos;
        this.formatoAudio = formatoAudio;
        this.narrador = narrador;
        this.calidad = calidad;
        this.permiteDescarga = false; // Por defecto solo streaming
        calcularTamanoEstimado();
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // ===============================================================
    
    @Override
    public double calcularMulta(int diasRetraso) {
        // Los AudioLibros no generan multas (streaming/acceso temporal)
        return 0.0;
    }
    
    @Override
    public int getTiempoPrestamoPorDefecto() {
        return 21; // 21 días para audiolibros (más tiempo por duración)
    }
    
    @Override
    public boolean puedeSerRenovado() {
        return true; // Los audiolibros siempre se pueden renovar
    }
    
    @Override
    public String getInformacionEspecifica() {
        return String.format("Duración: %s | Formato: %s | Narrador: %s | Calidad: %s | Tamaño: %.2f MB",
                formatearDuracion(), formatoAudio, narrador, calidad, tamanoMB);
    }
    
    @Override
    public boolean validarCondicionPrestamo() {
        return true; // Los audiolibros siempre están disponibles
    }
    
    // ===============================================================
    // IMPLEMENTACIÓN DE INTERFACE PRESTABLE
    // ===============================================================
    
    @Override
    public boolean prestar() {
        // Los audiolibros se "prestan" otorgando acceso de streaming
        numeroVecesPrestado++;
        fechaUltimoPrestamo = LocalDate.now();
        return true;
    }
    
    @Override
    public boolean devolver() {
        // Los audiolibros se "devuelven" revocando acceso de streaming
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
    // MÉTODOS ESPECÍFICOS DEL AUDIOLIBRO
    // ===============================================================
    
    /**
     * Genera URL de streaming temporal para el usuario
     */
    public String generarUrlStreaming(String usuarioId) {
        String token = usuarioId + "_" + System.currentTimeMillis();
        return urlStreaming + "?token=" + token + "&quality=" + calidad.toLowerCase();
    }
    
    /**
     * Calcula el tamaño estimado basado en duración y calidad
     */
    private void calcularTamanoEstimado() {
        double factorCalidad;
        switch (calidad.toLowerCase()) {
            case "alta":
                factorCalidad = 1.5; // 1.5 MB por minuto aprox
                break;
            case "media":
                factorCalidad = 1.0; // 1.0 MB por minuto aprox
                break;
            case "baja":
                factorCalidad = 0.5; // 0.5 MB por minuto aprox
                break;
            default:
                factorCalidad = 1.0;
        }
        this.tamanoMB = duracionMinutos * factorCalidad;
    }
    
    /**
     * Formatea la duración en formato legible
     */
    private String formatearDuracion() {
        int horas = duracionMinutos / 60;
        int minutos = duracionMinutos % 60;
        return String.format("%d:%02d horas", horas, minutos);
    }
    
    /**
     * Verifica si el formato es compatible con un dispositivo
     */
    public boolean esCompatibleCon(String dispositivo) {
        switch (dispositivo.toLowerCase()) {
            case "ios":
                return formatoAudio.equals("M4A") || formatoAudio.equals("MP3");
            case "android":
                return !formatoAudio.equals("M4A"); // Android no maneja bien M4A
            case "web":
                return formatoAudio.equals("MP3"); // MP3 es más universal en web
            default:
                return formatoAudio.equals("MP3");
        }
    }
    
    // ===============================================================
    // GETTERS Y SETTERS ESPECÍFICOS
    // ===============================================================
    
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { 
        this.duracionMinutos = duracionMinutos;
        calcularTamanoEstimado(); // Recalcular tamaño
    }
    
    public String getFormatoAudio() { return formatoAudio; }
    public void setFormatoAudio(String formatoAudio) { this.formatoAudio = formatoAudio; }
    
    public String getNarrador() { return narrador; }
    public void setNarrador(String narrador) { this.narrador = narrador; }
    
    public String getCalidad() { return calidad; }
    public void setCalidad(String calidad) { 
        this.calidad = calidad;
        calcularTamanoEstimado(); // Recalcular tamaño
    }
    
    public double getTamanoMB() { return tamanoMB; }
    
    public String getUrlStreaming() { return urlStreaming; }
    public void setUrlStreaming(String urlStreaming) { this.urlStreaming = urlStreaming; }
    
    public boolean isPermiteDescarga() { return permiteDescarga; }
    public void setPermiteDescarga(boolean permiteDescarga) { this.permiteDescarga = permiteDescarga; }
    
    public String getDuracionFormateada() { return formatearDuracion(); }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [Duración: %s, Formato: %s, Narrador: %s, %.2f MB]",
                formatearDuracion(), formatoAudio, narrador, tamanoMB);
    }
}