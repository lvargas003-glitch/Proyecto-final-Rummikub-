package com.rummikub.domain;

import java.util.List;

public class EscaleraMeld extends Meld {

    public EscaleraMeld(List<Ficha> fichas) {
        super(fichas);
    }

    @Override
    public boolean esValido() {
        if (fichas == null || fichas.size() < 3) return false;
        return true;
    }

    @Override
    public int calcularPuntos() {
        return fichas.stream().mapToInt(Ficha::getValor).sum();
    }
}