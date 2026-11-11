package Ascensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Guarda el historial de eventos y muestra las alertas por falla. */
public class ModuloAlertas {
    private static final String PREFIJO_FALLA = "[FALLA] ";
    private static final String PREFIJO_ACTIVIDAD = "[ACTIVIDAD] ";

    private final List<String> historial = new ArrayList<>();

    public void reportarFalla(String detalle) {
        historial.add(PREFIJO_FALLA + detalle);
        mostrarAlerta();
    }

    public void registrarActividad(String evento) {
        historial.add(PREFIJO_ACTIVIDAD + evento);
    }

    /** Muestra la última falla registrada. */
    public void mostrarAlerta() {
        for (int i = historial.size() - 1; i >= 0; i--) {
            if (historial.get(i).startsWith(PREFIJO_FALLA)) {
                System.out.println("!!! ALERTA: " + historial.get(i).substring(PREFIJO_FALLA.length()));
                return;
            }
        }
    }

    public List<String> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
}
