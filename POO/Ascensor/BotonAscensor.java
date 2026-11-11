package Ascensor;

/** Botón de la cabina: al presionarlo agrega su piso a los destinos del ascensor. */
public class BotonAscensor extends Boton {
    private final int pisoDestino;
    private final Ascensor ascensor;

    public BotonAscensor(int pisoDestino, Ascensor ascensor) {
        this.pisoDestino = pisoDestino;
        this.ascensor = ascensor;
    }

    @Override
    public void presionar() {
        if (ascensor.getEstado() == EstadoAscensor.FUERA_DE_SERVICIO) {
            return;
        }
        System.out.println("   Boton de cabina " + pisoDestino + " (ascensor " + ascensor.getId() + ")");
        encenderIndicador();
        ascensor.agregarDestino(pisoDestino);
    }

    public int getPisoDestino() {
        return pisoDestino;
    }
}
