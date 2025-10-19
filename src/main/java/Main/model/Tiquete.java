package Main.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un tiquete del sistema bancario
 *
 */
public class Tiquete {

    private Cliente cliente;
    private String tramite;
    private String tipo; // P, A, B
    private LocalDateTime horaCreacion;
    private LocalDateTime horaAtencion;
    private int numeroCaja;

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
