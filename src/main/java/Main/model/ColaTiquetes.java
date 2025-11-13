/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main.model;

import javax.swing.JOptionPane;

/**
 *
 * @author Josue
 */
public class ColaTiquetes {

    private NodoTiquete inicio;
    private NodoTiquete fin;

    public boolean esVacia() {
        return inicio == null;
    }

    public void encolar(Tiquete tiquete) {
        NodoTiquete n = new NodoTiquete(tiquete);
        if (esVacia()) {
            inicio = fin = n;
        } else {
            fin.setSiguiente(n);
            fin = n;
        }
    }

    public Tiquete desencolar() {
        if (esVacia()) {
            return null;
        }

        Tiquete tiquete = inicio.getTiquete(); // guardar el tiquete que está al frente

        if (inicio == fin) {
            // solo un elemento
            inicio = fin = null;
        } else {
            inicio = inicio.getSiguiente(); // avanzar el inicio
        }

        return tiquete; // devolver el tiquete atendido
    }

    public int contarElementos() {
        int contador = 0;
        NodoTiquete actual = inicio;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public String mostrarCola() {
        if (esVacia()) {
            return "No hay tiquetes en la cola.";
        }
        StringBuilder sb = new StringBuilder("=== COLA DE TIQUETES ===\n");
        NodoTiquete actual = inicio;
        while (actual != null) {
            sb.append(actual.getTiquete().toString()).append("\n\n");
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }

    /**
     * Serializa la cola completa usando recursión
     * Retorna una cadena con todos los tiquetes serializados (uno por línea)
     */
    public String serializarCola() {
        StringBuilder sb = new StringBuilder();
        serializarColaRecursivo(primero, sb);
        return sb.toString();
    }

    private void serializarColaRecursivo(NodoTiquete nodo, StringBuilder sb) {
        if (nodo == null) {
            return; // Caso base
        }
        sb.append(nodo.getTiquete().serializar()).append("\n");
        serializarColaRecursivo(nodo.getSiguiente(), sb); // Recursión
    }

    /**
     * Deserializa y carga tiquetes desde un String usando recursión
     */
    public void deserializarCola(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }
        String[] lineas = contenido.split("\n");
        deserializarLineasRecursivo(lineas, 0);
    }

    private void deserializarLineasRecursivo(String[] lineas, int indice) {
        if (indice >= lineas.length) {
            return; // Caso base: fin del array
        }

        String linea = lineas[indice].trim();
        if (!linea.isEmpty()) {
            Tiquete tiquete = Tiquete.deserializar(linea);
            if (tiquete != null) {
                encolar(tiquete);
            }
        }

        deserializarLineasRecursivo(lineas, indice + 1); // Recursión
    }

    public NodoTiquete getPrimero() {
        return primero;
    }
}
