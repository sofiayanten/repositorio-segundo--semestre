package Ascensor;

/** Botón dentro de la cabina: selecciona el piso de destino. */
public class BotonAscensor extends Boton {
    private final int pisoDestino;

    public BotonAscensor(int pisoDestino) {
        this.pisoDestino = pisoDestino;
    }

    public int getPisoDestino() {
        return pisoDestino;
    }

    @Override
    public void presionar() {
        System.out.println(" Boton interno: ir al piso " + pisoDestino);
        encenderIndicador();
    }
}