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
    
    // Getters y Setters


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Determina si el cliente es preferencial por edad
     */
    public boolean esPreferencial() {
        return edad >= 65; // Adultos mayores
    }
    
    @Override
    public String toString() {
        return String.format("Cliente{nombre='%s', id='%s', edad=%d}", 
                           nombre, id, edad);
    }
}
