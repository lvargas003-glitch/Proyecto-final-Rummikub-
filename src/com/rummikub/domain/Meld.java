package com.rummikub.domain;

import java.util.List;

public abstract class Meld {
    protected List<Ficha> fichas;

    public Meld(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    public abstract boolean esValido();
    public abstract int calcularPuntos();

    public List<Ficha> getFichas() {
        return fichas;
    }
}