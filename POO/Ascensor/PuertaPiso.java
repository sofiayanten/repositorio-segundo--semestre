package Ascensor;

/** Puerta del piso: se abre y se cierra sincronizada con la puerta del ascensor. */
public class PuertaPiso extends Puerta {
    private final int numeroPiso;

    public PuertaPiso(int numeroPiso) {
        this.numeroPiso = numeroPiso;
    }

    @Override
    public void abrir() {
        abierta = true;
        System.out.println("   Puerta del piso " + numeroPiso + ": se abre");
    }

    @Override
    public void cerrar() {
        abierta = false;
        System.out.println("   Puerta del piso " + numeroPiso + ": se cierra");
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }
}