package Ascensor;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Pruebas sin librerías externas. Ejecutar con:  java PruebasAscensor
 * Cada prueba lanza AssertionError si algo no se cumple.
 */
public class PruebasAscensor {
    private static final PrintStream CONSOLA = System.out;
    private static final PrintStream SILENCIO = new PrintStream(OutputStream.nullOutputStream());
    private static int aprobadas = 0;
    private static int falladas = 0;

    public static void main(String[] args) {
        probar("Polimorfismo: BotonAscensor y BotonPiso se usan como Boton", () -> {
            ControlAscensor c = edificio(1, 4);
            Boton[] botones = {c.getAscensores().get(0).getBotones().get(2), c.getPisos().get(0).getBotonSube()};
            for (Boton b : botones) {
                b.presionar();
                verificar(b.estaIluminado(), "el boton deberia iluminarse al presionarlo");
            }
        });

        probar("Puerta del ascensor se cierra sola tras la espera", () -> {
            PuertaAscensor p = new PuertaAscensor();
            p.abrir();
            p.cierreAutomatico();
            verificar(p.estaAbierta(), "aun no deberia cerrarse");
            p.cierreAutomatico();
            verificar(!p.estaAbierta(), "deberia haberse cerrado");
        });

        probar("Sensor: el obstaculo mantiene la puerta abierta y 3 lecturas seguidas son falla", () -> {
            PuertaAscensor p = new PuertaAscensor();
            p.abrir();
            p.getSensor().setObstaculo(true);
            for (int i = 0; i < 4; i++) {
                p.cierreAutomatico();
            }
            verificar(p.estaAbierta(), "con obstaculo la puerta no debe cerrarse");
            verificar(p.getSensor().verificarFalla(), "deberia reportar falla");
            p.getSensor().setObstaculo(false);
            p.cierreAutomatico();
            verificar(!p.estaAbierta(), "sin obstaculo deberia cerrarse");
            verificar(!p.getSensor().verificarFalla(), "la falla se limpia al leer sin obstaculo");
        });

        probar("mantenerAbiertas(true) evita el cierre automatico", () -> {
            PuertaAscensor p = new PuertaAscensor();
            p.mantenerAbiertas(true);
            p.abrir();
            for (int i = 0; i < 5; i++) {
                p.cierreAutomatico();
            }
            verificar(p.estaAbierta(), "debe seguir abierta");
            p.mantenerAbiertas(false);
            p.cierreAutomatico();
            p.cierreAutomatico();
            verificar(!p.estaAbierta(), "al desactivar el modo, se cierra");
        });

        probar("Asignacion: responde el ascensor mas cercano", () -> {
            ControlAscensor c = edificio(2, 8);
            c.getAscensores().get(1).presionarBoton(5);
            pasos(c, 15);   // el ascensor 2 queda en el piso 5
            c.getPisos().get(3).solicitar(Direccion.SUBE);   // piso 4
            verificar(historial(c).contains("asignado al ascensor 2"), "deberia responder el ascensor 2");
        });

        probar("Un ascensor fuera de servicio no recibe llamados", () -> {
            ControlAscensor c = edificio(2, 5);
            c.getAscensores().get(0).setEstado(EstadoAscensor.FUERA_DE_SERVICIO);
            verificar(c.asignarAscensor(3, Direccion.SUBE) == c.getAscensores().get(1), "debe elegir el 2");
            c.getAscensores().get(1).setEstado(EstadoAscensor.FUERA_DE_SERVICIO);
            verificar(c.asignarAscensor(3, Direccion.SUBE) == null, "si todos fallan, no hay asignacion");
        });

        probar("LOOK: sigue subiendo hasta el ultimo destino y despues baja", () -> {
            ControlAscensor c = edificio(1, 8);
            Ascensor a = c.getAscensores().get(0);
            a.presionarBoton(5);
            pasos(c, 3);              // va por el piso 4
            a.presionarBoton(3);      // pedido hacia abajo mientras sube
            a.presionarBoton(1);
            pasos(c, 40);
            verificar(paradas(c).equals(List.of(5, 3, 1)), "paradas esperadas [5, 3, 1] pero fueron " + paradas(c));
        });

        probar("El boton del piso se enciende al llamar y se apaga al llegar el ascensor", () -> {
            ControlAscensor c = edificio(1, 5);
            Piso piso3 = c.getPisos().get(2);
            piso3.solicitar(Direccion.SUBE);
            verificar(piso3.getBotonSube().estaIluminado(), "debe estar encendido");
            pasos(c, 10);
            verificar(!piso3.getBotonSube().estaIluminado(), "debe apagarse al atender el piso");
        });

        probar("Un llamado repetido no se registra dos veces", () -> {
            ControlAscensor c = edificio(1, 5);
            c.getPisos().get(2).solicitar(Direccion.SUBE);
            c.getPisos().get(2).solicitar(Direccion.SUBE);
            int veces = 0;
            for (String e : c.getAlertas().getHistorial()) {
                if (e.contains("Llamado piso 3")) {
                    veces++;
                }
            }
            verificar(veces == 1, "deberia haber 1 registro, hubo " + veces);
        });

        probar("Puerta bloqueada: el ascensor queda fuera de servicio y se genera una alerta", () -> {
            ControlAscensor c = edificio(1, 5);
            Ascensor a = c.getAscensores().get(0);
            a.getPuerta().getSensor().setObstaculo(true);
            c.getPisos().get(0).solicitar(Direccion.SUBE);   // el ascensor esta en el piso 1
            pasos(c, 8);
            verificar(a.getEstado() == EstadoAscensor.FUERA_DE_SERVICIO, "deberia estar fuera de servicio");
            verificar(historial(c).contains("[FALLA]"), "deberia haber una falla en el historial");
        });

        probar("Piso.solicitar(DETENIDO) es invalido", () -> {
            ControlAscensor c = edificio(1, 3);
            boolean lanzo = false;
            try {
                c.getPisos().get(0).solicitar(Direccion.DETENIDO);
            } catch (IllegalArgumentException e) {
                lanzo = true;
            }
            verificar(lanzo, "deberia lanzar IllegalArgumentException");
        });

        System.out.println("\nResultado: " + aprobadas + " aprobadas, " + falladas + " falladas");
        if (falladas > 0) {
            System.exit(1);
        }
    }

    // ---- Ayudas ----

    private static ControlAscensor edificio(int numAscensores, int numPisos) {
        List<Ascensor> ascensores = new ArrayList<>();
        for (int i = 1; i <= numAscensores; i++) {
            ascensores.add(new Ascensor(i, numPisos));
        }
        List<Piso> pisos = new ArrayList<>();
        for (int i = 1; i <= numPisos; i++) {
            pisos.add(new Piso(i));
        }
        return new ControlAscensor(ascensores, pisos);
    }

    private static void pasos(ControlAscensor c, int n) {
        for (int i = 0; i < n; i++) {
            c.paso();
        }
    }

    private static String historial(ControlAscensor c) {
        return String.join("\n", c.getAlertas().getHistorial());
    }

    /** Pisos atendidos, en orden, leídos del historial. */
    private static List<Integer> paradas(ControlAscensor c) {
        List<Integer> resultado = new ArrayList<>();
        String marca = "atendio el piso ";
        for (String e : c.getAlertas().getHistorial()) {
            int i = e.indexOf(marca);
            if (i >= 0) {
                resultado.add(Integer.parseInt(e.substring(i + marca.length()).trim()));
            }
        }
        return resultado;
    }

    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void probar(String nombre, Runnable prueba) {
        System.setOut(SILENCIO);
        try {
            prueba.run();
            System.setOut(CONSOLA);
            System.out.println("OK     " + nombre);
            aprobadas++;
        } catch (Throwable t) {
            System.setOut(CONSOLA);
            System.out.println("FALLO  " + nombre + "  ->  " + t.getMessage());
            falladas++;
        }
    }
}
