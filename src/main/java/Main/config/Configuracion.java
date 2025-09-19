package Main.config;

import Main.persistence.ArchivoManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuración del Sistema
 * Maneja la configuración inicial y persistente del sistema en el banco
 */
public class Configuracion {
    private String nombreBanco;
    private int cantidadCajas;
    private int cajaPreferencial;
    private int cajaRapida;
    private int[] cajasNormales;
    private boolean primerEjecucion;
    private ArchivoManager archivoManager;
    
    private static final String ARCHIVO_CONFIG = "src/main/resources/data/prod.txt";
    private String cantidadStr;

    public Configuracion() {
        this.archivoManager = new ArchivoManager();
        cargarConfiguracion();
    }
    
    /**
     * Carga la configuración desde archivo o solicita configuración inicial
     */
    private void cargarConfiguracion() {
        try{
            if (archivoManager.existeArchivo(ARCHIVO_CONFIG)) {
                cargarDesdeArchivo();
                this.primerEjecucion = false;
            } else {
                solicitarConfiguracionInicial();
                guardarConfiguracion();
                this.primerEjecucion = true;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar configuración: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Verificacion de la existencia de la configuracion valida
     */
    private boolean existeConfiguracionValida() {
        if (!archivoManager.existeArchivo(ARCHIVO_CONFIG)){
            return false;
        }
        Map<String, String> config = archivoManager.leerConfiguracion(ARCHIVO_CONFIG);
            return config.get("NombreBanco") != null &&
                    config.get("CantidadCajas") != null &&
                    !config.get("NombreBanco").trim().isEmpty() &&
                    !config.get("CantidadCajas").trim().isEmpty();
    }
    
    /**
     * Solicita configuración inicial al usuario
     */
    private void solicitarConfiguracionInicial() {
        // TODO: se necesita construir logica inicial para solicitar la configuracion al usuario
        try {
            // Agregar el nombre del banco
            this.nombreBanco = JOptionPane.showInputDialog(
                    null,
                    "Ingrese el nombre del banco:",
                    "Configuración Inicial",
                    JOptionPane.QUESTION_MESSAGE
            );
            try {
                this.cantidadCajas = Integer.parseInt(cantidadStr);
                if (this.cantidadCajas < 3) {
                    this.cantidadCajas = 5;
                    JOptionPane.showMessageDialog(null,
                            "Cantidad mínima de cajas es 3. Se establecerán 5 cajas por defecto.");
                }
            } catch (NumberFormatException e) {
                this.cantidadCajas = 5;
                JOptionPane.showMessageDialog(null,
                        "Se establecerán 5 cajas por defecto.");
            }

            // Configurar cajas especiales
            this.cajaPreferencial = 1;
            this.cajaRapida = 2;

            // Configurar cajas normales (el resto)
            int cantidadNormales = this.cantidadCajas - 2;
            this.cajasNormales = new int[cantidadNormales];
            for (int i = 0; i < cantidadNormales; i++) {
                this.cajasNormales[i] = i + 3; // Empiezan desde la caja 3
            }

            // Mostrar resumen
            StringBuilder resumen = new StringBuilder();
            resumen.append("Configuración establecida:\n\n");
            resumen.append("Banco: ").append(this.nombreBanco).append("\n");
            resumen.append("Total de cajas: ").append(this.cantidadCajas).append("\n");
            resumen.append("Caja preferencial: ").append(this.cajaPreferencial).append("\n");
            resumen.append("Caja rápida: ").append(this.cajaRapida).append("\n");
            resumen.append("Cajas normales: ");
            for (int i = 0; i < this.cajasNormales.length; i++) {
                resumen.append(this.cajasNormales[i]);
                if (i < this.cajasNormales.length - 1) resumen.append(", ");
            }

            JOptionPane.showMessageDialog(null, resumen.toString(),
                    "Configuración Completa", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.err.println("Error en configuración inicial, usando valores por defecto");
            establecerValoresPorDefecto();
        }
    }
    // Establecer valores por defecto para el banco
    private void establecerValoresPorDefecto() {
        this.nombreBanco = "Banco ABC";
        this.cantidadCajas = 5;
        this.cajaPreferencial = 1;
        this.cajaRapida = 2;
        this.cajasNormales = new int[]{3, 4, 5};
        this.primerEjecucion = true;
    }

    /**
     * Carga configuración desde archivo
     */
    private void cargarDesdeArchivo() {
        Map<String, String> config = archivoManager.leerConfiguracion(ARCHIVO_CONFIG);

        this.nombreBanco = config.getOrDefault("NombreBanco", "Banco ABC");

        // Parsing seguro de enteros
        try {
            this.cantidadCajas = Integer.parseInt(config.getOrDefault("CantidadCajas", "5"));
        } catch (NumberFormatException e) {
            this.cantidadCajas = 3;
        }

        try {
            this.cajaPreferencial = Integer.parseInt(config.getOrDefault("CajaPreferencial", "1"));
        } catch (NumberFormatException e) {
            this.cajaPreferencial = 1;
        }

        try {
            this.cajaRapida = Integer.parseInt(config.getOrDefault("CajaRapida", "2"));
        } catch (NumberFormatException e) {
            this.cajaRapida = 2;
        }

        // Configurar cajas normales logica basada en la cantidad total
        int cantidadNormales = this.cantidadCajas - 2;
        this.cajasNormales = new int[cantidadNormales];
        for (int i = 0; i < cantidadNormales; i++) {
            this.cajasNormales[i] = i + 3;
        }
    }

    private void guardarConfiguracion() {
        try {
            Map<String, String> config = new HashMap<>();
            config.put("NombreBanco", nombreBanco);
            config.put("CantidadCajas", String.valueOf(cantidadCajas));
            config.put("CajaPreferencial", String.valueOf(cajaPreferencial));
            config.put("CajaRapida", String.valueOf(cajaRapida));
            config.put("PrimerEjecucion", "false");

            // Guardar cajas normales como string
            StringBuilder cajasNormalesStr = new StringBuilder();
            for (int i = 0; i < cajasNormales.length; i++) {
                cajasNormalesStr.append(cajasNormales[i]);
                if (i < cajasNormales.length - 1) cajasNormalesStr.append(",");
            }
            config.put("CajasNormales", cajasNormalesStr.toString());

            archivoManager.guardarConfiguracion(ARCHIVO_CONFIG, config);
            System.out.println("Configuracion guardada");

        } catch (Exception e) {
            System.err.println("Error guardando configuracion: " + e.getMessage());
        }
    }
    // Getters
    public String getNombreBanco() {
        return nombreBanco;
    }

    public int getCantidadCajas() {
        return cantidadCajas;
    }

    public int getCajaPreferencial() {
        return cajaPreferencial;
    }

    public int getCajaRapida() {
        return cajaRapida;
    }

    public int[] getCajasNormales() {
        return cajasNormales;
    }

    public boolean isPrimerEjecucion() {
        return primerEjecucion;
    }

}
