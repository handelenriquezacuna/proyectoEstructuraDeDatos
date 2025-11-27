package Main.servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para consultar el tipo de cambio del BCCR mediante webscraping
 * @author handelenriquez
 */
public class ServicioTipoCambio {

    // URL de la página de tipo de cambio de ventanilla del BCCR
    private static final String URL_TC_VENTANILLA = "https://gee.bccr.fi.cr/IndicadoresEconomicos/Cuadros/frmConsultaTCVentanilla.aspx";

    /**
     * Obtiene el tipo de cambio del día desde el BCCR mediante webscraping
     * Retorna un String con la información formateada
     */
    public String obtenerTipoCambio() {
        try {
            // Realizar webscraping de la página del BCCR
            String htmlCompleto = obtenerHTMLPagina(URL_TC_VENTANILLA);

            if (htmlCompleto != null && !htmlCompleto.isEmpty()) {
                // Extraer tipo de cambio promedio de la tabla
                TipoCambioInfo info = extraerTipoCambioDeHTML(htmlCompleto);

                if (info != null && info.compra != null && info.venta != null) {
                    return formatearTipoCambioBCCR(info);
                } else {
                    return obtenerTipoCambioSimulado();
                }
            } else {
                return obtenerTipoCambioSimulado();
            }

        } catch (Exception e) {
            System.err.println("Error consultando tipo de cambio: " + e.getMessage());
            return obtenerTipoCambioSimulado();
        }
    }

    /**
     * Clase interna para almacenar información del tipo de cambio
     */
    private static class TipoCambioInfo {
        String compra;
        String venta;
        String entidad;

        TipoCambioInfo(String compra, String venta, String entidad) {
            this.compra = compra;
            this.venta = venta;
            this.entidad = entidad;
        }
    }

    /**
     * Obtiene el HTML completo de una página usando GET
     */
    private String obtenerHTMLPagina(String urlString) {
        HttpURLConnection conexion = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setConnectTimeout(10000);
            conexion.setReadTimeout(10000);
            conexion.setRequestProperty("User-Agent", "Mozilla/5.0");
            conexion.setRequestProperty("Accept", "text/html");

            int codigoRespuesta = conexion.getResponseCode();

            if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conexion.getInputStream(), "UTF-8"));
                return leerRespuestaRecursivo(reader, new StringBuilder()).toString();
            } else {
                System.err.println("Error HTTP: " + codigoRespuesta);
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error obteniendo página: " + e.getMessage());
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
                if (conexion != null) conexion.disconnect();
            } catch (Exception e) {
                // Ignorar errores al cerrar
            }
        }
    }

    /**
     * Extrae el tipo de cambio del HTML usando recursión
     * Busca la primera fila de datos en la tabla
     */
    private TipoCambioInfo extraerTipoCambioDeHTML(String html) {
        try {
            // Buscar todas las filas <tr> de la tabla
            return buscarPrimeraFilaDatosRecursivo(html, 0);

        } catch (Exception e) {
            System.err.println("Error leyendo HTML: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca recursivamente la primera fila con datos de tipo de cambio
     */
    private TipoCambioInfo buscarPrimeraFilaDatosRecursivo(String html, int posicionInicio) {
        // Buscar el siguiente <tr>
        int inicioTr = html.indexOf("<tr", posicionInicio);
        if (inicioTr == -1) {
            return null; // No hay más filas
        }

        int finTr = html.indexOf("</tr>", inicioTr);
        if (finTr == -1) {
            return null;
        }

        String fila = html.substring(inicioTr, finTr + 5);

        // Extraer todas las celdas <td> de esta fila
        String[] celdas = extraerCeldasDeFila(fila);

        // Verificar si esta fila tiene datos válidos (mínimo 4 columnas: entidad, nombre, compra, venta)
        if (celdas.length >= 4) {
            String compra = limpiarTexto(celdas[2] );
            String venta = limpiarTexto(celdas[3]);

            // Validar que sean números
            if (esNumeroValido(compra) && esNumeroValido(venta)) {
                String entidad = limpiarTexto(celdas[1]);
                return new TipoCambioInfo(compra, venta, entidad);
            }
        }

        // Recursión: buscar en la siguiente fila
        return buscarPrimeraFilaDatosRecursivo(html, finTr + 5);
    }

    /**
     * Extrae todas las celdas <td> de una fila usando recursión
     */
    private String[] extraerCeldasDeFila(String fila) {
        // Contar cuántas celdas hay
        int numCeldas = contarCeldasRecursivo(fila, 0, 0);

        if (numCeldas == 0) {
            return new String[0];
        }

        String[] celdas = new String[numCeldas];
        extraerCeldasRecursivo(fila, 0, celdas, 0);

        return celdas;
    }

    /**
     * Cuenta el número de celdas <td> recursivamente
     */
    private int contarCeldasRecursivo(String fila, int posicion, int contador) {
        int inicio = fila.indexOf("<td", posicion);
        if (inicio == -1) {
            return contador; // Caso base: no hay más celdas
        }

        int fin = fila.indexOf("</td>", inicio);
        if (fin == -1) {
            return contador;
        }

        return contarCeldasRecursivo(fila, fin + 5, contador + 1); // Recursión
    }

    /**
     * Extrae el contenido de las celdas recursivamente
     */
    private void extraerCeldasRecursivo(String fila, int posicion, String[] celdas, int indice) {
        if (indice >= celdas.length) {
            return; // Caso base: todas las celdas extraídas
        }

        int inicioTd = fila.indexOf("<td", posicion);
        if (inicioTd == -1) {
            return;
        }

        int inicioContenido = fila.indexOf(">", inicioTd) + 1;
        int finTd = fila.indexOf("</td>", inicioContenido);

        if (finTd == -1) {
            return;
        }

        String contenido = fila.substring(inicioContenido, finTd);
        celdas[indice] = contenido;

        // Recursión para la siguiente celda
        extraerCeldasRecursivo(fila, finTd + 5, celdas, indice + 1);
    }

    /**
     * Limpia el texto HTML (elimina tags y espacios)
     */
    private String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        // Eliminar tags HTML recursivamente
        texto = eliminarTagsRecursivo(texto);

        // Limpiar espacios
        return texto.trim().replaceAll("\\s+", " ");
    }

    /**
     * Elimina tags HTML de forma recursiva
     */
    private String eliminarTagsRecursivo(String texto) {
        int inicio = texto.indexOf("<");
        if (inicio == -1) {
            return texto; // Caso base: no hay más tags
        }

        int fin = texto.indexOf(">", inicio);
        if (fin == -1) {
            return texto;
        }

        // Eliminar el tag y continuar recursivamente
        String nuevoTexto = texto.substring(0, inicio) + texto.substring(fin + 1);
        return eliminarTagsRecursivo(nuevoTexto);
    }

    /**
     * Verifica si un string es un número válido
     */
    private boolean esNumeroValido(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }

        try {
            // Remover comas si las hay
            String numero = texto.replace(",", ".");
            Double.parseDouble(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Lee la respuesta del BufferedReader de forma recursiva
     */
    private StringBuilder leerRespuestaRecursivo(BufferedReader reader, StringBuilder acumulador) throws Exception {
        String linea = reader.readLine();
        if (linea == null) {
            return acumulador; // Caso base: fin del stream
        }
        acumulador.append(linea).append("\n");
        return leerRespuestaRecursivo(reader, acumulador); // Recursión
    }

    /**
     * Formatea el tipo de cambio del BCCR para mostrar
     */
    private String formatearTipoCambioBCCR(TipoCambioInfo info) {
        try {
            // Normalizar números (reemplazar comas por puntos)
            String compraStr = info.compra.replace(",", ".");
            String ventaStr = info.venta.replace(",", ".");

            double valorCompra = Double.parseDouble(compraStr);
            double valorVenta = Double.parseDouble(ventaStr);

            return String.format(
                "=== TIPO DE CAMBIO DEL DÍA ===\n\n" +
                "Fuente: Banco Central de Costa Rica (BCCR)\n" +
                "Entidad: %s\n" +
                "Fecha: %s\n\n" +
                "Dólar (USD) a Colones (CRC):\n" +
                "• Compra: ₡%.2f\n" +
                "• Venta:  ₡%.2f\n\n" +
                "Diferencial: ₡%.2f\n\n" +
                "Nota: Tipo de cambio de ventanilla",
                info.entidad,
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                valorCompra,
                valorVenta,
                valorVenta - valorCompra
            );
        } catch (Exception e) {
            System.err.println("Error formateando: " + e.getMessage());
            return "Error formateando tipo de cambio";
        }
    }

    /**
     * Método alternativo: simulación de tipo de cambio si no hay conexión
     * (útil para desarrollo/testing o cuando falla el webscraping)
     */
    public String obtenerTipoCambioSimulado() {
        return String.format(
            "=== TIPO DE CAMBIO DEL DÍA (SIMULADO) ===\n\n" +
            "⚠ MODO SIMULACIÓN\n" +
            "No se pudo obtener datos del BCCR\n\n" +
            "Fuente: Valores simulados\n" +
            "Fecha: %s\n\n" +
            "Dólar (USD) a Colones (CRC):\n" +
            "• Compra: ₡535.50\n" +
            "• Venta:  ₡540.75\n\n" +
            "Diferencial: ₡5.25\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Nota: Estos valores son referenciales.\n" +
            "Para consultar el tipo de cambio oficial:\n" +
            "https://gee.bccr.fi.cr/",
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }
}
