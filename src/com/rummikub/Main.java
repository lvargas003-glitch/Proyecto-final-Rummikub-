package com.rummikub;

import com.rummikub.domain.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   RUMMIKUB - Laura Vargas      ");
        System.out.println("==========================================\n");

        Juego juego = new Juego(42L);

        Jugador p1 = new Jugador("1", "Laura");
        Jugador p2 = new Jugador("2", "Valentina");
        juego.agregarJugador(p1);
        juego.agregarJugador(p2);

        juego.iniciarPartida();

        System.out.println("Partida iniciada correctamente.");
        System.out.println("Fichas restantes en el pozo: " + juego.getPozo().cantidadRestante());
        System.out.println("\nMano inicial de " + p1.getNombre() + " (" + p1.getMano().tamanio() + " fichas):");
        System.out.println(p1.getMano());
        System.out.println("\nMano inicial de " + p2.getNombre() + " (" + p2.getMano().tamanio() + " fichas):");
        System.out.println(p2.getMano());
    }
}