package com.rummikub.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mano {
    private final List<Ficha> fichas = new ArrayList<>();

    public void agregarFicha(Ficha f) {
        if (f != null) fichas.add(f);
    }

    public boolean removerFicha(Ficha f) {
        return fichas.remove(f);
    }

    public boolean tieneTodas(List<Ficha> requeridas) {
        List<Ficha> copia = new ArrayList<>(fichas);
        for (Ficha f : requeridas) {
            if (!copia.remove(f)) {
                return false;
            }
        }
        return true;
    }

    public void removerFichas(List<Ficha> requeridas) {
        for (Ficha f : requeridas) {
            fichas.remove(f);
        }
    }

    public List<Ficha> getFichas() {
        return Collections.unmodifiableList(fichas);
    }

    public int cantidadFichas() {
        return fichas.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fichas.size(); i++) {
            sb.append(i).append(":").append(fichas.get(i));
            if (i < fichas.size() - 1) sb.append(" ");
        }
        return sb.toString();
    }
}