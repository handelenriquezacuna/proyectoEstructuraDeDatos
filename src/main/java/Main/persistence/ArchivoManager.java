package Main.persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada del manejo de archivos para persistencia
 */
public class ArchivoManager {

    /**
     * Verifica si un archivo existe y no está vacío
     */
    public boolean existeArchivo(String rutaArchivo) {
        try {
            Path path = Paths.get(rutaArchivo);
            return Files.exists(path) && Files.size(path) > 0;
        } catch (Exception e) {
            System.err.println("Error verificando archivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea los directorios necesarios si no existen
     */
    public void crearDirectorios(String rutaArchivo) {
        try {
            Path path = Paths.get(rutaArchivo);
            Path directorio = path.getParent();
            if (directorio != null && !Files.exists(directorio)) {
                Files.createDirectories(directorio);
                System.out.println("Directorio creado: " + directorio);
            }
        } catch (Exception e) {
            System.err.println("Error creando directorios: " + e.getMessage());
        }
    }

    /**
     * Lee configuración desde archivo con manejo seguro de errores
     */
    public Map<String, String> leerConfiguracion(String rutaArchivo) {
        Map<String, String> config = new HashMap<>();

        if (!existeArchivo(rutaArchivo)) {
            System.out.println("Archivo de configuración no encontrado: " + rutaArchivo);
            return config;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                // Ignorar líneas vacías y comentarios
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // Procesar líneas con formato clave=valor
                if (linea.contains("=")) {
                    String[] partes = linea.split("=", 2);
                    if (partes.length == 2) {
                        String clave = partes[0].trim();
                        String valor = partes[1].trim();

                        if (!clave.isEmpty() && !valor.isEmpty()) {
                            config.put(clave, valor);
                        }
                    }
                } else {
                    System.out.println("Linea " + numeroLinea + " ignorada (formato invalido): " + linea);
                }
            }

            System.out.println("Configuracion leida: " + config.size() + " parametros");

        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado leyendo configuración: " + e.getMessage());
        }

        return config;
    }

    /**
     * Guarda configuración en archivo con manejo completo de errores
     */
    public void guardarConfiguracion(String rutaArchivo, Map<String, String> config) {
        try {
            // Crear directorios si no existen
            crearDirectorios(rutaArchivo);

            // Escribir configuración
            try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
                // Escribir encabezado
                writer.println("# Configuración del Sistema Bancario");
                writer.println("# Generado automáticamente - No editar manualmente");
                writer.println("# Fecha: " + java.time.LocalDateTime.now());
                writer.println();

                // Escribir configuración
                for (Map.Entry<String, String> entry : config.entrySet()) {
                    writer.println(entry.getKey() + "=" + entry.getValue());
                }

                writer.flush();
                System.out.println("Configuracion guardada en: " + rutaArchivo);

            } catch (IOException e) {
                System.err.println("Error escribiendo archivo: " + e.getMessage());
                throw e;
            }

        } catch (Exception e) {
            System.err.println("Error guardando configuración: " + e.getMessage());
            // No lanzar la excepción para evitar crashes del sistema
        }
    }

    /**
     * Elimina un archivo si existe
     */
    public boolean eliminarArchivo(String rutaArchivo) {
        try {
            Path path = Paths.get(rutaArchivo);
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("Archivo eliminado: " + rutaArchivo);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error eliminando archivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lee el contenido completo de un archivo de texto
     */
    public String leerArchivo(String rutaArchivo) {
        StringBuilder contenido = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error leyendo archivo: " + e.getMessage());
        }

        return contenido.toString();
    }

    /**
     * Escribe contenido en un archivo
     */
    public void escribirArchivo(String rutaArchivo, String contenido) {
        try {
            crearDirectorios(rutaArchivo);

            try (FileWriter writer = new FileWriter(rutaArchivo)) {
                writer.write(contenido);
                System.out.println("Contenido escrito en: " + rutaArchivo);
            }
        } catch (Exception e) {
            System.err.println("Error escribiendo archivo: " + e.getMessage());
        }
    }
}