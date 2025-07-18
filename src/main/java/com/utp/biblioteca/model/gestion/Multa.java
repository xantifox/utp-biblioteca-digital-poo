package com.utp.biblioteca.model.gestion;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Clase Multa - COMPOSICIÓN
 * Representa una multa generada por un préstamo
 * Aplicación de COMPOSICIÓN: una multa no puede existir sin un préstamo
 */
public class Multa {
    
    // COMPOSICIÓN: Multa pertenece a un Préstamo específico
    private String id;
    private String prestamoId;
    
    // Información de la multa
    private double monto;
    private LocalDate fechaGeneracion;
    private LocalDate fechaPago;
    private String concepto;
    private boolean pagada;
    
    // Información adicional
    private String metodoPago;
    private String numeroTransaccion;
    
    /**
     * Constructor para crear una nueva multa
     * COMPOSICIÓN: requiere un préstamo para existir
     */
    public Multa(String prestamoId, double monto, String concepto) {
        this.id = UUID.randomUUID().toString();
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.concepto = concepto;
        this.fechaGeneracion = LocalDate.now();
        this.pagada = false;
    }
    
    // ===============================================================
    // LÓGICA DE NEGOCIO DE MULTAS
    // ===============================================================
    
    /**
     * Procesa el pago de la multa
     */
    public boolean procesarPago(double montoPagado, String metodoPago) {
        if (pagada) {
            return false; // Ya fue pagada
        }
        
        if (montoPagado < monto) {
            return false; // Pago insuficiente
        }
        
        this.pagada = true;
        this.fechaPago = LocalDate.now();
        this.metodoPago = metodoPago;
        this.numeroTransaccion = generarNumeroTransaccion();
        
        return true;
    }
    
    /**
     * Aplica un descuento a la multa (por ejemplo, pago temprano)
     */
    public boolean aplicarDescuento(double porcentajeDescuento) {
        if (pagada) {
            return false; // No se puede descontar una multa ya pagada
        }
        
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            return false; // Porcentaje inválido
        }
        
        double descuento = monto * (porcentajeDescuento / 100.0);
        this.monto -= descuento;
        this.concepto += String.format(" (Descuento %.1f%% aplicado)", porcentajeDescuento);
        
        return true;
    }
    
    /**
     * Incrementa la multa por días adicionales de retraso
     */
    public void incrementarPorDiasAdicionales(int diasAdicionales, double tarifaDiaria) {
        if (!pagada) {
            double incremento = diasAdicionales * tarifaDiaria;
            this.monto += incremento;
            this.concepto += String.format(" (+%d días adicionales)", diasAdicionales);
        }
    }
    
    /**
     * Genera un número de transacción único
     */
    private String generarNumeroTransaccion() {
        return "TXN-" + System.currentTimeMillis() + "-" + 
               String.format("%04d", (int)(Math.random() * 10000));
    }
    
    /**
     * Calcula los días transcurridos desde la generación
     */
    public long getDiasDesdeGeneracion() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaGeneracion, LocalDate.now());
    }
    
    /**
     * Verifica si la multa está vencida para pago
     */
    public boolean estaVencidaParaPago() {
        // Las multas se consideran vencidas después de 30 días sin pago
        return !pagada && getDiasDesdeGeneracion() > 30;
    }
    
    /**
     * Genera el recibo de pago
     */
    public String generarRecibo() {
        if (!pagada) {
            return null;
        }
        
        return String.format(
            "RECIBO DE PAGO\n" +
            "ID Multa: %s\n" +
            "Préstamo: %s\n" +
            "Concepto: %s\n" +
            "Monto: S/ %.2f\n" +
            "Fecha Pago: %s\n" +
            "Método: %s\n" +
            "Transacción: %s\n",
            id, prestamoId, concepto, monto, fechaPago, metodoPago, numeroTransaccion
        );
    }
    
    // ===============================================================
    // GETTERS Y SETTERS
    // ===============================================================
    
    public String getId() { return id; }
    
    public String getPrestamoId() { return prestamoId; }
    
    public double getMonto() { return monto; }
    
    public LocalDate getFechaGeneracion() { return fechaGeneracion; }
    
    public LocalDate getFechaPago() { return fechaPago; }
    
    public String getConcepto() { return concepto; }
    
    public boolean isPagada() { return pagada; }
    
    public String getMetodoPago() { return metodoPago; }
    
    public String getNumeroTransaccion() { return numeroTransaccion; }
    
    /**
     * Obtiene el estado textual de la multa
     */
    public String getEstadoTexto() {
        if (pagada) {
            return "PAGADA";
        } else if (estaVencidaParaPago()) {
            return "VENCIDA";
        } else {
            return "PENDIENTE";
        }
    }
    
    @Override
    public String toString() {
        return String.format("Multa{id='%s', préstamo='%s', monto=S/ %.2f, concepto='%s', estado='%s'}",
                id, prestamoId, monto, concepto, getEstadoTexto());
    }
}