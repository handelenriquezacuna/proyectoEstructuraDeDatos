package Main.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un tiquete del sistema bancario
 *
 */
public class Tiquete {

    private final Cliente cliente;
    private final String tramite;
    private final String tipo; // P, A, B
    private LocalDateTime horaCreacion;
    private LocalDateTime horaAtencion;
    private final int numeroCaja;

    public Tiquete(Cliente cliente, String tramite, String tipo, int numeroCaja) {
        this.cliente = cliente;
        this.tramite = tramite;
        this.tipo = tipo;
        this.numeroCaja = numeroCaja;
        this.horaCreacion = LocalDateTime.now();
        this.horaAtencion = null;
    }

    public void atender() {
        this.horaAtencion = LocalDateTime.now();
    }

    public String getEstadoAtencion() {
        return (horaAtencion == null) ? "Sin atender" : horaAtencion.toString();
    }

    public String getTramite() {
        return tramite;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getTipo() {
        return tipo;
    }

    public int getNumeroCaja() {
        return numeroCaja;
    }

    public LocalDateTime getHoraCreacion() {
        return horaCreacion;
    }

    public LocalDateTime getHoraAtencion() {
        return horaAtencion;
    }

    /**
     * Serializa el tiquete a formato String para persistencia
     * Formato: nombre|id|edad|tramite|tipo|caja|horaCreacion|horaAtencion
     */
    public String serializar() {
        StringBuilder sb = new StringBuilder();
        sb.append(cliente.getNombre()).append("|");
        sb.append(cliente.getId()).append("|");
        sb.append(cliente.getEdad()).append("|");
        sb.append(tramite).append("|");
        sb.append(tipo).append("|");
        sb.append(numeroCaja).append("|");
        sb.append(horaCreacion.toString()).append("|");
        sb.append(horaAtencion != null ? horaAtencion.toString() : "null");
        return sb.toString();
    }

    /**
     * Deserializa un String a Tiquete
     */
    public static Tiquete deserializar(String linea) {
        try {
            String[] partes = linea.split("\\|");
            if (partes.length != 8) {
                return null;
            }

            String nombre = partes[0];
            String id = partes[1];
            int edad = Integer.parseInt(partes[2]);
            String tramite = partes[3];
            String tipo = partes[4];
            int caja = Integer.parseInt(partes[5]);
            LocalDateTime horaCreacion = LocalDateTime.parse(partes[6]);
            LocalDateTime horaAtencion = partes[7].equals("null") ? null : LocalDateTime.parse(partes[7]);

            Cliente cliente = new Cliente(nombre, id, edad);
            Tiquete tiquete = new Tiquete(cliente, tramite, tipo, caja);
            tiquete.horaCreacion = horaCreacion;
            tiquete.horaAtencion = horaAtencion;

            return tiquete;
        } catch (Exception e) {
            System.err.println("Error deserializando tiquete: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return cliente.toString()
                + " | Trámite: " + tramite
                + " | Tipo: " + tipo
                + " | Caja: " + numeroCaja
                + " | Creado: " + horaCreacion
                + " | Atención: " + getEstadoAtencion();
    }
}
