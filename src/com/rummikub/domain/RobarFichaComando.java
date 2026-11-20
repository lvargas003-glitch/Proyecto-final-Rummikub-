package com.rummikub.domain;

public class RobarFichaComando implements IComando {
    private final Jugador jugador;
    private final Pozo pozo;
    private Ficha fichaRobada;

    public RobarFichaComando(Jugador jugador, Pozo pozo) {
        this.jugador = jugador;
        this.pozo = pozo;
    }

    @Override
    public boolean ejecutar() {
        if (pozo.cantidadRestante() <= 0) {
            return false;
        }
        this.fichaRobada = pozo.robar();
        if (this.fichaRobada != null) {
            jugador.getMano().agregarFicha(this.fichaRobada);
            return true;
        }
        return false;
    }

    @Override
    public boolean deshacer() {
        if (this.fichaRobada == null) {
            return false;
        }
        if (jugador.getMano().removerFicha(this.fichaRobada)) {
            pozo.devolverFicha(this.fichaRobada);
            this.fichaRobada = null;
            return true;
        }
        return false;
    }

    @Override
    public String descripcion() {
        return jugador.getNombre() + " robó una ficha del pozo";
    }
}