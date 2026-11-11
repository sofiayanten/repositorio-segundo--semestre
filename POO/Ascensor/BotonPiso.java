package Ascensor;

/** Botón de llamado en un piso (sube o baja). El aviso al control lo hace Piso.solicitar(). */
public class BotonPiso extends Boton {
    private final int numeroPiso;
    private final Direccion sentido;

    public BotonPiso(int numeroPiso, Direccion sentido) {
        this.numeroPiso = numeroPiso;
        this.sentido = sentido;
    }

    @Override
    public void presionar() {
        System.out.println("   Boton del piso " + numeroPiso + " (" + sentido + ") presionado");
        encenderIndicador();
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }

    public Direccion getSentido() {
        return sentido;
    }
}
