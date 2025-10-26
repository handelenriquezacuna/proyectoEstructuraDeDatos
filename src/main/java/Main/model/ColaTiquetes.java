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
}
