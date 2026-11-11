package Ascensor;

/** Clase abstracta: todo botón tiene luz y sonido; cada tipo define qué hace al presionarlo. */
public abstract class Boton {
    protected boolean iluminado;
    protected boolean sonidoActivo = true;

    public abstract void presionar();

    public void encenderIndicador() {
        iluminado = true;
        if (sonidoActivo) {
            System.out.println("   [bip]");
        }
    }

    public void apagarIndicador() {
        iluminado = false;
    }

    public boolean estaIluminado() {
        return iluminado;
    }

    public void setSonidoActivo(boolean sonidoActivo) {
        this.sonidoActivo = sonidoActivo;
    }
}