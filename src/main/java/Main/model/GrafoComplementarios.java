/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Main.model;

import java.io.BufferedReader;
import java.io.FileReader;

/*
* Grafo para los servicios complementarios
* Lo hicimos con recursion porque era requisito del proyecto
* Un while hubiera sido mas sencillo pero bueno
* @author handelenriquez
*/
public class GrafoComplementarios {

    // cada nodo tiene un servicio y sus complementarios
    private class NodoGrafo {
        String servicio;
        ListaComplementarios complementarios;
        NodoGrafo siguiente;

        NodoGrafo(String servicio) {
            this.servicio = servicio;
            this.complementarios = new ListaComplementarios();
            this.siguiente = null;
        }
    }

    // lista para guardar los complementarios de cada servicio
    private class ListaComplementarios {
        private NodoComplementario primero;

        private class NodoComplementario {
            String complementario;
            NodoComplementario siguiente;

            NodoComplementario(String complementario) {
                this.complementario = complementario;
                this.siguiente = null;
            }
        }

        void agregar(String complementario) {
            agregarRecursivo(primero, complementario);
        }

        private NodoComplementario agregarRecursivo(NodoComplementario nodo, String complementario) {
            if (nodo == null) {
                NodoComplementario nuevo = new NodoComplementario(complementario);
                if (primero == null) {
                    primero = nuevo;
                }
                return nuevo;
            }
            if (nodo.siguiente == null) {
                nodo.siguiente = new NodoComplementario(complementario);
            } else {
                agregarRecursivo(nodo.siguiente, complementario);
            }
            return nodo;
        }

        String obtenerTodos() {
            return obtenerTodosRecursivo(primero, new StringBuilder()).toString();
        }

        private StringBuilder obtenerTodosRecursivo(NodoComplementario nodo, StringBuilder resultado) {
            if (nodo == null) {
                return resultado;
            }
            if (resultado.length() > 0) {
                resultado.append(", ");
            }
            resultado.append(nodo.complementario);
            return obtenerTodosRecursivo(nodo.siguiente, resultado);
        }

        boolean estaVacia() {
            return primero == null;
        }
    }
    // el primer nodo del grafo
    private NodoGrafo cabeza;
    private static final String ARCHIVO_COMPLEMENTARIOS = "src/main/resources/data/complementarios.txt";

    public GrafoComplementarios() {
        this.cabeza = null;
        cargarDesdeArchivo();
    }

    // carga los datos del archivo
    private void cargarDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_COMPLEMENTARIOS))) {
            cargarLineasRecursivo(reader);
            System.out.println("Grafo de complementarios cargado exitosamente");
        } catch (Exception e) {
            System.err.println("Error cargando complementarios: " + e.getMessage());
        }
    }

    // lee linea por linea del archivo (recursivo por requisito)
    private void cargarLineasRecursivo(BufferedReader reader) throws Exception {
        String linea = reader.readLine();
        if (linea == null) {
            return; // Caso base: fin del archivo
        }

        linea = linea.trim();
        // Ignorar líneas vacías y comentarios
        if (!linea.isEmpty() && !linea.startsWith("#")) {
            procesarLinea(linea);
        }

        // Llamada recursiva para la siguiente línea
        cargarLineasRecursivo(reader);
    }

    // procesa cada linea del archivo
    // formato: Depositos->Seguros
    private void procesarLinea(String linea) {
        if (linea.contains("->")) {
            String[] partes = linea.split("->");
            if (partes.length == 2) {
                String servicio = partes[0].trim();
                String complementario = partes[1].trim();
                agregarRelacion(servicio, complementario);
            }
        }
    }

    // agrega la relacion al grafo
    private void agregarRelacion(String servicio, String complementario) {
        NodoGrafo nodo = buscarOCrearNodo(servicio);
        nodo.complementarios.agregar(complementario);
    }

    // busca el nodo o lo crea si no existe
    private NodoGrafo buscarOCrearNodo(String servicio) {
        if (cabeza == null) {
            cabeza = new NodoGrafo(servicio);
            return cabeza;
        }
        return buscarOCrearNodoRecursivo(cabeza, servicio, null);
    }

    private NodoGrafo buscarOCrearNodoRecursivo(NodoGrafo actual, String servicio, NodoGrafo anterior) {
        // Caso base: encontramos el nodo
        if (actual != null && actual.servicio.equalsIgnoreCase(servicio)) {
            return actual;
        }

        // Caso base: llegamos al final, crear nuevo nodo
        if (actual == null) {
            NodoGrafo nuevo = new NodoGrafo(servicio);
            if (anterior != null) {
                anterior.siguiente = nuevo;
            }
            return nuevo;
        }

        // Llamada recursiva
        return buscarOCrearNodoRecursivo(actual.siguiente, servicio, actual);
    }

    // devuelve los complementarios de un servicio
    public String obtenerComplementarios(String servicio) {
        NodoGrafo nodo = buscarNodoRecursivo(cabeza, servicio);
        if (nodo != null && !nodo.complementarios.estaVacia()) {
            return nodo.complementarios.obtenerTodos();
        }
        return null;
    }

    // busca un nodo por su nombre
    private NodoGrafo buscarNodoRecursivo(NodoGrafo actual, String servicio) {
        if (actual == null) {
            return null; // Caso base: no encontrado
        }
        if (actual.servicio.equalsIgnoreCase(servicio)) {
            return actual; // Caso base: encontrado
        }
        return buscarNodoRecursivo(actual.siguiente, servicio); // Recursión
    }

    // para ver todo el grafo (debugging)
    public String mostrarGrafo() {
        StringBuilder sb = new StringBuilder("GRAFO DE COMPLEMENTARIOS\n");
        mostrarGrafoRecursivo(cabeza, sb);
        return sb.toString();
    }

    private void mostrarGrafoRecursivo(NodoGrafo nodo, StringBuilder sb) {
        if (nodo == null) {
            return; // Caso base
        }
        sb.append(nodo.servicio).append(" -> ").append(nodo.complementarios.obtenerTodos()).append("\n");
        mostrarGrafoRecursivo(nodo.siguiente, sb); // Recursión
    }
}
