/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main.model;

import javax.swing.*;

/**
 *
 * @author Josue
 */
public class GestorTiquetes {

    private final GestorCajas gestorCajas;

    public GestorTiquetes(GestorCajas gestorCajas) {
        this.gestorCajas = gestorCajas;
    }

    public void crearTiquete() {
        String nombre = JOptionPane.showInputDialog("Nombre del cliente:");
        String id = JOptionPane.showInputDialog("Identificación:");
        int edad = Integer.parseInt(JOptionPane.showInputDialog("Edad:"));
        Cliente cliente = new Cliente(nombre, id, edad);

        String tramite = (String) JOptionPane.showInputDialog(
                null, "Seleccione el trámite:", "Trámite",
                JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Depósitos", "Retiros", "Cambio de Divisas"},
                "Depósitos"
        );

        // Tipo será P/A/B
        String tipo = seleccionarTipo(cliente);

        // Caja basada en P/A/B
        Caja cajaAsignada = asignarCaja(tipo);

        Tiquete tiquete = new Tiquete(cliente, tramite, tipo, cajaAsignada.getNumero());
        cajaAsignada.getCola().encolar(tiquete);

        int personasAntes = cajaAsignada.getCola().contarElementos() - 1;
        if (personasAntes == 0 && !cajaAsignada.estaOcupada()) {
            JOptionPane.showMessageDialog(null,
                    "Caja: " + cajaAsignada.getNumero() + "\n"
                    + "Es su turno! Pase a ser atendido.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Caja: " + cajaAsignada.getNumero() + "\n"
                    + "Personas antes que usted: " + personasAntes);
        }
    }

    private String seleccionarTipo(Cliente c) {
        if (c.esPreferencial()) {
            return "P";
        }

        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "Ingrese el tipo de tiquete:\n 1: 1 trámite\n 2: 2+ trámites",
                    "Tipo de Tiquete",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == null) {
                JOptionPane.showMessageDialog(null, "Operación cancelada. Se asigna 2 (B) por defecto.");
                opcion = "2";
            } else {
                opcion = opcion.trim();
            }
        } while (!opcion.equals("1") && !opcion.equals("2"));
        
        return opcion.equals("1") ? "A" : "B";
    }

    private Caja asignarCaja(String tipo) {
        switch (tipo) {
            case "P":
                return gestorCajas.getCajaPreferencial(); // Caja 1
            case "A":
                return gestorCajas.getCajaRapida();       // Caja 2
            case "B":
            default:
                return gestorCajas.buscarCajaNormalConMenosClientes(); // Caja 3, 4, 5...
        }
    }

}
