package Main.ui;

import Main.config.Configuracion;
import javax.swing.JOptionPane;

/**
 * Interfaz principal del sistema usando JOptionPane
 */
public class InterfazPrincipal {
    private Configuracion configuracion;
    
    public InterfazPrincipal(Configuracion configuracion) {
        this.configuracion = configuracion;
    }
    
    /**
     * Inicia el sistema mostrando el menú principal
     */
    public void iniciarSistema() {
        mostrarBienvenida();
        mostrarMenuPrincipal();
    }
    
    /**
     * Muestra mensaje de bienvenida
     */
    private void mostrarBienvenida() {
        // Generacion del mensaje de bienvenida
        String mensaje = String.format(
            "¡Bienvenido al Sistema de Gestión de %s!\n\n" +
            "Configuración actual:\n" +
            "- Número de cajas: %d\n" +
            "- Caja preferencial: %d\n" +
            "- Caja rápida: %d\n" +
            "- Primera ejecución: %s",
            configuracion.getNombreBanco(),
            configuracion.getCantidadCajas(),
            configuracion.getCajaPreferencial(),
            configuracion.getCajaRapida(),
            configuracion.isPrimerEjecucion() ? "Sí" : "No"
        );
        
        JOptionPane.showMessageDialog(null, mensaje, 
            "Sistema Bancario", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Muestra el menú principal del sistema
     */
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
    
    /**
     * Procesa la opción seleccionada del menú
     */
    private void procesarOpcionMenu(int opcion) {
        switch (opcion) {
            case 1:
                // TODO: Implementar creación de tiquetes
                JOptionPane.showMessageDialog(null, "Función en desarrollo: Crear Tiquete");
                break;
            case 2:
                // TODO: Implementar atención de tiquetes
                JOptionPane.showMessageDialog(null, "Función en desarrollo: Atender Tiquete");
                break;
            case 3:
                // TODO: Implementar visualización de colas
                JOptionPane.showMessageDialog(null, "Función en desarrollo: Estado de Colas");
                break;
            case 4:
                // TODO: Implementar reportes
                JOptionPane.showMessageDialog(null, "Función en desarrollo: Reportes");
                break;
            case 5:
                // TODO: Implementar consulta tipo de cambio
                JOptionPane.showMessageDialog(null, "Función en desarrollo: Tipo de Cambio");
                break;
            case 6:
                JOptionPane.showMessageDialog(null, "¡Gracias por usar el sistema!");
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción no válida");
                break;
        }
        
        // Volver al menú principal (excepto si es salir)
        if (opcion != 6) {
            mostrarMenuPrincipal();
        }
    }
}
