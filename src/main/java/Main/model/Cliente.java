package Main.model;

/**
 * Clase que representa un cliente del banco
 *
 * @author handelenriquez
 */
public class Cliente {

    private final String nombre;
    private final String id;
    private final int edad;
    private boolean discapacidad;
    private boolean embarazada;
    private boolean empresarial;

    // Constructor principal (edad, nombre, id)
    public Cliente(String nombre, String id, int edad) {
        this.nombre = nombre;
        this.id = id;
        this.edad = edad;
        this.discapacidad = false;
        this.embarazada = false;
        this.empresarial = false;
    }

    // Constructor con condiciones preferenciales
    public Cliente(String nombre, String id, int edad, boolean discapacidad, boolean embarazada, boolean empresarial) {
        this.nombre = nombre;
        this.id = id;
        this.edad = edad;
        this.discapacidad = discapacidad;
        this.embarazada = embarazada;
        this.empresarial = empresarial;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public int getEdad() {
        return edad;
    }

    public boolean isDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(boolean discapacidad) {
        this.discapacidad = discapacidad;
    }

    public boolean isEmbarazada() {
        return embarazada;
    }

    public void setEmbarazada(boolean embarazada) {
        this.embarazada = embarazada;
    }

    public boolean isEmpresarial() {
        return empresarial;
    }

    public void setEmpresarial(boolean empresarial) {
        this.empresarial = empresarial;
    }
    
    // Método para determinar si el cliente es preferencial
    public boolean esPreferencial() {
        return edad >= 65 || discapacidad || embarazada || empresarial;
    }

    @Override
    public String toString() {
        return "Datos | Nombre: " + nombre + " | Id: " + id + " | Edad: " + edad;
    }
}
