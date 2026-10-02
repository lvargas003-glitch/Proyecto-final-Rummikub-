package com.rummikub.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrupoMeld extends Meld {

    public GrupoMeld(List<Ficha> fichas) {
        super(fichas);
    }

    @Override
    public boolean esValido() {
        if (fichas == null || fichas.size() < 3 || fichas.size() > 4) return false;
        
        int valorObjetivo = -1;
        Set<Color> colores = new HashSet<>();

        for (Ficha f : fichas) {
            if (f.esComodin()) continue;
            
            if (valorObjetivo == -1) {
                valorObjetivo = f.getValor();
            } else if (f.getValor() != valorObjetivo) {
                return false;
            }

            if (colores.contains(f.getColor())) {
                return false;
            }
            colores.add(f.getColor());
        }
        return true;
    }

    @Override
    public int calcularPuntos() {
        return fichas.stream().mapToInt(Ficha::getValor).sum();
    }
}