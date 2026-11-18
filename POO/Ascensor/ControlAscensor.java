package Ascensor;

/** El "cerebro": recibe las solicitudes, elige ascensor y hace avanzar el sistema. */
public class ControlAscensor {
    private final Ascensor[] ascensores;
    private final Piso[] pisos;
    private final ModuloAlertas alertas = new ModuloAlertas();

    /** Crea todas sus partes (composición): ascensores y pisos numerados desde 0. */
    public ControlAscensor(int totalAscensores, int totalPisos) {
        ascensores = new Ascensor[totalAscensores];
        for (int i = 0; i < totalAscensores; i++) {
            ascensores[i] = new Ascensor(i, totalPisos);
        }
        pisos = new Piso[totalPisos];
        for (int i = 0; i < totalPisos; i++) {
            pisos[i] = new Piso(i);
        }
    }

    /** Alguien presionó el botón de un piso. */
    public void recibirSolicitud(int piso, Direccion dir) {
        if (piso < 0 || piso >= pisos.length) {
            throw new IllegalArgumentException("Piso inexistente: " + piso);
        }
        pisos[piso].solicitar(dir);
        Ascensor elegido = asignarAscensor(piso, dir);
        if (elegido == null) {
            alertas.reportarFalla("Sin ascensores disponibles para el piso " + piso);
            return;
        }
        elegido.agregarDestino(piso);
        alertas.registrarActividad("Solicitud piso " + piso + " (" + dir + ") -> ascensor " + elegido.getId());
    }

    /** Elige el ascensor sano con menor "costo" (distancia + pedidos pendientes). */
    public Ascensor asignarAscensor(int piso, Direccion dir) {
        Ascensor mejor = null;
        for (Ascensor a : ascensores) {
            if (a.estaEnFalla()) {
                continue;
            }
            if (mejor == null || costo(a, piso) < costo(mejor, piso)) {
                mejor = a;
            }
        }
        return mejor;
    }

    private int costo(Ascensor a, int piso) {
        return Math.abs(a.getPisoActual() - piso) + a.getDestinos().size();
    }

    /** Un tick del reloj del sistema: cada ascensor revisa su sensor y avanza. */
    public void paso() {
        for (Ascensor a : ascensores) {
            if (a.estaEnFalla()) {
                continue;
            }
            if (a.getPuerta().getSensor().verificarFalla()) {
                alertas.reportarFalla("Sensor de puerta del ascensor " + a.getId()
                        + " en piso " + a.getPisoActual() + ". Ascensor detenido.");
                a.detenerPorFalla();
                continue;
            }
            int antes = a.getPisoActual();
            a.mover();
            if (a.getEstado() == Estado.ATENDIENDO) {
                pisos[a.getPisoActual()].llegoAscensor();
                alertas.registrarActividad("Ascensor " + a.getId() + " atendio el piso " + a.getPisoActual());
            } else if (a.getPisoActual() != antes) {
                alertas.registrarActividad("Ascensor " + a.getId() + " paso al piso " + a.getPisoActual());
            }
        }
    }

    public Ascensor getAscensor(int i) {
        return ascensores[i];
    }

    public int getTotalAscensores() {
        return ascensores.length;
    }

    public Piso getPiso(int i) {
        return pisos[i];
    }

    public ModuloAlertas getAlertas() {
        return alertas;
    }
}