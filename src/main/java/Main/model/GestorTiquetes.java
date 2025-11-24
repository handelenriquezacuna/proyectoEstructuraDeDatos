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
    private GestorCajas gestorCajas;

    public GestorTiquetes(GestorCajas gestorCajas) {
        this.gestorCajas = gestorCajas;
    }

    public void crearTiquete() {

        String nombre = JOptionPane.showInputDialog("Nombre del cliente:");
        String id = JOptionPane.showInputDialog("Identificación:");
        int edad = Integer.parseInt(JOptionPane.showInputDialog("Edad:"));

        Cliente cliente = new Cliente(nombre, id, edad);

        // Seleccionar trámite
        String tramite = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el trámite:",
                "Trámite",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Depósitos", "Retiros", "Cambio de Divisas"},
                "Depósitos"
        );

        // Seleccionar tipo (automático según el profe si querés)
        String tipo = seleccionarTipo(cliente);

        // Buscar la caja según el tipo
        Caja cajaAsignada = asignarCaja(tipo);

        // Crear el tiquete
        Tiquete tiquete = new Tiquete(cliente, tramite, tipo, cajaAsignada.getNumero());

        // Encolar
        cajaAsignada.getCola().encolar(tiquete);

        // Mostrar información al cliente
        int personasAntes = cajaAsignada.getCola().contarElementos() - 1;

        if (personasAntes == 0 && !cajaAsignada.estaOcupada()) {
            JOptionPane.showMessageDialog(null,
                    "Caja: " + cajaAsignada.getNumero() + "\n" +
                            "Es su turno! Pase a ser atendido.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Caja: " + cajaAsignada.getNumero() + "\n" +
                            "Personas antes que usted: " + personasAntes);
        }
    }

    private String seleccionarTipo(Cliente c) {
        if (c.getEdad() >= 65) return "P";

        String tipo = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el tipo de tiquete:",
                "Tipo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"A (1 trámite)", "B (2+ trámites)"},
                "A (1 trámite)"
        );

        return tipo.startsWith("A") ? "A" : "B";
    }

    private Caja asignarCaja(String tipo) {
        switch (tipo) {
            case "P":
                return gestorCajas.getCajaPreferencial();
            case "A":
                return gestorCajas.getCajaRapida();
            default:
                return gestorCajas.buscarCajaNormalConMenosClientes();
        }
    }
}