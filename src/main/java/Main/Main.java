/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Main;

import Main.config.Configuracion;
import Main.ui.InterfazLogin;


/**
 * Clase principal para ejecucion de programa de atencion.
 * @author handelenriquez
 */
public class Main {
    public static void main(String[] args) {
     System.out.println("SISTEMA DE GESTION DE ATENCION");
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
