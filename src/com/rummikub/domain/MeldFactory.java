package com.rummikub.domain;

import java.util.List;

public class MeldFactory {

    public Meld crearMeld(List<Ficha> fichas) {
        if (fichas == null || fichas.size() < 3) {
            return null;
        }
        GrupoMeld grupo = new GrupoMeld(fichas);
        if (grupo.esValido()) {
            return grupo;
        }
        EscaleraMeld escalera = new EscaleraMeld(fichas);
        if (escalera.esValido()) {
            return escalera;
        }
        return null;
    }
}