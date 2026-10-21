public class Main {
    public static void main(String[] args) {
        Foto f1 = new Foto("playa.jpg");
        Foto f2 = new Foto("familia.png");

        Impresion impresion = new Impresion(1, "Color", new Foto[]{f1, f2});
        Camara camara = new Camara(2, "Canon", "EOS R50");

        Cliente cliente = new Cliente("123456789", "Juan Pérez");

        Pedido pedido = new Pedido(cliente,
                new Producto[]{impresion, camara},
                new String(), 1234567);

        pedido.mostrarPedido();
        impresion.imprimirFotos();
    }
}