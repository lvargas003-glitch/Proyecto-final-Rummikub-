package com.rummikub.domain;

public class ColocarMeldComando implements IComando {
    private final Jugador jugador;
    private final Mesa mesa;
    private final Meld meld;
    private boolean ejecutadoConExito = false;

    public ColocarMeldComando(Jugador jugador, Mesa mesa, Meld meld) {
        this.jugador = jugador;
        this.mesa = mesa;
        this.meld = meld;
    }

    @Override
    public boolean ejecutar() {
        if (meld == null || !meld.esValido()) {
            return false;
        }
        if (!jugador.getMano().tieneTodas(meld.getFichas())) {
            return false;
        }
        if (!jugador.haAbierto() && meld.calcularPuntos() < 30) {
            return false;
        }
        if (!mesa.agregarMeld(meld)) {
            return false;
        }
        jugador.getMano().removerFichas(meld.getFichas());
        if (!jugador.haAbierto()) {
            jugador.setJugadaInicialRealizada(true);
        }
        ejecutadoConExito = true;
        return true;
    }

    @Override
    public boolean deshacer() {
        if (!ejecutadoConExito) {
            return false;
        }
        if (mesa.removerMeld(meld)) {
            for (Ficha f : meld.getFichas()) {
                jugador.getMano().agregarFicha(f);
            }
            ejecutadoConExito = false;
            return true;
        }
        return false;
    }

    @Override
    public String descripcion() {
        return jugador.getNombre() + " colocó combinación " + meld;
    }
}