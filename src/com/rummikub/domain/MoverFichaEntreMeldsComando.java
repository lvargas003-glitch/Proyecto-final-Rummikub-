package com.rummikub.domain;

import java.util.ArrayList;
import java.util.List;

public class MoverFichaEntreMeldsComando implements IComando {
    private final Jugador jugador;
    private final Mesa mesa;
    private final Meld meldOrigen;
    private final Ficha fichaTomada;
    private final List<Ficha> fichasAdicionalesMano;
    private final MeldFactory factory;

    private Meld origenReducido;
    private Meld nuevoMeldCreado;
    private boolean ejecutadoConExito = false;

    public MoverFichaEntreMeldsComando(
            Jugador jugador,
            Mesa mesa,
            Meld meldOrigen,
            Ficha fichaTomada,
            List<Ficha> fichasAdicionalesMano,
            MeldFactory factory
    ) {
        this.jugador = jugador;
        this.mesa = mesa;
        this.meldOrigen = meldOrigen;
        this.fichaTomada = fichaTomada;
        this.fichasAdicionalesMano = new ArrayList<>(fichasAdicionalesMano);
        this.factory = factory;
    }

    @Override
    public boolean ejecutar() {
        if (meldOrigen == null || fichaTomada == null || factory == null) {
            return false;
        }
        if (!jugador.haAbierto()) {
            return false;
        }
        if (!mesa.getCombinaciones().contains(meldOrigen)) {
            return false;
        }
        if (!meldOrigen.getFichas().contains(fichaTomada)) {
            return false;
        }
        if (!jugador.getMano().tieneTodas(fichasAdicionalesMano)) {
            return false;
        }

        // 1. Verificar que el meld origen siga siendo válido sin la ficha tomada
        List<Ficha> restantesOrigen = new ArrayList<>(meldOrigen.getFichas());
        restantesOrigen.remove(fichaTomada);
        Meld candidatoOrigen = factory.crearMeld(restantesOrigen);
        if (candidatoOrigen == null || !candidatoOrigen.esValido()) {
            return false;
        }

        // 2. Armar el nuevo meld con la ficha tomada de la mesa + fichas de la mano
        List<Ficha> fichasNuevoMeld = new ArrayList<>(fichasAdicionalesMano);
        fichasNuevoMeld.add(fichaTomada);
        Meld candidatoNuevo = factory.crearMeld(fichasNuevoMeld);
        if (candidatoNuevo == null || !candidatoNuevo.esValido()) {
            return false;
        }

        // 3. Aplicar cambios en mesa y mano
        if (!mesa.reemplazarMeld(meldOrigen, candidatoOrigen)) {
            return false;
        }
        if (!mesa.agregarMeld(candidatoNuevo)) {
            mesa.reemplazarMeld(candidatoOrigen, meldOrigen);
            return false;
        }

        jugador.getMano().removerFichas(fichasAdicionalesMano);
        this.origenReducido = candidatoOrigen;
        this.nuevoMeldCreado = candidatoNuevo;
        this.ejecutadoConExito = true;
        return true;
    }

    @Override
    public boolean deshacer() {
        if (!ejecutadoConExito) {
            return false;
        }
        mesa.removerMeld(nuevoMeldCreado);
        mesa.reemplazarMeld(origenReducido, meldOrigen);
        for (Ficha f : fichasAdicionalesMano) {
            jugador.getMano().agregarFicha(f);
        }
        this.ejecutadoConExito = false;
        return true;
    }

    @Override
    public String descripcion() {
        return jugador.getNombre() + " movió " + fichaTomada + " desde " + meldOrigen
                + " para formar " + nuevoMeldCreado;
    }
}