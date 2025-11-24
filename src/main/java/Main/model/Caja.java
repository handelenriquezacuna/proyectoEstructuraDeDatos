/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main.model;

/**
 *
 * @author Josue
 */
public class Caja {
    private int numero;
    private boolean ocupada;
    private ColaTiquetes cola;  // la cola asignada a esa caja

    public Caja(int numero) {
        this.numero = numero;
        this.ocupada = false; // inicia vacía
        this.cola = new ColaTiquetes();
    }

    public int getNumero() { 
        return numero; 
    }
    public boolean estaOcupada() { 
        return ocupada; 
    }
    public void setOcupada(boolean valor) { 
        this.ocupada = valor; 
    }
    public ColaTiquetes getCola() { 
        return cola; 
    }

    public int personasEnFila() {
        return cola.contarElementos();
    }
}