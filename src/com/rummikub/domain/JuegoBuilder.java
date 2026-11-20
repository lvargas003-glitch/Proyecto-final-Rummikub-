package com.rummikub.domain;

import java.util.ArrayList;
import java.util.List;

public class JuegoBuilder {
    private long seed = System.currentTimeMillis();
    private boolean usarSeed = false;
    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<IObservadorJuego> observadores = new ArrayList<>();

    public JuegoBuilder conSemilla(long seed) {
        this.seed = seed;
        this.usarSeed = true;
        return this;
    }

    public JuegoBuilder agregarJugador(String id, String nombre, IEstrategiaJugador estrategia) {
        this.jugadores.add(new Jugador(id, nombre, estrategia));
        return this;
    }

    public JuegoBuilder agregarObservador(IObservadorJuego observador) {
        if (observador != null) {
            this.observadores.add(observador);
        }
        return this;
    }

    public Juego build() {
        if (jugadores.size() < 2) {
            throw new IllegalStateException("Se requieren al menos 2 jugadores para construir la partida.");
        }
        Juego juego = usarSeed ? new Juego(seed) : new Juego();
        for (IObservadorJuego obs : observadores) {
            juego.agregarObservador(obs);
        }
        for (Jugador j : jugadores) {
            juego.agregarJugador(j);
        }
        juego.iniciarPartida();
        return juego;
    }
}