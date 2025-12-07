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
    
    public NodoTiquete getPrimero() {
        return inicio;
    }

    public void encolar(Tiquete nuevoTiquete) { //Encola usando prioridad: P > A > B
        NodoTiquete nuevo = new NodoTiquete(nuevoTiquete);

        if (esVacia()) {
            inicio = fin = nuevo;
            return;
        }

        NodoTiquete actual = inicio;
        NodoTiquete anterior = null;

        while (actual != null
                && compararPrioridad(actual.getTiquete(), nuevoTiquete) >= 0) { // Buscar la posición correcta según prioridad
            anterior = actual;
            actual = actual.getSiguiente();
        }

        if (anterior == null) {
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
            return;
        }

        anterior.setSiguiente(nuevo);
        nuevo.setSiguiente(actual);

        if (actual == null) {
            fin = nuevo;
        }
    }

    private int compararPrioridad(Tiquete t1, Tiquete t2) { // Devuelve positivo si t1 tiene MAYOR prioridad que t2
        return prioridad(t1.getTipo()) - prioridad(t2.getTipo());
    }

    private int prioridad(String tipo) {
        switch (tipo) {
            case "P":
                return 3; // Preferencial
            case "A":
                return 2; // 1 trámite 
            case "B":
                return 1; // 2+ trámites 
            default:
                return 0;
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
            return "No hay tiquetes en la cola";
        }
        StringBuilder sb = new StringBuilder("*** COLA DE TIQUETES ***\n");
        NodoTiquete actual = inicio;
        while (actual != null) {
            sb.append(actual.getTiquete().toString()).append("\n\n");
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }

    //Serializa la cola completa usando recursión Retorna una cadena con todos los tiquetes serializados (uno por línea)
    public String serializarCola() {
        StringBuilder sb = new StringBuilder();
        ColaRecursivo(inicio, sb);
        return sb.toString();
    }

    private void ColaRecursivo(NodoTiquete nodo, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        sb.append(nodo.getTiquete().serializar()).append("\n");
        ColaRecursivo(nodo.getSiguiente(), sb);
    }
    
    //Deserializa y carga tiquetes desde un String usando recursión
    public void deserializarCola(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }
        String[] lineas = contenido.split("\n");
        LineasRecursivo(lineas, 0);
    }

    private void LineasRecursivo(String[] lineas, int indice) {
        if (indice >= lineas.length) {
            return;
        }

        String linea = lineas[indice].trim();
        if (!linea.isEmpty()) {
            Tiquete tiquete = Tiquete.deserializar(linea);
            if (tiquete != null) {
                encolar(tiquete);
            }
        }
        LineasRecursivo(lineas, indice + 1); // Recursión
    }
}
