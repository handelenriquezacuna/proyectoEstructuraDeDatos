package Main;

import Main.config.Configuracion;
import Main.ui.InterfazPrincipal;

/**
 * Clase Main del Sistema de Gestión Bancaria
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTIÓN BANCARIA ===");
        System.out.println("Proyecto Final - Estructuras de Datos");
        System.out.println("Iniciando sistema...\n");
        
        try {
            // Cargar configuración del sistema
            Configuracion config = new Configuracion();
            
            // Inicializar interfaz principal
            InterfazPrincipal interfaz = new InterfazPrincipal(config);
            interfaz.iniciarSistema();
            
        } catch (Exception e) {
            System.err.println("Error al inicializar el sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
