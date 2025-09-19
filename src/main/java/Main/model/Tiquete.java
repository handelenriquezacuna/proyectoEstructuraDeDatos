package Main.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un tiquete del sistema bancario
 *
 */
public class Tiquete {
    private static int contadorTiquetes = 1;
    
    private int numeroTiquete;
    private Cliente cliente;
    private LocalDateTime horaCreacion;
    private LocalDateTime horaAtencion;
    private String tramite; // Depósitos, Retiros, Cambio de Divisas
    private char tipo; // P: preferencial, A: un trámite, B: dos+ trámites
    private int numeroCaja;
    private int posicionEnCola;
    private boolean atendido;
    
    // Enum para tipos de trámite TODO: se necesita a migrar a clase ENUM TipoTramite
    public enum TipoTramite {
        DEPOSITOS("Depósitos"),
        RETIROS("Retiros"), 
        CAMBIO_DIVISAS("Cambio de Divisas");
    // fin se necesita a migrar a clase ENUM TipoTramite

        private final String descripcion;
        TipoTramite(String descripcion) {
            this.descripcion = descripcion;
        }
        public String getDescripcion() { return descripcion; }
    }

    public Tiquete(Cliente cliente, String tramite) {
        this.numeroTiquete = contadorTiquetes++;
        this.cliente = cliente;
        this.tramite = tramite;
        this.horaCreacion = LocalDateTime.now();
        this.horaAtencion = null; // Se asigna cuando sea atendido
        this.atendido = false;
        this.posicionEnCola = 0;
        
        // Determinar tipo de tiquete
        determinarTipo();
    }
    
    /**
     * Determina el tipo de tiquete basado en el cliente y trámite
     */
    private void determinarTipo() {
        // Lógica para determinar el tipo
        if (cliente.esPreferencial()) {
            this.tipo = 'P'; // Preferencial
        } else {
            // Por simplicidad, asumimos un trámite = A, múltiples = B
            this.tipo = 'A'; // Un solo trámite (se puede expandir)
        }
    }
    
    /**
     * Marca el tiquete como atendido
     */
    public void marcarComoAtendido() {
        this.atendido = true;
        this.horaAtencion = LocalDateTime.now();
    }
    
    /**
     * Calcula el tiempo de espera en minutos
     */
    public long getTiempoEsperaMinutos() {
        if (horaAtencion == null) return 0;
        return java.time.Duration.between(horaCreacion, horaAtencion).toMinutes();
    }
    
    // Getters y Setters

    public static int getContadorTiquetes() {
        return contadorTiquetes;
    }

    public static void setContadorTiquetes(int contadorTiquetes) {
        Tiquete.contadorTiquetes = contadorTiquetes;
    }

    public int getNumeroTiquete() {
        return numeroTiquete;
    }

    public void setNumeroTiquete(int numeroTiquete) {
        this.numeroTiquete = numeroTiquete;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(LocalDateTime horaCreacion) {
        this.horaCreacion = horaCreacion;
    }

    public LocalDateTime getHoraAtencion() {
        return horaAtencion;
    }

    public void setHoraAtencion(LocalDateTime horaAtencion) {
        this.horaAtencion = horaAtencion;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getNumeroCaja() {
        return numeroCaja;
    }

    public void setNumeroCaja(int numeroCaja) {
        this.numeroCaja = numeroCaja;
    }

    public int getPosicionEnCola() {
        return posicionEnCola;
    }

    public void setPosicionEnCola(int posicionEnCola) {
        this.posicionEnCola = posicionEnCola;
    }

    public boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("Tiquete #%d | Cliente: %s | Trámite: %s | Tipo: %c | Creado: %s", 
                           numeroTiquete, cliente.getNombre(), tramite, tipo, 
                           horaCreacion.format(formatter));
    }
}
