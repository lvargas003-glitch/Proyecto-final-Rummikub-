package com.rummikub.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Meld implements IComponenteTablero {
    protected final List<Ficha> fichas;

    public Meld(List<Ficha> fichas) {
        this.fichas = new ArrayList<>(fichas);
    }

    public abstract boolean esValido();

    @Override
    public int calcularPuntos() {
        return fichas.stream().mapToInt(Ficha::calcularPuntos).sum();
    }

    @Override
    public int contarFichas() {
        return fichas.size();
    }

    public List<Ficha> getFichas() {
        return List.copyOf(fichas);
    }

    @Override
    public String toString() {
        return fichas.toString();
    }
}