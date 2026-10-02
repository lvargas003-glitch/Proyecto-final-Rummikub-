package com.rummikub.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mesa {
    private final List<Meld> combinaciones = new ArrayList<>();

    public boolean agregarMeld(Meld m) {
        if (m.esValido()) {
            combinaciones.add(m);
            return true;
        }
        return false;
    }

    public List<Meld> getCombinaciones() {
        return Collections.unmodifiableList(combinaciones);
    }
}