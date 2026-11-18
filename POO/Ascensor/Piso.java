package Ascensor;

/** Un piso del edificio: tiene botón de subir, botón de bajar y su puerta. */
public class Piso {
    private final int ubicacion;
    private final BotonPiso botonSube;
    private final BotonPiso botonBaja;
    private final PuertaPiso puerta;

    public Piso(int ubicacion) {
        this.ubicacion = ubicacion;
        this.botonSube = new BotonPiso(ubicacion, Direccion.SUBE);
        this.botonBaja = new BotonPiso(ubicacion, Direccion.BAJA);
        this.puerta = new PuertaPiso(ubicacion);
    }

    /** Alguien pidió el ascensor desde este piso. */
    public void solicitar(Direccion dir) {
        if (dir == Direccion.SUBE) {
            botonSube.presionar();
        } else {
            botonBaja.presionar();
        }
    }

    /** El ascensor llegó: se apagan las luces y la puerta se abre y se cierra. */
    public void llegoAscensor() {
        puerta.abrir();
        botonSube.apagarIndicador();
        botonBaja.apagarIndicador();
        puerta.cerrar();
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public BotonPiso getBotonSube() {
        return botonSube;
    }

    public BotonPiso getBotonBaja() {
        return botonBaja;
    }

    public PuertaPiso getPuerta() {
        return puerta;
    }
}