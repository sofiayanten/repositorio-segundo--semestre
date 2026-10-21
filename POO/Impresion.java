public class Impresion extends Producto {
    private String color;
    private Foto[] foto;

    public Impresion(int numero, String color, Foto[] foto) {
        super(numero);
        this.color = color;
        this.foto = foto;
    }

    public void imprimirFotos() {
        System.out.println("Impresión #" + numero + " (" + color + ")");
        for (Foto f : foto) {
            f.print();
        }
    }
}
