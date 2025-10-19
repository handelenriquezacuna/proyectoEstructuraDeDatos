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
}
