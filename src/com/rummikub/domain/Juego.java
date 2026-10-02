package com.rummikub.domain;

import java.util.ArrayList;
import java.util.List;

public class Juego {
    private final List<Jugador> jugadores = new ArrayList<>();
    private final Pozo pozo;
    private final Mesa mesa;
    private int turnoActualIdx;

    public Juego() {
        this.pozo = new Pozo();
        this.mesa = new Mesa();
        this.turnoActualIdx = 0;
    }

    public Juego(long seed) {
        this.pozo = new Pozo(seed);
        this.mesa = new Mesa();
        this.turnoActualIdx = 0;
    }

    public void agregarJugador(Jugador j) {
        jugadores.add(j);
    }

    public void iniciarPartida() {
        if (jugadores.size() < 2) {
            throw new IllegalStateException("Se necesitan al menos 2 jugadores.");
        }

        for (Jugador j : jugadores) {
            for (int i = 0; i < 14; i++) {
                j.getMano().agregarFicha(pozo.robar());
            }
        }
    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActualIdx);
    }

    public void pasarTurno() {
        turnoActualIdx = (turnoActualIdx + 1) % jugadores.size();
    }

    public Pozo getPozo() { return pozo; }
    public Mesa getMesa() { return mesa; }
}