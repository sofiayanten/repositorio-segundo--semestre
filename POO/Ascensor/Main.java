package Ascensor;

/** Demostración del sistema: solicitudes, movimiento, obstáculo y falla. */
public class Main {
    public static void main(String[] args) {
        ControlAscensor control = new ControlAscensor(2, 6); // 2 ascensores, pisos 0 a 5

        System.out.println("=== 1. Solicitudes ===");
        control.recibirSolicitud(3, Direccion.SUBE);   // alguien en el piso 3 quiere subir
        control.recibirSolicitud(5, Direccion.BAJA);   // alguien en el piso 5 quiere bajar
        control.getAscensor(0).presionarBoton(1);      // un pasajero dentro del ascensor 0 va al piso 1

        System.out.println("\n=== 2. Movimiento ===");
        for (int t = 1; t <= 14; t++) {
            control.paso();
            mostrar(control, t);
        }

        System.out.println("\n=== 3. Obstaculo en la puerta ===");
        PuertaAscensor puerta = control.getAscensor(0).getPuerta();
        puerta.abrir();
        puerta.getSensor().setObstaculo(true);
        puerta.cierreAutomatico();
        System.out.println("Puerta abierta con obstaculo? " + puerta.estaAbierta());
        puerta.getSensor().setObstaculo(false);
        puerta.cierreAutomatico();
        System.out.println("Puerta abierta sin obstaculo? " + puerta.estaAbierta());

        System.out.println("\n=== 4. Falla de un sensor ===");
        control.getAscensor(1).getPuerta().getSensor().setFalla(true);
        control.paso();
        mostrar(control, 15);

        System.out.println("\n=== 5. Historial ===");
        for (String registro : control.getAlertas().getHistorial()) {
            System.out.println(registro);
        }
    }

    private static void mostrar(ControlAscensor c, int t) {
        StringBuilder sb = new StringBuilder("t=" + t + " | ");
        for (int i = 0; i < c.getTotalAscensores(); i++) {
            Ascensor a = c.getAscensor(i);
            sb.append("Asc").append(i).append(": piso ").append(a.getPisoActual())
              .append(" ").append(a.getEstado()).append("   ");
        }
        System.out.println(sb);
    }
}