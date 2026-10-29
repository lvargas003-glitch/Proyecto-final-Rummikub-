package com.rummikub;

import com.rummikub.domain.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   RUMMIKUB - Laura Vargas      ");
        System.out.println("==========================================\n");

        Juego juego = new Juego(42L);

        // 1. Suscribimos el Observador (imprimirá las alertas del juego en consola)
        juego.agregarObservador(mensaje -> System.out.println("[NOTIFICACIÓN EVENTO]: " + mensaje));

        // Asignamos estrategia Humano a ti y Estrategia Bot al segundo jugador
        Jugador p1 = new Jugador("1", "Laura", new EstrategiaHumano());
        Jugador p2 = new Jugador("2", "Bot", new EstrategiaBot());
        
        juego.agregarJugador(p1);
        juego.agregarJugador(p2);

        // Esto disparará la primera notificación del Observer ("Partida iniciada...")
        juego.iniciarPartida();

        System.out.println("Partida iniciada correctamente.");
        System.out.println("Fichas restantes en el pozo: " + juego.getPozo().cantidadRestante());
        System.out.println("\nMano inicial de " + p1.getNombre() + " (" + p1.getMano().cantidadFichas() + " fichas):");
        System.out.println(p1.getMano());
        System.out.println("\nMano inicial de " + p2.getNombre() + " (" + p2.getMano().cantidadFichas() + " fichas):");
        System.out.println(p2.getMano());

        // Prueba del patrón Strategy y simulación de cambio de turno con Observer
        System.out.println("\n--- SIMULANDO TURNOS ---");
        p1.jugarTurno(juego);
        
        // Al pasar turno, el Observer notificará automáticamente el cambio
        juego.pasarTurno(); 
        p2.jugarTurno(juego);
    }
}