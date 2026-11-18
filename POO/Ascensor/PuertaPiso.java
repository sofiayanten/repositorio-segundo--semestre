package Ascensor;

/** Puerta de un piso (la que está en el pasillo). */
public class PuertaPiso extends Puerta {
    private final int numeroPiso;

    public PuertaPiso(int numeroPiso) {
        this.numeroPiso = numeroPiso;
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }

    @Override
    public void abrir() {
        abierta = true;
    }

    /** Cierra solo si no hay obstáculo. */
    @Override
    public void cerrar() {
        if (!getSensor().detectarObstaculo()) {
            abierta = false;
        }
    }
}