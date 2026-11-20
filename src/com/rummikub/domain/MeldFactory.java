package com.rummikub.domain;

import java.util.List;

public class MeldFactory {
    private final FabricaMeld fabricaGrupo = new FabricaGrupoMeld();
    private final FabricaMeld fabricaEscalera = new FabricaEscaleraMeld();

    public Meld crearMeld(List<Ficha> fichas) {
        Meld grupo = fabricaGrupo.crearMeld(fichas);
        if (grupo != null) return grupo;
        return fabricaEscalera.crearMeld(fichas);
    }
}