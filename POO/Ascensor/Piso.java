package Ascensor;

/** Un piso del edificio: tiene botón de subir, botón de bajar y su puerta. */
public class Piso {
    private final int ubicacion;
    private final BotonPiso botonSube;
    private final BotonPiso botonBaja;
    private final PuertaPiso puerta;
    private ControlAscensor control;

    public Piso(int ubicacion) {
        this.ubicacion = ubicacion;
        this.botonSube = new BotonPiso(ubicacion, Direccion.SUBE);
        this.botonBaja = new BotonPiso(ubicacion, Direccion.BAJA);
        this.puerta = new PuertaPiso(ubicacion);
    }

    /** Lo llama ControlAscensor al recibir los pisos. */
    public void setControl(ControlAscensor control) {
        this.control = control;
    }

    /** Una persona pide el ascensor en este piso. */
    public void solicitar(Direccion dir) {
        if (dir == Direccion.DETENIDO) {
            throw new IllegalArgumentException("El llamado debe ser SUBE o BAJA");
        }
        if (control == null) {
            throw new IllegalStateException("El piso " + ubicacion + " no esta conectado a un control");
        }
        BotonPiso boton = (dir == Direccion.SUBE) ? botonSube : botonBaja;
        if (boton.estaIluminado()) {
            return;   // el llamado ya fue registrado
        }
        boton.presionar();
        control.recibirSolicitud(ubicacion, dir);
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
