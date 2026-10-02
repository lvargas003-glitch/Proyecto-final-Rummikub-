package com.rummikub.domain;

public class Jugador {
    private final String id;
    private final String nombre;
    private final Mano mano;
    private boolean jugadaInicialRealizada;

    public Jugador(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.mano = new Mano();
        this.jugadaInicialRealizada = false;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Mano getMano() { return mano; }
    public boolean haAbierto() { return jugadaInicialRealizada; }
    public void setJugadaInicialRealizada(boolean yaAbrio) { this.jugadaInicialRealizada = yaAbrio; }
}