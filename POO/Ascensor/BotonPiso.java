package Ascensor;

/** Botón de un piso: pide el ascensor para subir o para bajar. */
public class BotonPiso extends Boton {
    private final int numeroPiso;
    private final Direccion sentido;

    public BotonPiso(int numeroPiso, Direccion sentido) {
        this.numeroPiso = numeroPiso;
        this.sentido = sentido;
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }

    public Direccion getSentido() {
        return sentido;
    }

    @Override
    public void presionar() {
        System.out.println(" Boton del piso " + numeroPiso + ": llamar para " + sentido);
        encenderIndicador();
    }
}