/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main.model;

import Main.config.Configuracion;
import Main.persistence.ArchivoManager;

/**
 *
 * @author Josue
 */
public class GestorCajas {

    private Caja cajaPreferencial;
    private Caja cajaRapida;
    private final Caja[] cajasNormales;
    private final ArchivoManager archivoManager = new ArchivoManager();
    private final String RUTA_COLAS = "src/main/resources/data/colasTemp.txt";

    public GestorCajas(Configuracion config) {
        this.cajaPreferencial = new Caja(config.getCajaPreferencial());
        this.cajaRapida = new Caja(config.getCajaRapida());

        int[] normales = config.getCajasNormales();
        cajasNormales = new Caja[normales.length];
        for (int i = 0; i < normales.length; i++) {
            cajasNormales[i] = new Caja(normales[i]);
        }
    }

    public Caja buscarCajaNormalConMenosClientes() {
        Caja menor = cajasNormales[0];
        for (Caja c : cajasNormales) {
            if (c.personasEnFila() < menor.personasEnFila()) {
                menor = c;
            }
        }
        return menor;
    }

    public void guardarEstadoCajas() {
        ColaTiquetes[] todas = new ColaTiquetes[cajasNormales.length + 2];
        todas[0] = cajaPreferencial.getCola();
        todas[1] = cajaRapida.getCola();
        for (int i = 0; i < cajasNormales.length; i++) {
            todas[i + 2] = cajasNormales[i].getCola();
        }
        archivoManager.guardarColas(RUTA_COLAS, todas);
    }

    // Cargar todas las colas
    public void cargarEstadoCajas() {
        int cantidad = cajasNormales.length + 2;
        ColaTiquetes[] todas = archivoManager.cargarColas(RUTA_COLAS, cantidad);

        // Limpiar colas actuales antes de cargar
        this.cajaPreferencial = new Caja(cajaPreferencial.getNumero());
        this.cajaRapida = new Caja(cajaRapida.getNumero());
        for (int i = 0; i < cajasNormales.length; i++) {
            cajasNormales[i] = new Caja(cajasNormales[i].getNumero());
        }

        copiarCola(todas[0], cajaPreferencial.getCola());
        copiarCola(todas[1], cajaRapida.getCola());

        for (int i = 0; i < cajasNormales.length; i++) {
            copiarCola(todas[i + 2], cajasNormales[i].getCola());
        }
    }

    private void copiarCola(ColaTiquetes origen, ColaTiquetes destino) {
        NodoTiquete nodo = origen.getPrimero();
        while (nodo != null) {
            destino.encolar(nodo.getTiquete());
            nodo = nodo.getSiguiente();
        }
    }

    public Caja getCajaPreferencial() {
        return cajaPreferencial;
    }

    public Caja getCajaRapida() {
        return cajaRapida;
    }

    public Caja[] getCajasNormales() {
        return cajasNormales;
    }
}
