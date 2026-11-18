package Ascensor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda el historial de actividad y avisa cuando hay fallas. */
public class ModuloAlertas {
    private final List<String> historial = new ArrayList<>();
    private final long inicio = System.currentTimeMillis();

    /** Registra una falla y la muestra de inmediato. */
    public void reportarFalla(String detalle) {
        historial.add(conHora("FALLA: " + detalle));
        mostrarAlerta();
    }

    /** Registra un evento normal (para análisis de mantenimiento). */
    public void registrarActividad(String evento) {
        historial.add(conHora(evento));
    }

    /** Muestra por consola el último registro. */
    public void mostrarAlerta() {
        if (!historial.isEmpty()) {
            System.out.println("[ALERTA] " + historial.get(historial.size() - 1));
        }
    }

    public long segundosFuncionando() {
        return (System.currentTimeMillis() - inicio) / 1000;
    }

    public List<String> getHistorial() {
        return historial;
    }

    private String conHora(String texto) {
        return "[" + LocalTime.now().withNano(0) + "] " + texto;
    }
}