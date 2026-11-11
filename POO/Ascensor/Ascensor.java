package Ascensor;

import java.util.ArrayList;
import java.util.List;

/** Un ascensor. Se mueve con la estrategia LOOK: sigue en su sentido mientras haya destinos adelante. */
public class Ascensor {
    private final int id;
    private final int numeroPisos;
    private EstadoAscensor estado = EstadoAscensor.DETENIDO;
    private Direccion direccion = Direccion.DETENIDO;
    private int pisoActual = 1;
    private final List<Integer> destinos = new ArrayList<>();
    private final PuertaAscensor puerta = new PuertaAscensor();
    private final List<BotonAscensor> botones = new ArrayList<>();

    public Ascensor(int id, int numeroPisos) {
        this.id = id;
        this.numeroPisos = numeroPisos;
        for (int piso = 1; piso <= numeroPisos; piso++) {
            botones.add(new BotonAscensor(piso, this));
        }
    }

    /** Avanza UN piso en el sentido actual (solo si la puerta está cerrada). */
    public void mover() {
        if (estado == EstadoAscensor.FUERA_DE_SERVICIO || puerta.estaAbierta() || direccion == Direccion.DETENIDO) {
            return;
        }
        int siguiente = pisoActual + (direccion == Direccion.SUBE ? 1 : -1);
        if (siguiente < 1 || siguiente > numeroPisos) {
            return;
        }
        pisoActual = siguiente;
        estado = EstadoAscensor.EN_MOVIMIENTO;
        System.out.println("   Ascensor " + id + " " + direccion + " -> piso " + pisoActual);
    }

    /** Debe parar si el piso actual está entre sus destinos. */
    public boolean debeParar() {
        return destinos.contains(pisoActual);
    }

    /** Llegó a un destino: lo quita de la lista, apaga el botón de cabina y abre la puerta. */
    public void atenderPiso() {
        destinos.remove(Integer.valueOf(pisoActual));
        botones.get(pisoActual - 1).apagarIndicador();
        estado = EstadoAscensor.PUERTAS_ABIERTAS;
        System.out.println("   Ascensor " + id + " llega al piso " + pisoActual);
        puerta.abrir();
    }

    /** LOOK: solo invierte el sentido cuando ya no quedan destinos adelante. */
    public void cambiarDireccion() {
        boolean hayArriba = hayDestinosArriba();
        boolean hayAbajo = hayDestinosAbajo();
        if (direccion == Direccion.SUBE) {
            if (!hayArriba) {
                direccion = hayAbajo ? Direccion.BAJA : Direccion.DETENIDO;
            }
        } else if (direccion == Direccion.BAJA) {
            if (!hayAbajo) {
                direccion = hayArriba ? Direccion.SUBE : Direccion.DETENIDO;
            }
        } else {
            direccion = hayArriba ? Direccion.SUBE : (hayAbajo ? Direccion.BAJA : Direccion.DETENIDO);
        }
    }

    // ---- Métodos de apoyo (no aparecen en el diagrama) ----

    public void agregarDestino(int piso) {
        if (piso < 1 || piso > numeroPisos) {
            throw new IllegalArgumentException("Piso fuera de rango: " + piso);
        }
        if (!destinos.contains(piso)) {
            destinos.add(piso);
        }
    }

    /** Un pasajero dentro de la cabina presiona el botón de un piso. */
    public void presionarBoton(int piso) {
        if (piso < 1 || piso > numeroPisos) {
            throw new IllegalArgumentException("Piso fuera de rango: " + piso);
        }
        botones.get(piso - 1).presionar();
    }

    private boolean hayDestinosArriba() {
        for (int d : destinos) {
            if (d > pisoActual) {
                return true;
            }
        }
        return false;
    }

    private boolean hayDestinosAbajo() {
        for (int d : destinos) {
            if (d < pisoActual) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public int getPisoActual() {
        return pisoActual;
    }

    public EstadoAscensor getEstado() {
        return estado;
    }

    public void setEstado(EstadoAscensor estado) {
        this.estado = estado;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public List<Integer> getDestinos() {
        return List.copyOf(destinos);
    }

    public PuertaAscensor getPuerta() {
        return puerta;
    }

    public List<BotonAscensor> getBotones() {
        return List.copyOf(botones);
    }

    @Override
    public String toString() {
        return "Ascensor " + id + " [piso " + pisoActual + ", " + direccion + ", " + estado
                + ", destinos=" + destinos + "]";
    }
}
