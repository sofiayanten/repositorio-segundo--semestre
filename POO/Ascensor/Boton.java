package Ascensor;

/** Clase abstracta: lo que tienen en común todos los botones. */
public abstract class Boton {
    protected boolean iluminado;
    protected boolean sonidoActivo = true;

    /** Cada tipo de botón decide qué hace al presionarse (polimorfismo). */
    public abstract void presionar();

    public void encenderIndicador() {
        iluminado = true;
        if (sonidoActivo) {
            System.out.println("   bip (boton iluminado)");
        }
    }

    public void apagarIndicador() {
        iluminado = false;
    }

    public boolean isIluminado() {
        return iluminado;
    }
}