package com.rummikub.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Juego {
    private final List<Jugador> jugadores = new ArrayList<>();
    private final Pozo pozo;
    private final Mesa mesa;
    private final MeldFactory meldFactory = new MeldFactory();
    private final Deque<IComando> historialComandos = new ArrayDeque<>();
    private final List<IObservadorJuego> observadores = new ArrayList<>();

    private int turnoActualIdx = 0;
    private int turnosPasadosSinCambios = 0;
    private boolean partidaTerminada = false;
    private Jugador ganador;

    public Juego() {
        this.pozo = new Pozo();
        this.mesa = new Mesa();
    }

    public Juego(long seed) {
        this.pozo = new Pozo(seed);
        this.mesa = new Mesa();
    }

    public void agregarJugador(Jugador j) {
        jugadores.add(j);
    }

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
        turnoActualIdx = 0;
        turnosPasadosSinCambios = 0;
        partidaTerminada = false;
        ganador = null;
        notificarObservadores("Partida iniciada con " + jugadores.size() + " jugadores. Turno de: " + getJugadorActual().getNombre());
    }

    public boolean ejecutarComando(IComando comando) {
        if (partidaTerminada || comando == null) return false;
        boolean ok = comando.ejecutar();
        if (ok) {
            historialComandos.push(comando);
            notificarObservadores("Comando ejecutado: " + comando.descripcion());
            verificarVictoria();
        }
        return ok;
    }

    public boolean deshacerUltimoComando() {
        if (partidaTerminada || historialComandos.isEmpty()) return false;
        IComando ultimo = historialComandos.pop();
        boolean ok = ultimo.deshacer();
        if (ok) {
            notificarObservadores("Se deshizo la acción: " + ultimo.descripcion());
        }
        return ok;
    }

    public boolean jugarTurnoActual() {
        if (partidaTerminada) return false;
        Jugador actual = getJugadorActual();
        int fichasAntes = actual.getMano().cantidadFichas();
        int pozoAntes = pozo.cantidadRestante();

        actual.jugarTurno(this);
        verificarVictoria();

        if (!partidaTerminada) {
            boolean huboMovimiento = actual.getMano().cantidadFichas() != fichasAntes
                    || pozo.cantidadRestante() != pozoAntes;
            if (!huboMovimiento) {
                turnosPasadosSinCambios++;
            } else {
                turnosPasadosSinCambios = 0;
            }

            if (pozo.cantidadRestante() == 0 && turnosPasadosSinCambios >= jugadores.size() * 2) {
                finalizarPorBloqueo();
            } else {
                pasarTurno();
            }
        }
        return !partidaTerminada;
    }
    public void finalizarPartidaManual() {
        if (partidaTerminada || jugadores.isEmpty()) {
            return;
        }
        this.partidaTerminada = true;
        Jugador menorPuntos = jugadores.get(0);
        int menorSuma = calcularPuntosMano(menorPuntos);
        for (Jugador j : jugadores) {
            int suma = calcularPuntosMano(j);
            if (suma < menorSuma) {
                menorSuma = suma;
                menorPuntos = j;
            }
        }
        this.ganador = menorPuntos;
        notificarObservadores("Partida finalizada manualmente. Ganador por menor puntaje en mano: "
                + ganador.getNombre() + " (" + menorSuma + " pts)");
    }

    private void verificarVictoria() {
        for (Jugador j : jugadores) {
            if (j.getMano().cantidadFichas() == 0) {
                this.ganador = j;
                this.partidaTerminada = true;
                notificarObservadores("¡Partida finalizada! Ganador: " + j.getNombre());
                return;
            }
        }
    }

    private void finalizarPorBloqueo() {
        this.partidaTerminada = true;
        Jugador menorPuntos = jugadores.get(0);
        int menorSuma = calcularPuntosMano(menorPuntos);
        for (Jugador j : jugadores) {
            int suma = calcularPuntosMano(j);
            if (suma < menorSuma) {
                menorSuma = suma;
                menorPuntos = j;
            }
        }
        this.ganador = menorPuntos;
        notificarObservadores("Pozo agotado sin movimientos. Ganador por menor puntaje en mano: "
                + ganador.getNombre() + " (" + menorSuma + " pts)");
    }

    private int calcularPuntosMano(Jugador j) {
        return j.getMano().getFichas().stream().mapToInt(Ficha::calcularPuntos).sum();
    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActualIdx);
    }

    public void pasarTurno() {
        turnoActualIdx = (turnoActualIdx + 1) % jugadores.size();
        notificarObservadores("Cambio de turno -> Ahora juega: " + getJugadorActual().getNombre());
    }

    public List<Jugador> getJugadores() { return List.copyOf(jugadores); }
    public Pozo getPozo() { return pozo; }
    public Mesa getMesa() { return mesa; }
    public MeldFactory getMeldFactory() { return meldFactory; }
    public boolean isPartidaTerminada() { return partidaTerminada; }
    public Jugador getGanador() { return ganador; }
}