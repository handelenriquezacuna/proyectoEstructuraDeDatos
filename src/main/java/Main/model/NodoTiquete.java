/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main.model;

/**
 *
 * @author Josue
 */
public class NodoTiquete {

    private Tiquete tiquete;
    private NodoTiquete siguiente;

    public NodoTiquete(Tiquete tiquete) {
        this.tiquete = tiquete;
        this.siguiente = null;
    }

    public Tiquete getTiquete() {
        return tiquete;
    }

    public void setTiquete(Tiquete tiquete) {
        this.tiquete = tiquete;
    }

    public NodoTiquete getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoTiquete siguiente) {
        this.siguiente = siguiente;
    }
}
