package Ascensor;

import java.util.ArrayList;
import java.util.List;

/** Demostración: llamados desde pisos, pasajeros, LOOK y una falla de puerta. */
public class Main {
    public static void main(String[] args) {
        int numeroPisos = 6;

        List<Ascensor> ascensores = List.of(new Ascensor(1, numeroPisos), new Ascensor(2, numeroPisos));
        List<Piso> pisos = new ArrayList<>();
        for (int i = 1; i <= numeroPisos; i++) {
            pisos.add(new Piso(i));
        }
        ControlAscensor control = new ControlAscensor(ascensores, pisos);

        System.out.println("=== 1. Llamados desde los pisos ===");
        pisos.get(3).solicitar(Direccion.SUBE);   // piso 4
        pisos.get(1).solicitar(Direccion.BAJA);   // piso 2
        correr(control, 8);

        System.out.println("\n=== 2. Un pasajero entra y marca el piso 6 ===");
        Ascensor a1 = ascensores.get(0);
        a1.presionarBoton(6);
        correr(control, 8);

        System.out.println("\n=== 3. Falla: un obstaculo bloquea la puerta del ascensor 1 ===");
        a1.getPuerta().getSensor().setObstaculo(true);
        pisos.get(5).solicitar(Direccion.BAJA);   // piso 6, donde esta el ascensor 1
        correr(control, 6);

        System.out.println("\n=== 4. Con el ascensor 1 fuera de servicio, responde el 2 ===");
        pisos.get(0).solicitar(Direccion.SUBE);   // piso 1
        correr(control, 4);

        System.out.println("\n=== Historial ===");
        control.getAlertas().getHistorial().forEach(e -> System.out.println("   " + e));
    }

    private static void correr(ControlAscensor control, int pasos) {
        for (int i = 1; i <= pasos; i++) {
            System.out.println("-- paso " + i);
            control.paso();
        }
        control.mostrarEstado();
    }
}
