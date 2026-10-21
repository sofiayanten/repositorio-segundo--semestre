class Pedido {
    private Cliente cliente;
    private Producto[] producto;
    private String fecha;
    private int numeroTarjetaCredito;

    public Pedido(Cliente cliente, Producto[] producto, String fecha, int numeroTarjetaCredito) {
        this.cliente = cliente;
        this.producto = producto;
        this.fecha = fecha;
        this.numeroTarjetaCredito = numeroTarjetaCredito;
    }

    public void mostrarPedido() {
        System.out.println("Cliente: " + cliente.getNombre() + " (CC " + cliente.getCedula() + ")");
        System.out.println("Fecha: " + fecha);
        System.out.println("Tarjeta: " + numeroTarjetaCredito);
        System.out.println("Productos:");
        for (Producto p : producto) {
            System.out.println(" - Producto #" + p.getNumero() + " (" + p.getClass().getSimpleName() + ")");
        }
    }
}
