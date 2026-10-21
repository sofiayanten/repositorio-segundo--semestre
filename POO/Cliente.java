public class Cliente {

    // 1. ATRIBUTOS
    private String cedula;
    private String nombre;

    // 2. CONSTRUCTOR
    public Cliente(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
    }

    // 3. GETTERS Y SETTERS (sin setCedula: la cédula no cambia)
    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // 4. toString
    @Override
    public String toString() {
        return "Cliente [cedula: " + cedula + ", nombre: " + nombre + "]";
    }
}