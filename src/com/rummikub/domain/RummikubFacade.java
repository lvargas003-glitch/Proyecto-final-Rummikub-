package com.rummikub.domain;

import java.util.Scanner;

public class RummikubFacade {
    private Juego juego;

    public void iniciarPartidaEstandar(String nombreHumano, String nombreBot, long seed, Scanner scanner) {
        this.juego = new JuegoBuilder()
                .conSemilla(seed)
                .agregarJugador("1", nombreHumano, new EstrategiaHumano(scanner))
                .agregarJugador("2", nombreBot, new EstrategiaBot())
                .agregarObservador(mensaje -> System.out.println("[OBSERVER]: " + mensaje))
                .build();
    }

    public void ejecutarCicloPartida(int maxTurnosSeguridad) {
        if (juego == null) {
            throw new IllegalStateException("La partida no ha sido iniciada.");
        }
        int turnos = 0;
        while (!juego.isPartidaTerminada() && turnos < maxTurnosSeguridad) {
            juego.jugarTurnoActual();
            turnos++;
        }
        if (juego.getGanador() != null) {
            System.out.println("\n=== GANADOR: " + juego.getGanador().getNombre() + " ===");
        } else {
            System.out.println("\n=== Partida detenida tras " + turnos + " turnos ===");
        }
        System.out.println("Total fichas en mesa (Composite): " + juego.getMesa().contarFichas()
                + " | Puntos en mesa: " + juego.getMesa().calcularPuntos());
    }

    public boolean deshacerUltimaAccion() {
        return juego != null && juego.deshacerUltimoComando();
    }

    public Juego getJuego() {
        return juego;
    }
}