package com.rummikub.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mano {
    private final List<Ficha> fichas = new ArrayList<>();

    public void agregarFicha(Ficha f) {
        if (f != null) fichas.add(f);
    }

    public List<Ficha> getFichas() {
        return Collections.unmodifiableList(fichas);
    }

    public int tamanio() {
        return fichas.size();
    }

    @Override
    public String toString() {
        return fichas.toString();
    }
}