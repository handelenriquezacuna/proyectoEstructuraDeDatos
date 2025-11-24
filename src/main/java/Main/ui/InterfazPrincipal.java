package Main.ui;

import Main.config.Configuracion;
import Main.model.Cliente;
import Main.model.Tiquete;
import Main.model.ColaTiquetes;
import Main.model.GrafoComplementarios;
import Main.persistence.ArchivoManager;
import Main.persistence.ReporteAtencion;
import Main.servicios.ServicioTipoCambio;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Interfaz principal del sistema usando JOptionPane
 */
public class InterfazPrincipal {

    private Configuracion configuracion;
    private ColaTiquetes[] colasPorCaja;
    private GrafoComplementarios grafoComplementarios;
    private ArchivoManager archivoManager;
    private ServicioTipoCambio servicioTipoCambio;

    private static final String ARCHIVO_COLAS = "src/main/resources/data/colasTemp.txt";

    public InterfazPrincipal(Configuracion configuracion) {
        this.configuracion = configuracion;
        this.archivoManager = new ArchivoManager();
        this.grafoComplementarios = new GrafoComplementarios();
        this.servicioTipoCambio = new ServicioTipoCambio();
        this.colasPorCaja = new ColaTiquetes[configuracion.getCantidadCajas()];
        for (int i = 0; i < colasPorCaja.length; i++) {
            colasPorCaja[i] = new ColaTiquetes();
        }
        // Cargar colas desde archivo (persistencia)
        this.colasPorCaja = archivoManager.cargarColas(ARCHIVO_COLAS,  configuracion.getCantidadCajas());
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
                int caja = Integer.parseInt(
                        JOptionPane.showInputDialog("¿Qué caja desea atender?")
                );
                atenderTiquete(caja);
                break;
            case 3:
                JOptionPane.showMessageDialog(null, mostrarEstadoColas());
                break;
            case 4:
                new ReporteAtencion().generarReporte();
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
        // Nombre e ID del cliente
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente:");
        String id = JOptionPane.showInputDialog("Ingrese el ID del cliente:");

        // Validar edad
        int edad = -1;
        do {
            try {
                edad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la edad del cliente:"));
                if (edad < 0) JOptionPane.showMessageDialog(null, "Edad inválida, intente de nuevo.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido para la edad.");
            }
        } while (edad < 0);

        // Preguntar condiciones preferenciales
        boolean discapacidad = JOptionPane.showConfirmDialog(null, 
                "¿El cliente tiene alguna discapacidad?", "Preferencial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        boolean embarazada = JOptionPane.showConfirmDialog(null, 
                "¿El cliente está embarazada?", "Preferencial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        boolean empresarial = JOptionPane.showConfirmDialog(null, 
                "¿El cliente es empresarial?", "Preferencial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        // Crear cliente con todas las condiciones
        Cliente cliente = new Cliente(nombre, id, edad, discapacidad, embarazada, empresarial);

        // Selección de trámite
        String tramite = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el trámite:",
                "Trámite",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Depósitos", "Retiros", "Cambio de Divisas"},
                "Depósitos"
        );

        // Determinar tipo automáticamente
        String tipo = cliente.esPreferencial() ? "P" :
                JOptionPane.showInputDialog("Ingrese el tipo de tiquete (A: 1 trámite, B: 2+ trámites):").toUpperCase();

        // Asignar caja según tipo
        int cajaAsignada = asignarCajaSegunTipo(tipo);

        // Crear y encolar tiquete
        Tiquete tiquete = new Tiquete(cliente, tramite, tipo, cajaAsignada);
        colasPorCaja[cajaAsignada - 1].encolar(tiquete);

        // Mostrar información al cliente
        int personasAntes = colasPorCaja[cajaAsignada - 1].contarElementos() - 1;

        if (personasAntes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Tiquete creado exitosamente:\n\n" + tiquete.toString()
                    + "\nAsignado a la caja: " + cajaAsignada
                    + "\n¡Es su turno! Pase a ser atendido.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Tiquete creado exitosamente:\n\n" + tiquete.toString()
                    + "\nAsignado a la caja: " + cajaAsignada
                    + "\nPersonas delante de usted: " + personasAntes);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al crear tiquete: " + e.getMessage());
        e.printStackTrace();
    }
}

    private int asignarCajaSegunTipo(String tipo) {

        if (tipo.equals("P")) {
            return configuracion.getCajaPreferencial();
        }

        if (tipo.equals("A")) {
            return configuracion.getCajaRapida();
        }

        int[] cajasNormales = configuracion.getCajasNormales();

        int cajaConMenos = cajasNormales[0];
        int min = colasPorCaja[cajaConMenos - 1].contarElementos();

        for (int caja : cajasNormales) {
            int cantidad = colasPorCaja[caja - 1].contarElementos();
            if (cantidad < min) {
                min = cantidad;
                cajaConMenos = caja;
            }
        }

        return cajaConMenos;
    }

    // 🧾 Atender tiquete
    private void atenderTiquete(int numeroCaja) {

        // Validar caja
        if (numeroCaja < 1 || numeroCaja > colasPorCaja.length) {
            JOptionPane.showMessageDialog(null, "Número de caja inválido.");
            return;
        }

        ColaTiquetes cola = colasPorCaja[numeroCaja - 1];

        if (cola.esVacia()) {
            JOptionPane.showMessageDialog(null,
                    "La caja " + numeroCaja + " no tiene clientes en espera.");
            return;
        }

        // Atender siguiente cliente
        Tiquete siguiente = cola.desencolar();
        siguiente.atender();

        JOptionPane.showMessageDialog(null,
                "Cliente atendido en caja " + numeroCaja + ":\n\n" + siguiente.toString());

        // Servicios complementarios
        String complementarios = grafoComplementarios.obtenerComplementarios(siguiente.getTramite());
        if (complementarios != null) {
            JOptionPane.showMessageDialog(null,
                    "Recuerde ofrecer: " + complementarios,
                    "Servicios Complementarios",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        registrarTransaccion(siguiente, numeroCaja);
    }
    
    private void registrarTransaccion(Tiquete t, int caja) {
        String ruta = "src/main/resources/data/registroTransacciones.txt";
      

        // ✔ FORMATO LIMPIO DE FECHA/HORA
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHora = java.time.LocalDateTime.now().format(formato);

        String linea = caja + ";"
                + t.getCliente().getId() + ";"
                + t.getCliente().getNombre() + ";"
                + t.getTramite() + ";"
                + t.getTipo() + ";"
                + t.getTiempoAtencion() + ";"
                + fechaHora;  // ← AQUÍ EL CAMBIO

        archivoManager.agregarLinea(ruta, linea);
    }
    
    
    private String mostrarEstadoColas() {
        StringBuilder sb = new StringBuilder("=== ESTADO DE TODAS LAS COLAS ===\n\n");

        for (int i = 0; i < colasPorCaja.length; i++) {
            sb.append("Caja ").append(i + 1).append(":\n")
                    .append(colasPorCaja[i].mostrarCola()) // <--- usa tu método existente
                    .append("\n\n");
        }

        return sb.toString();
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
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < colasPorCaja.length; i++) {
                sb.append("#CAJA ").append(i).append("\n");
                sb.append(colasPorCaja[i].serializarCola()).append("\n");
            }

            archivoManager.escribirArchivo(ARCHIVO_COLAS, sb.toString());

            System.out.println("Estado del sistema guardado exitosamente");

        } catch (Exception e) {
            System.err.println("Error guardando estado del sistema: " + e.getMessage());
        }
    }
}
