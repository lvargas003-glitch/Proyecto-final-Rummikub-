package com.rummikub.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Pozo {
    private final List<Ficha> fichas = new ArrayList<>();

    public Pozo() {
        inicializar(new Random());
    }

    public Pozo(long seed) {
        inicializar(new Random(seed));
    }

    private void inicializar(Random random) {
        fichas.clear();
        Color[] colores = {Color.NEGRO, Color.AZUL, Color.ROJO, Color.AMARILLO};
        
        for (int i = 0; i < 2; i++) {
            for (Color c : colores) {
                for (int v = 1; v <= 13; v++) {
                    fichas.add(new Ficha(v, c, false));
                }
            }
        }
        fichas.add(new Ficha(0, Color.COMODIN, true));
        fichas.add(new Ficha(0, Color.COMODIN, true));

        Collections.shuffle(fichas, random);
    }

    public Ficha robar() {
        if (fichas.isEmpty()) return null;
        return fichas.remove(fichas.size() - 1);
    }

    public int cantidadRestante() {
        return fichas.size();
    }
}