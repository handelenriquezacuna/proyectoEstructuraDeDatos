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

    /**
     * Encola usando prioridad: P > A > B
     */
    public void encolar(Tiquete nuevoTiquete) {
        NodoTiquete nuevo = new NodoTiquete(nuevoTiquete);

        // Caso 1: cola vacía
        if (esVacia()) {
            inicio = fin = nuevo;
            return;
        }

        NodoTiquete actual = inicio;
        NodoTiquete anterior = null;

        // Buscar la posición correcta según prioridad
        while (actual != null && compararPrioridad(actual.getTiquete(), nuevoTiquete) >= 0) {
            anterior = actual;
            actual = actual.getSiguiente();
        }

        // Caso 2: insertar al inicio (tiene mayor prioridad que todos)
        if (anterior == null) {
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
            return;
        }

        // Caso 3: insertar en medio o final
        anterior.setSiguiente(nuevo);
        nuevo.setSiguiente(actual);

        if (actual == null) {
            fin = nuevo; // si se insertó al final
        }
    }

    /**
     * Compara prioridad: Devuelve positivo si t1 tiene MAYOR prioridad que t2.
     */
    private int compararPrioridad(Tiquete t1, Tiquete t2) {
        return prioridad(t1.getTipo()) - prioridad(t2.getTipo());
    }

    private int prioridad(String tipo) {
        switch (tipo) {
            case "P":
                return 3;
            case "A":
                return 2;
            default:
                return 1; // B
        }
    }

    public Tiquete desencolar() {
        if (esVacia()) {
            return null;
        }

        Tiquete t = inicio.getTiquete();

        if (inicio == fin) {
            inicio = fin = null;
        } else {
            inicio = inicio.getSiguiente();
        }

        return t;
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
     * Serializa la cola completa usando recursión Retorna una cadena con todos
     * los tiquetes serializados (uno por línea)
     */
    public String serializarCola() {
        StringBuilder sb = new StringBuilder();
        serializarColaRecursivo(inicio, sb);
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
     *
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
        return inicio;
    }
}
