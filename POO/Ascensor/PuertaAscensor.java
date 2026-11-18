package Ascensor;

/** Puerta de la cabina. Tiene la opción de "mantener puertas abiertas". */
public class PuertaAscensor extends Puerta {
    private boolean mantenerAbierta;

    @Override
    public void abrir() {
        abierta = true;
    }

    /** Cierra solo si el sensor NO detecta un obstáculo. */
    @Override
    public void cerrar() {
        if (!getSensor().detectarObstaculo()) {
            abierta = false;
        }
    }

    /** Cierre automático: respeta el botón "mantener puertas abiertas". */
    public void cierreAutomatico() {
        if (!mantenerAbierta) {
            cerrar();
        }
    }

    public void mantenerAbiertas(boolean activo) {
        mantenerAbierta = activo;
    }
}