package Ascensor;

import java.util.ArrayList;
import java.util.List;

/** Cerebro del sistema: recibe llamados, elige qué ascensor responde y avanza la simulación paso a paso. */
public class ControlAscensor {
    private final List<Ascensor> ascensores;
    private final List<Piso> pisos;
    private final ModuloAlertas alertas = new ModuloAlertas();

    /** Los pisos deben venir ordenados: 1, 2, 3... */
    public ControlAscensor(List<Ascensor> ascensores, List<Piso> pisos) {
        this.ascensores = new ArrayList<>(ascensores);
        this.pisos = new ArrayList<>(pisos);
        for (int i = 0; i < this.pisos.size(); i++) {
            if (this.pisos.get(i).getUbicacion() != i + 1) {
                throw new IllegalArgumentException("Los pisos deben estar ordenados desde el 1");
            }
            this.pisos.get(i).setControl(this);
        }
    }

    /** Llega un llamado desde un piso: se asigna a un ascensor. */
    public void recibirSolicitud(int piso, Direccion dir) {
        Ascensor elegido = asignarAscensor(piso, dir);
        if (elegido == null) {
            alertas.reportarFalla("No hay ascensores disponibles para el llamado del piso " + piso);
            return;
        }
        elegido.agregarDestino(piso);
        alertas.registrarActividad("Llamado piso " + piso + " (" + dir + ") asignado al ascensor " + elegido.getId());
    }

    /**
     * Elige el ascensor de menor "costo": distancia al piso + destinos pendientes,
     * con penalización si se está alejando del piso o va en sentido contrario al llamado.
     */
    public Ascensor asignarAscensor(int piso, Direccion dir) {
        Ascensor mejor = null;
        int mejorCosto = Integer.MAX_VALUE;
        for (Ascensor a : ascensores) {
            if (a.getEstado() == EstadoAscensor.FUERA_DE_SERVICIO) {
                continue;
            }
            int costo = Math.abs(a.getPisoActual() - piso) + a.getDestinos().size();
            boolean seAleja = (a.getDireccion() == Direccion.SUBE && piso < a.getPisoActual())
                    || (a.getDireccion() == Direccion.BAJA && piso > a.getPisoActual());
            if (seAleja) {
                costo += pisos.size();
            }
            if (a.getDireccion() != Direccion.DETENIDO && a.getDireccion() != dir) {
                costo += 2;
            }
            if (costo < mejorCosto) {
                mejorCosto = costo;
                mejor = a;
            }
        }
        return mejor;
    }

    /** Un "tick" de la simulación: cada ascensor hace una acción. */
    public void paso() {
        for (Ascensor a : ascensores) {
            pasoDe(a);
        }
    }

    private void pasoDe(Ascensor a) {
        if (a.getEstado() == EstadoAscensor.FUERA_DE_SERVICIO) {
            return;
        }
        PuertaAscensor puerta = a.getPuerta();

        // 1. Puerta abierta: esperar, intentar cerrar y vigilar el sensor.
        if (puerta.estaAbierta()) {
            puerta.cierreAutomatico();
            if (!puerta.estaAbierta()) {
                pisoDe(a).getPuerta().cerrar();
                a.setEstado(EstadoAscensor.DETENIDO);
            } else if (puerta.getSensor().verificarFalla()) {
                ponerFueraDeServicio(a, "puerta bloqueada por un obstaculo");
            }
            return;
        }

        // 2. ¿Hay que atender el piso donde está?
        if (a.debeParar()) {
            atender(a);
            return;
        }

        // 3. LOOK: seguir en su sentido o invertirlo; si no hay nada que hacer, queda detenido.
        a.cambiarDireccion();
        if (a.getDireccion() == Direccion.DETENIDO) {
            a.setEstado(EstadoAscensor.DETENIDO);
            return;
        }
        a.mover();
        if (a.debeParar()) {
            atender(a);
        }
    }

    private void atender(Ascensor a) {
        Piso piso = pisoDe(a);
        a.atenderPiso();
        piso.getPuerta().abrir();
        piso.getBotonSube().apagarIndicador();
        piso.getBotonBaja().apagarIndicador();
        alertas.registrarActividad("Ascensor " + a.getId() + " atendio el piso " + piso.getUbicacion());
    }

    private void ponerFueraDeServicio(Ascensor a, String motivo) {
        a.setEstado(EstadoAscensor.FUERA_DE_SERVICIO);
        alertas.reportarFalla("Ascensor " + a.getId() + " fuera de servicio: " + motivo);
    }

    private Piso pisoDe(Ascensor a) {
        return pisos.get(a.getPisoActual() - 1);
    }

    public void mostrarEstado() {
        for (Ascensor a : ascensores) {
            System.out.println("   " + a);
        }
    }

    public List<Ascensor> getAscensores() {
        return List.copyOf(ascensores);
    }

    public List<Piso> getPisos() {
        return List.copyOf(pisos);
    }

    public ModuloAlertas getAlertas() {
        return alertas;
    }
}
