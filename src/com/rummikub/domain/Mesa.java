package com.rummikub.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mesa implements IComponenteTablero {
    private final List<Meld> combinaciones = new ArrayList<>();

    public boolean agregarMeld(Meld m) {
        if (m != null && m.esValido()) {
            combinaciones.add(m);
            return true;
        }
        return false;
    }

    public boolean removerMeld(Meld m) {
        return combinaciones.remove(m);
    }
    public boolean reemplazarMeld(Meld anterior, Meld nuevo) {
        if (anterior == null || nuevo == null || !nuevo.esValido()) {
            return false;
    }
        int idx = combinaciones.indexOf(anterior);
        if (idx < 0) {
            return false;
    }
        combinaciones.set(idx, nuevo);
        return true;
}
    public List<Meld> getCombinaciones() {
        return Collections.unmodifiableList(combinaciones);
    }

    @Override
    public int calcularPuntos() {
        return combinaciones.stream().mapToInt(Meld::calcularPuntos).sum();
    }

    @Override
    public int contarFichas() {
        return combinaciones.stream().mapToInt(Meld::contarFichas).sum();
    }
}