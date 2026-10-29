package com.rummikub.domain;

public class EstrategiaBot implements IEstrategiaJugador {
    @Override
    public void ejecutarTurno(Jugador jugador, Juego juego) {
        System.out.println("Turno automático del Bot: " + jugador.getNombre());
        
        // 1. El bot intenta hacer una jugada o apertura
        boolean jugo = intentarJugadaAutomatica(jugador, juego);
        
        // 2. Si no pudo jugar, roba una ficha del pozo automáticamente
        if (!jugo) {
            jugador.robarFicha(juego.getPozo());
            System.out.println(jugador.getNombre() + " (Bot) no tuvo jugada válida y robó una ficha del pozo.");
        }
    }

    private boolean intentarJugadaAutomatica(Jugador jugador, Juego juego) {
        // Heurística simplificada del bot (retorna true si bajó fichas, false si roba)
        return false; 
    }
}