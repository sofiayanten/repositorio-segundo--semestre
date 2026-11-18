package Ascensor;

/** Sensor de seguridad de una puerta: detecta obstáculos y fallas. */
public class SensorPuerta {
    private boolean obstaculo;
    private boolean falla; // agregado al diagrama para poder simular fallas

    public boolean detectarObstaculo() {
        return obstaculo;
    }

    public boolean verificarFalla() {
        return falla;
    }

    // Métodos para simular el hardware (en un sistema real vendrían del sensor físico)
    public void setObstaculo(boolean valor) {
        obstaculo = valor;
    }

    public void setFalla(boolean valor) {
        falla = valor;
    }
}