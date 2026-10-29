package com.rummikub.domain;

public class Jugador {
    private final String id;
    private final String nombre;
    private final Mano mano;
    private boolean jugadaInicialRealizada;
    
    // 1. Añadimos el atributo de la estrategia (Patrón Strategy)
    private IEstrategiaJugador estrategia;

    // 2. Modificamos el constructor para recibir la estrategia
    public Jugador(String id, String nombre, IEstrategiaJugador estrategia) {
        this.id = id;
        this.nombre = nombre;
        this.mano = new Mano();
        this.jugadaInicialRealizada = false;
        this.estrategia = estrategia;
    }

    // 3. Método clave que ejecuta el turno usando la estrategia asignada
    public void jugarTurno(Juego juego) {
        estrategia.ejecutarTurno(this, juego);
    }

    // 4. Método para robar ficha del pozo (necesario para el bot y el humano)
public void robarFicha(Pozo pozo) {
    if (pozo.cantidadRestante() > 0) {
        Ficha fichaRobada = pozo.robar();
        if (fichaRobada != null) {
            mano.agregarFicha(fichaRobada);
        }
    }
}

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Mano getMano() { return mano; }
    public boolean haAbierto() { return jugadaInicialRealizada; }
    public void setJugadaInicialRealizada(boolean yaAbrio) { this.jugadaInicialRealizada = yaAbrio; }
    
    // Opcional: Permite cambiar la estrategia en tiempo de ejecución si se requiere
    public void setEstrategia(IEstrategiaJugador estrategia) {
        this.estrategia = estrategia;
    }
}