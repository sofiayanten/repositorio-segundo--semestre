package Ascensor;

/** Clase abstracta: lo común a las puertas de ascensor y de piso. */
public abstract class Puerta {
    protected boolean abierta;
    private final SensorPuerta sensor = new SensorPuerta(); // composición: la puerta "tiene" su sensor

    public abstract void abrir();

    public abstract void cerrar();

    public boolean estaAbierta() {
        return abierta;
    }

    public SensorPuerta getSensor() {
        return sensor;
    }
}
