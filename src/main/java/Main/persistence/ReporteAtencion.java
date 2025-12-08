/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Main.persistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
/**
 * @author jomas
 * Clase para generar reportes del registro historico
 */

public class ReporteAtencion {

    private final ArchivoManager archivoManager = new ArchivoManager();
    private final String RUTA_REGISTRO = "src/main/resources/data/registroTransacciones.txt";

    public void generarReporte() {

        List<String> lineas;
        try {
            String contenido = archivoManager.leerArchivo(RUTA_REGISTRO);
            if (contenido.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay transacciones registradas.");
                return;
            }
            lineas = Arrays.asList(contenido.split("\n"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error leyendo registro.");
            return;
        }

        Map<Integer, Integer> atendidosPorCaja = new HashMap<>();
        Map<Integer, Long> tiempoTotalPorCaja = new HashMap<>();

        int totalClientes = 0;
        long tiempoGeneral = 0;

        for (String linea : lineas) {
            if (!linea.contains(";")) continue;

            String[] p = linea.split(";");

            int caja = Integer.parseInt(p[0]);
            long tiempo = Long.parseLong(p[5]);

            totalClientes++;
            tiempoGeneral += tiempo;

            atendidosPorCaja.put(caja, atendidosPorCaja.getOrDefault(caja, 0) + 1);
            tiempoTotalPorCaja.put(caja, tiempoTotalPorCaja.getOrDefault(caja, 0L) + tiempo);
        }

        // Caja con más clientes
        int mayorCaja = atendidosPorCaja.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();

        // Caja con mejor tiempo promedio
        double mejorPromedio = Double.MAX_VALUE;
        int mejorCaja = -1;

        for (int c : atendidosPorCaja.keySet()) {
            double prom = tiempoTotalPorCaja.get(c) / (double) atendidosPorCaja.get(c);
            if (prom < mejorPromedio) {
                mejorPromedio = prom;
                mejorCaja = c;
            }
        }

        double promedioGeneral = tiempoGeneral / (double) totalClientes;

        String contenidoReporte =
                "REPORTE DE ATENCIONES\n\n"
                + "1. Caja que más clientes atendió: " + mayorCaja + "\n"
                + "2. Total clientes atendidos: " + totalClientes + "\n"
                + "3. Caja con mejor tiempo promedio: " + mejorCaja + "\n"
                + "4. Tiempo promedio general: " + promedioGeneral + " ms\n";

        // Guardar archivo
        String archivoGenerado = archivoManager.generarReporte(contenidoReporte);

        JOptionPane.showMessageDialog(null,
                "Reporte generado exitosamente:\n" + archivoGenerado);
        JOptionPane.showMessageDialog(null, contenidoReporte);
    }
}
