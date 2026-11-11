package Ascensor;

/** Clase abstracta: toda puerta tiene un estado (abierta/cerrada) y un sensor. */
public abstract class Puerta {
    protected boolean abierta;
    private final SensorPuerta sensor = new SensorPuerta();   // composición

    public abstract void abrir();

    public abstract void cerrar();

    public boolean estaAbierta() {
        return abierta;
    }

    public SensorPuerta getSensor() {
        return sensor;
    }
}
