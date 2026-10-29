package com.rummikub.domain;

import java.util.ArrayList;
import java.util.List;

public class Juego {
    private final List<Jugador> jugadores = new ArrayList<>();
    private final Pozo pozo;
    private final Mesa mesa;
    private int turnoActualIdx;
    
    // 1. Lista de observadores (Patrón Observer)
    private final List<IObservadorJuego> observadores = new ArrayList<>();

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

    // 2. Métodos para gestionar observadores
    public void agregarObservador(IObservadorJuego obs) {
        observadores.add(obs);
    }

    public void eliminarObservador(IObservadorJuego obs) {
        observadores.remove(obs);
    }

    public void notificarObservadores(String mensaje) {
        for (IObservadorJuego obs : observadores) {
            obs.actualizar(mensaje);
        }
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
        // Notificamos que la partida ha comenzado
        notificarObservadores("Partida iniciada con " + jugadores.size() + " jugadores.");
    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActualIdx);
    }

    public void pasarTurno() {
        turnoActualIdx = (turnoActualIdx + 1) % jugadores.size();
        // Notificamos el cambio de turno automáticamente a los observadores
        notificarObservadores("El turno ha cambiado. Ahora es el turno de: " + getJugadorActual().getNombre());
    }

    public Pozo getPozo() { return pozo; }
    public Mesa getMesa() { return mesa; }
}