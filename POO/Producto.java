public abstract class Producto {

    // 1. ATRIBUTOS: los que TODOS los productos tienen
    private int numero;
    private double precio;

    // 2. CONSTRUCTOR
    public Producto(int numero, double precio) {
        this.numero = numero;
        this.precio = precio;
    }

    // 3. GETTERS Y SETTERS
    public int getNumero() {
        return numero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio >= 0) {
            this.precio = precio;
        } else {
            System.out.println("El precio no puede ser negativo");
        }
    }

    // 4. toString
    @Override
    public String toString() {
        return "numero: " + numero + ", precio: " + precio;
    }
}