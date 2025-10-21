package Main;

import Main.config.Configuracion;
import Main.ui.InterfazLogin;


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

            // Iniciar login
            InterfazLogin login = new InterfazLogin(config);
            login.iniciarLogin(); // Solo si login es exitoso, se abrirá InterfazPrincipal

        } catch (Exception e) {
            System.err.println("Error al inicializar el sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
