package Main.ui;

import Main.config.Configuracion;
import Main.model.Cliente;
import Main.model.Tiquete;
import Main.model.ColaTiquetes;
import java.awt.HeadlessException;

import javax.swing.JOptionPane;

/**
 * Interfaz principal del sistema usando JOptionPane
 */
public class InterfazPrincipal {

    private final Configuracion configuracion;
    private final ColaTiquetes colaTiquetes;

    public InterfazPrincipal(Configuracion configuracion) {
        this.configuracion = configuracion;
        this.colaTiquetes = new ColaTiquetes(); // Tu estructura personalizada
    }

    public void iniciarSistema() {
        mostrarBienvenida();
        mostrarMenuPrincipal();
    }

    private void mostrarBienvenida() {
        String mensaje = String.format(
                "¡Bienvenido al Sistema de Gestión de %s!\n\n"
                + "Configuración actual:\n"
                + "- Número de cajas: %d\n"
                + "- Caja preferencial: %d\n"
                + "- Caja rápida: %d\n"
                + "- Primera ejecución: %s",
                configuracion.getNombreBanco(),
                configuracion.getCantidadCajas(),
                configuracion.getCajaPreferencial(),
                configuracion.getCajaRapida(),
                configuracion.isPrimerEjecucion() ? "Sí" : "No"
        );

        JOptionPane.showMessageDialog(null, mensaje,
                "Sistema Bancario", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMenuPrincipal() {
        String[] opciones = {
            "1. Crear Tiquete",
            "2. Atender Tiquete",
            "3. Ver Estado de Colas",
            "4. Generar Reportes",
            "5. Consultar Tipo de Cambio",
            "6. Salir"
        };

        String menu = "=== MENÚ PRINCIPAL ===\n\nSeleccione una opción:";

        int opcion = JOptionPane.showOptionDialog(null, menu,
                configuracion.getNombreBanco(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        procesarOpcionMenu(opcion + 1);
    }

    private void procesarOpcionMenu(int opcion) {
        switch (opcion) {
            case 1 -> crearTiquete();
            case 2 -> atenderTiquete();
            case 3 -> JOptionPane.showMessageDialog(null, colaTiquetes.mostrarCola());
            case 4 -> JOptionPane.showMessageDialog(null, "Función en desarrollo: Reportes");
            case 5 -> JOptionPane.showMessageDialog(null, "Función en desarrollo: Tipo de Cambio");
            case 6 -> {
                JOptionPane.showMessageDialog(null, "¡Gracias por usar el sistema!");
                System.exit(0);
            }
            default -> JOptionPane.showMessageDialog(null, "Opción no válida");
        }

        if (opcion != 6) {
            mostrarMenuPrincipal();
        }
    }

    // 🧾 Crear tiquete
    private void crearTiquete() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente:");
            String id = JOptionPane.showInputDialog("Ingrese el ID del cliente:");
            int edad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la edad del cliente:"));
            String tramite = JOptionPane.showInputDialog("Ingrese el trámite (Depósitos, Retiros, Cambio de Divisas):");
            String tipo = JOptionPane.showInputDialog("Ingrese el tipo (P, A, B):").toUpperCase();

            Cliente cliente = new Cliente(nombre, id, edad);
            int cajaAsignada = configuracion.getCajaPreferencial(); // Caja preferencial

            Tiquete tiquete = new Tiquete(cliente, tramite, tipo, cajaAsignada);
            colaTiquetes.encolar(tiquete);

            // 🔹 NUEVO: Verificar si la caja está libre (cola vacía antes de agregar)
            if (colaTiquetes.contarElementos() == 1) {
                JOptionPane.showMessageDialog(null,
                        "La caja está libre. Puede ser atendido inmediatamente.");
            }

            JOptionPane.showMessageDialog(null,
                    "Tiquete creado exitosamente:\n\n" + tiquete.toString()
                    + "\n\nPersonas delante de usted: " + (colaTiquetes.contarElementos() - 1));

        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al crear tiquete: " + e.getMessage());
        }
    }
        
        // 🧾 Atender tiquete
    private void atenderTiquete() {
        if (colaTiquetes.esVacia()) {
            JOptionPane.showMessageDialog(null, "No hay tiquetes en espera.");
            return;
        }

        Tiquete siguiente = colaTiquetes.desencolar();
        siguiente.atender();

        JOptionPane.showMessageDialog(null,
                "Cliente atendido:\n\n" + siguiente.toString());
    }
}
