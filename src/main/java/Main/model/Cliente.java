package Main.model;

/**
 * Clase que representa un cliente del banco
 */
public class Cliente {

    private String nombre;
    private String id;
    private int edad;

    public Cliente(String nombre, String id, int edad) {
        this.nombre = nombre;
        this.id = id;
        this.edad = edad;
    }

    public boolean esPreferencial() {
        return edad >= 65;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return nombre + " | ID: " + id + " | Edad: " + edad + (esPreferencial() ? " (Preferencial)" : "");
    }
}
