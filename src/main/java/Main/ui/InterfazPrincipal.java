package Main.ui;

import Main.config.Configuracion;
import Main.model.Cliente;
import Main.model.Tiquete;
import Main.model.ColaTiquetes;
import Main.model.GrafoComplementarios;
import Main.persistence.ArchivoManager;
import Main.servicios.ServicioTipoCambio;

import javax.swing.JOptionPane;

/**
 * Interfaz principal del sistema usando JOptionPane
 */
public class InterfazPrincipal {

    private Configuracion configuracion;
    private ColaTiquetes colaTiquetes;
    private GrafoComplementarios grafoComplementarios;
    private ArchivoManager archivoManager;
    private ServicioTipoCambio servicioTipoCambio;

    private static final String ARCHIVO_COLAS = "src/main/resources/data/colasTemp.txt";

    public InterfazPrincipal(Configuracion configuracion) {
        this.configuracion = configuracion;
        this.archivoManager = new ArchivoManager();
        this.grafoComplementarios = new GrafoComplementarios();
        this.servicioTipoCambio = new ServicioTipoCambio();

        // Cargar colas desde archivo (persistencia)
        this.colaTiquetes = archivoManager.cargarColas(ARCHIVO_COLAS);
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
            case 1:
                crearTiquete();
                break;
            case 2:
                atenderTiquete();
                break;
            case 3:
                JOptionPane.showMessageDialog(null, colaTiquetes.mostrarCola());
                break;
            case 4:
                JOptionPane.showMessageDialog(null, "Función en desarrollo: Reportes");
                break;
            case 5:
                consultarTipoCambio();
                break;
            case 6:
                guardarEstadoSistema();
                JOptionPane.showMessageDialog(null, "¡Gracias por usar el sistema!");
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción no válida");
                break;
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
            int cajaAsignada = 1; // Por ahora, caja fija

            Tiquete tiquete = new Tiquete(cliente, tramite, tipo, cajaAsignada);
            colaTiquetes.encolar(tiquete);

            JOptionPane.showMessageDialog(null,
                    "Tiquete creado exitosamente:\n\n" + tiquete.toString()
                    + "\nPersonas delante de usted: " + (colaTiquetes.contarElementos() - 1));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear tiquete: " + e.getMessage());
        }
    }

    // 🧾 Atender tiquete
    private void atenderTiquete() {
        if (colaTiquetes.estaVacia()) {
            JOptionPane.showMessageDialog(null, "No hay tiquetes en espera.");
            return;
        }

        Tiquete siguiente = colaTiquetes.desencolar();
        siguiente.atender();

        JOptionPane.showMessageDialog(null,
                "Cliente atendido:\n\n" + siguiente.toString());

        // Mostrar servicios complementarios
        String complementarios = grafoComplementarios.obtenerComplementarios(siguiente.getTramite());
        if (complementarios != null) {
            JOptionPane.showMessageDialog(null,
                    "Recuerde ofrecer: " + complementarios,
                    "Servicios Complementarios",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Consulta el tipo de cambio del día
     */
    private void consultarTipoCambio() {
        try {
            String resultado = servicioTipoCambio.obtenerTipoCambio();
            JOptionPane.showMessageDialog(null, resultado,
                    "Tipo de Cambio del Día",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error consultando tipo de cambio: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda el estado de las colas antes de salir
     */
    private void guardarEstadoSistema() {
        try {
            archivoManager.guardarColas(ARCHIVO_COLAS, colaTiquetes);
            System.out.println("Estado del sistema guardado exitosamente");
        } catch (Exception e) {
            System.err.println("Error guardando estado del sistema: " + e.getMessage());
        }
    }
}
