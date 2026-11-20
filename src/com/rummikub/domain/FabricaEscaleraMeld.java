package com.rummikub.domain;

import java.util.List;

public class FabricaEscaleraMeld extends FabricaMeld {
    @Override
    public Meld crearMeld(List<Ficha> fichas) {
        EscaleraMeld escalera = new EscaleraMeld(fichas);
        return escalera.esValido() ? escalera : null;
    }
}