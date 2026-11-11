package Ascensor;

/** Puerta de la cabina: se cierra sola tras unos pasos, respeta el sensor y puede mantenerse abierta. */
public class PuertaAscensor extends Puerta {
    private static final int PASOS_DE_ESPERA = 2;

    private boolean mantenerAbierta;
    private int pasosAbierta;

    @Override
    public void abrir() {
        abierta = true;
        pasosAbierta = 0;
        System.out.println("   Puerta del ascensor: se abre");
    }

    @Override
    public void cerrar() {
        if (mantenerAbierta) {
            System.out.println("   Puerta del ascensor: se mantiene abierta (modo manual)");
            return;
        }
        if (getSensor().detectarObstaculo()) {
            System.out.println("   Sensor: hay un obstaculo, la puerta no se cierra");
            return;
        }
        abierta = false;
        pasosAbierta = 0;
        System.out.println("   Puerta del ascensor: se cierra");
    }

    /** Se llama en cada paso de la simulación: cuando se cumple la espera, intenta cerrar. */
    public void cierreAutomatico() {
        if (!abierta) {
            return;
        }
        pasosAbierta++;
        if (pasosAbierta >= PASOS_DE_ESPERA) {
            cerrar();
        }
    }

    public void mantenerAbiertas(boolean activo) {
        this.mantenerAbierta = activo;
        if (!activo) {
            pasosAbierta = 0;
        }
    }
}