package Ascensor;

import java.util.ArrayList;
import java.util.List;

/** Pruebas simples (sin librerías externas). Ejecutar: java -cp out PruebasSistema */
public class PruebasSistema {
    private static int ok = 0;
    private static int fallos = 0;

    public static void main(String[] args) {
        pruebaBotones();
        pruebaPiso();
        pruebaPuertas();
        pruebaDireccion();
        pruebaCambioDeDireccion();
        pruebaAsignacion();
        pruebaFalla();
        pruebaFlujoCompleto();
        System.out.println("\nResultado: " + ok + " OK, " + fallos + " fallos");
        if (fallos > 0) {
            System.exit(1);
        }
    }

    private static void verificar(String nombre, boolean condicion) {
        if (condicion) {
            ok++;
            System.out.println("OK    - " + nombre);
        } else {
            fallos++;
            System.out.println("FALLO - " + nombre);
        }
    }

    /** Mueve el ascensor hasta que no queden paradas y devuelve los pisos donde paró. */
    private static List<Integer> correr(Ascensor a) {
        List<Integer> paradas = new ArrayList<>();
        int seguridad = 0;
        while (!a.getDestinos().isEmpty() && seguridad++ < 100) {
            a.mover();
            if (a.getEstado() == Estado.ATENDIENDO) {
                paradas.add(a.getPisoActual());
            }
        }
        return paradas;
    }

    private static void pruebaBotones() {
        BotonPiso b = new BotonPiso(2, Direccion.SUBE);
        verificar("Boton inicia apagado", !b.isIluminado());
        b.presionar();
        verificar("Boton se ilumina al presionarlo", b.isIluminado());
        b.apagarIndicador();
        verificar("Boton se apaga", !b.isIluminado());

        Boton[] lista = {new BotonAscensor(3), new BotonPiso(1, Direccion.BAJA)};
        boolean todos = true;
        for (Boton x : lista) {
            x.presionar(); // polimorfismo: cada uno hace lo suyo
            todos = todos && x.isIluminado();
        }
        verificar("Polimorfismo: ambos tipos de boton se iluminan", todos);
    }

    private static void pruebaPiso() {
        Piso p = new Piso(2);
        p.solicitar(Direccion.SUBE);
        verificar("Piso: solo se ilumina el boton de subir",
                p.getBotonSube().isIluminado() && !p.getBotonBaja().isIluminado());
        p.llegoAscensor();
        verificar("Piso: se apagan los botones al llegar el ascensor", !p.getBotonSube().isIluminado());
    }

    private static void pruebaPuertas() {
        PuertaAscensor pa = new PuertaAscensor();
        pa.abrir();
        verificar("Puerta se abre", pa.estaAbierta());
        pa.cierreAutomatico();
        verificar("Puerta se cierra automaticamente", !pa.estaAbierta());

        pa.abrir();
        pa.getSensor().setObstaculo(true);
        pa.cierreAutomatico();
        verificar("Puerta NO se cierra con obstaculo", pa.estaAbierta());
        pa.getSensor().setObstaculo(false);
        pa.cierreAutomatico();
        verificar("Puerta se cierra al quitar el obstaculo", !pa.estaAbierta());

        pa.abrir();
        pa.mantenerAbiertas(true);
        pa.cierreAutomatico();
        verificar("Mantener abiertas evita el cierre", pa.estaAbierta());
        pa.mantenerAbiertas(false);
        pa.cierreAutomatico();
        verificar("Al soltar, la puerta se cierra", !pa.estaAbierta());
    }

    private static void pruebaDireccion() {
        Ascensor a = new Ascensor(0, 6);
        a.agregarDestino(4);
        a.agregarDestino(2);
        verificar("Subiendo para en el 2 antes que en el 4", correr(a).equals(List.of(2, 4)));
    }

    private static void pruebaCambioDeDireccion() {
        Ascensor a = new Ascensor(0, 6);
        a.agregarDestino(3);
        correr(a);
        a.agregarDestino(5);
        a.agregarDestino(1);
        List<Integer> paradas = correr(a);
        verificar("Termina de subir (5) antes de bajar (1)", paradas.equals(List.of(5, 1)));
        verificar("Cambio la direccion a BAJA", a.getDireccion() == Direccion.BAJA);
    }

    private static void pruebaAsignacion() {
        ControlAscensor c = new ControlAscensor(2, 6);
        c.getAscensor(1).agregarDestino(5);
        correr(c.getAscensor(1)); // el ascensor 1 queda en el piso 5
        verificar("Se asigna el ascensor mas cercano",
                c.asignarAscensor(4, Direccion.BAJA).getId() == 1);
    }

    private static void pruebaFalla() {
        ControlAscensor c = new ControlAscensor(2, 6);
        c.getAscensor(0).getPuerta().getSensor().setFalla(true);
        c.paso();
        verificar("Falla: el ascensor se detiene", c.getAscensor(0).estaEnFalla());
        verificar("Falla: la puerta se abre", c.getAscensor(0).getPuerta().estaAbierta());
        verificar("Falla: queda en el historial",
                c.getAlertas().getHistorial().stream().anyMatch(s -> s.contains("FALLA")));
        verificar("Falla: no se asigna el ascensor danado",
                c.asignarAscensor(0, Direccion.SUBE).getId() == 1);
    }

    private static void pruebaFlujoCompleto() {
        ControlAscensor c = new ControlAscensor(2, 6);
        c.recibirSolicitud(3, Direccion.SUBE);
        for (int i = 0; i < 10; i++) {
            c.paso();
        }
        verificar("Flujo: el ascensor llego al piso 3", c.getAscensor(0).getPisoActual() == 3);
        verificar("Flujo: el boton del piso 3 se apago", !c.getPiso(3).getBotonSube().isIluminado());
    }
}