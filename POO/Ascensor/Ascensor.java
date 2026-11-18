package Ascensor;

import java.util.ArrayList;
import java.util.List;

/** Una cabina de ascensor: se mueve, decide dónde parar y maneja su puerta y sus botones. */
public class Ascensor {
    private final int id;
    private Estado estado = Estado.DETENIDO;
    private Direccion direccion = Direccion.NINGUNA;
    private int pisoActual = 0;
    private final List<Integer> destinos = new ArrayList<>();
    private final PuertaAscensor puerta = new PuertaAscensor();
    private final BotonAscensor[] botones;

    public Ascensor(int id, int totalPisos) {
        this.id = id;
        this.botones = new BotonAscensor[totalPisos];
        for (int i = 0; i < totalPisos; i++) {
            botones[i] = new BotonAscensor(i);
        }
    }

    /** Un pasajero presiona un botón dentro de la cabina. */
    public void presionarBoton(int piso) {
        if (estado == Estado.FALLA) {
            return;
        }
        agregarDestino(piso);
        botones[piso].presionar();
    }

    /** Agrega un piso a la lista de paradas pendientes (sin repetir). */
    public void agregarDestino(int piso) {
        if (piso < 0 || piso >= botones.length) {
            throw new IllegalArgumentException("Piso inexistente: " + piso);
        }
        if (!destinos.contains(piso)) {
            destinos.add(piso);
        }
    }

    /** Un "paso" del ascensor: o atiende un piso, o se mueve un piso. */
    public void mover() {
        if (estado == Estado.FALLA) {
            return;
        }
        if (destinos.isEmpty()) {
            estado = Estado.DETENIDO;
            direccion = Direccion.NINGUNA;
            return;
        }
        if (debeParar()) {
            atenderPiso();
            return;
        }
        if (!hayDestinosEnDireccion()) {
            cambiarDireccion(); // solo cambia cuando ya no hay pedidos en su sentido
        }
        if (direccion == Direccion.SUBE) {
            pisoActual++;
            estado = Estado.SUBIENDO;
        } else {
            pisoActual--;
            estado = Estado.BAJANDO;
        }
    }

    /** ¿El piso donde está es una de sus paradas? */
    public boolean debeParar() {
        return destinos.contains(pisoActual);
    }

    /** Llegó a una parada: la quita de la lista, apaga el botón y maneja la puerta. */
    public void atenderPiso() {
        destinos.remove(Integer.valueOf(pisoActual));
        botones[pisoActual].apagarIndicador();
        puerta.abrir();
        puerta.cierreAutomatico();
        estado = Estado.ATENDIENDO;
    }

    /** Invierte el sentido; si estaba quieto, elige el sentido hacia la primera parada. */
    public void cambiarDireccion() {
        if (direccion == Direccion.SUBE) {
            direccion = Direccion.BAJA;
        } else if (direccion == Direccion.BAJA) {
            direccion = Direccion.SUBE;
        } else {
            direccion = destinos.get(0) > pisoActual ? Direccion.SUBE : Direccion.BAJA;
        }
    }

    /** ¿Quedan paradas más adelante en el sentido actual? */
    private boolean hayDestinosEnDireccion() {
        for (int d : destinos) {
            if (direccion == Direccion.SUBE && d > pisoActual) {
                return true;
            }
            if (direccion == Direccion.BAJA && d < pisoActual) {
                return true;
            }
        }
        return false;
    }

    /** Emergencia: se detiene, se cancelan los pedidos y se abre la puerta. */
    public void detenerPorFalla() {
        estado = Estado.FALLA;
        direccion = Direccion.NINGUNA;
        destinos.clear();
        puerta.abrir();
    }

    /** Botón "mantener puertas abiertas" de la cabina. */
    public void mantenerPuertas(boolean activo) {
        puerta.mantenerAbiertas(activo);
        if (activo) {
            puerta.abrir();
        } else {
            puerta.cierreAutomatico();
        }
    }

    public boolean estaEnFalla() {
        return estado == Estado.FALLA;
    }

    public int getId() {
        return id;
    }

    public Estado getEstado() {
        return estado;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public int getPisoActual() {
        return pisoActual;
    }

    public List<Integer> getDestinos() {
        return destinos;
    }

    public PuertaAscensor getPuerta() {
        return puerta;
    }
}