/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main.model;

/**
 *
 * @author Josue
 */
public class ColaTiquetes {

    private NodoTiquete primero;
    private NodoTiquete ultimo;

    public void encolar(Tiquete tiquete) {
        NodoTiquete nuevo = new NodoTiquete(tiquete);
        if (ultimo == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
    }

    public Tiquete desencolar() {
        if (primero == null) {
            return null;
        }
        Tiquete tiquete = primero.getTiquete();
        primero = primero.getSiguiente();
        if (primero == null) {
            ultimo = null;
        }
        return tiquete;
    }

    public boolean estaVacia() {
        return primero == null;
    }

    public int contarElementos() {
        int contador = 0;
        NodoTiquete actual = primero;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public String mostrarCola() {
        if (estaVacia()) {
            return "No hay tiquetes en la cola.";
        }
        StringBuilder sb = new StringBuilder("=== COLA DE TIQUETES ===\n");
        NodoTiquete actual = primero;
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
