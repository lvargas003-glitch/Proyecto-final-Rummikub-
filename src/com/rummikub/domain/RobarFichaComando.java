package com.rummikub.domain;

public class RobarFichaComando implements IComando {
    private final Jugador jugador;
    private final Pozo pozo;

    public RobarFichaComando(Jugador jugador, Pozo pozo) {
        this.jugador = jugador;
        this.pozo = pozo;
    }

    @Override
    public void ejecutar() {
        jugador.robarFicha(pozo);
    }
}