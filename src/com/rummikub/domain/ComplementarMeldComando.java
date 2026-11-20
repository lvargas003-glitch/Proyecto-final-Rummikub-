package com.rummikub.domain;

import java.util.ArrayList;
import java.util.List;

public class ComplementarMeldComando implements IComando {
    private final Jugador jugador;
    private final Mesa mesa;
    private final Meld meldOriginal;
    private final List<Ficha> fichasAgregadas;
    private final MeldFactory factory;
    private Meld meldActualizado;
    private boolean ejecutadoConExito = false;

    public ComplementarMeldComando(
            Jugador jugador,
            Mesa mesa,
            Meld meldOriginal,
            List<Ficha> fichasAgregadas,
            MeldFactory factory
    ) {
        this.jugador = jugador;
        this.mesa = mesa;
        this.meldOriginal = meldOriginal;
        this.fichasAgregadas = new ArrayList<>(fichasAgregadas);
        this.factory = factory;
    }

    @Override
    public boolean ejecutar() {
        if (meldOriginal == null || fichasAgregadas.isEmpty() || factory == null) {
            return false;
        }
        if (!jugador.haAbierto()) {
            return false;
        }
        if (!jugador.getMano().tieneTodas(fichasAgregadas)) {
            return false;
        }
        if (!mesa.getCombinaciones().contains(meldOriginal)) {
            return false;
        }

        List<Ficha> combinadas = new ArrayList<>(meldOriginal.getFichas());
        combinadas.addAll(fichasAgregadas);

        Meld candidato = factory.crearMeld(combinadas);
        if (candidato == null || !candidato.esValido()) {
            return false;
        }

        if (!mesa.reemplazarMeld(meldOriginal, candidato)) {
            return false;
        }

        jugador.getMano().removerFichas(fichasAgregadas);
        this.meldActualizado = candidato;
        this.ejecutadoConExito = true;
        return true;
    }

    @Override
    public boolean deshacer() {
        if (!ejecutadoConExito || meldActualizado == null) {
            return false;
        }
        if (!mesa.reemplazarMeld(meldActualizado, meldOriginal)) {
            return false;
        }
        for (Ficha f : fichasAgregadas) {
            jugador.getMano().agregarFicha(f);
        }
        this.ejecutadoConExito = false;
        return true;
    }

    @Override
    public String descripcion() {
        return jugador.getNombre() + " complementó " + meldOriginal + " con " + fichasAgregadas;
    }
}