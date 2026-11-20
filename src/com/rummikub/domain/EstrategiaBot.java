package com.rummikub.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EstrategiaBot implements IEstrategiaJugador {

    @Override
    public void ejecutarTurno(Jugador jugador, Juego juego) {
        System.out.println("\n--- Turno del Bot: " + jugador.getNombre() + " ---");
        List<Ficha> manoActual = new ArrayList<>(jugador.getMano().getFichas());

        Meld combinacionEncontrada = buscarCombinacionValida(manoActual, juego.getMeldFactory());
        if (combinacionEncontrada != null) {
            boolean cumpleApertura = jugador.haAbierto() || combinacionEncontrada.calcularPuntos() >= 30;
            if (cumpleApertura) {
                IComando cmd = new ColocarMeldComando(jugador, juego.getMesa(), combinacionEncontrada);
                if (juego.ejecutarComando(cmd)) {
                    System.out.println(jugador.getNombre() + " bajó: " + combinacionEncontrada
                            + " (" + combinacionEncontrada.calcularPuntos() + " pts)");
                    return;
                }
            }
        }

        IComando robarCmd = new RobarFichaComando(jugador, juego.getPozo());
        if (juego.ejecutarComando(robarCmd)) {
            System.out.println(jugador.getNombre() + " robó una ficha del pozo.");
        } else {
            System.out.println(jugador.getNombre() + " no pudo bajar ni robar fichas.");
        }
    }

    private Meld buscarCombinacionValida(List<Ficha> fichas, MeldFactory factory) {
        // 1. Buscar grupos válidos de 3 o 4 fichas
        int n = fichas.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    List<Ficha> trio = List.of(fichas.get(i), fichas.get(j), fichas.get(k));
                    Meld m = factory.crearMeld(trio);
                    if (m != null) return m;
                }
            }
        }

        // 2. Buscar escaleras válidas de 3 fichas del mismo color
        List<Ficha> ordenadas = new ArrayList<>(fichas);
        ordenadas.sort(Comparator.comparing(Ficha::getColor).thenComparingInt(Ficha::getValor));
        for (int i = 0; i < ordenadas.size() - 2; i++) {
            List<Ficha> tramo = List.of(ordenadas.get(i), ordenadas.get(i + 1), ordenadas.get(i + 2));
            Meld m = factory.crearMeld(tramo);
            if (m != null) return m;
        }
        return null;
    }
}