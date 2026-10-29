package com.rummikub.domain;

public class EstrategiaHumano implements IEstrategiaJugador {
    @Override
    public void ejecutarTurno(Jugador jugador, Juego juego) {
        System.out.println("Turno del jugador humano: " + jugador.getNombre());
        
    }
}