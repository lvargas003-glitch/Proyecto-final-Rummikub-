package com.rummikub.domain;

import java.util.List;

public class FabricaGrupoMeld extends FabricaMeld {
    @Override
    public Meld crearMeld(List<Ficha> fichas) {
        GrupoMeld grupo = new GrupoMeld(fichas);
        return grupo.esValido() ? grupo : null;
    }
}