package Ascensor;

/**
 * Sensor de obstáculos de una puerta.
 * Si detecta obstáculo varias lecturas seguidas, se considera que la puerta está bloqueada (falla).
 */
public class SensorPuerta {
    private static final int LECTURAS_PARA_FALLA = 3;

    private boolean obstaculo;
    private int lecturasConObstaculo;

    /** Simula que hay (o no) algo atravesado en la puerta. */
    public void setObstaculo(boolean obstaculo) {
        this.obstaculo = obstaculo;
    }

    /** Lee el sensor y cuenta cuántas lecturas seguidas han detectado obstáculo. */
    public boolean detectarObstaculo() {
        if (obstaculo) {
            lecturasConObstaculo++;
        } else {
            lecturasConObstaculo = 0;
        }
        return obstaculo;
    }

    /** Hay falla si el obstáculo se detectó en LECTURAS_PARA_FALLA lecturas seguidas. */
    public boolean verificarFalla() {
        return lecturasConObstaculo >= LECTURAS_PARA_FALLA;
    }
}
