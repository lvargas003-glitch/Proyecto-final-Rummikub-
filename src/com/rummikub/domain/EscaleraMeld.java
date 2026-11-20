package com.rummikub.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EscaleraMeld extends Meld {

    public EscaleraMeld(List<Ficha> fichas) {
        super(fichas);
    }

    @Override
    public boolean esValido() {
        if (fichas == null || fichas.size() < 3) return false;

        Color colorObjetivo = null;
        List<Ficha> normales = new ArrayList<>();
        int comodines = 0;

        for (Ficha f : fichas) {
            if (f.esComodin()) {
                comodines++;
            } else {
                if (colorObjetivo == null) {
                    colorObjetivo = f.getColor();
                } else if (f.getColor() != colorObjetivo) {
                    return false;
                }
                normales.add(f);
            }
        }

        if (normales.isEmpty()) return false;
        normales.sort(Comparator.comparingInt(Ficha::getValor));

        int comodinesDisponibles = comodines;
        for (int i = 1; i < normales.size(); i++) {
            int diff = normales.get(i).getValor() - normales.get(i - 1).getValor();
            if (diff <= 0) return false; // duplicados o desorden
            int huecos = diff - 1;
            if (huecos > comodinesDisponibles) {
                return false;
            }
            comodinesDisponibles -= huecos;
        }

        int minValor = normales.get(0).getValor() - comodinesDisponibles;
        int maxValor = normales.get(normales.size() - 1).getValor() + comodinesDisponibles;
        return minValor >= 1 && maxValor <= 13;
    }

    @Override
    public int calcularPuntos() {
        List<Ficha> normales = fichas.stream()
                .filter(f -> !f.esComodin())
                .sorted(Comparator.comparingInt(Ficha::getValor))
                .toList();
        if (normales.isEmpty()) return 0;

        int suma = normales.stream().mapToInt(Ficha::getValor).sum();
        int comodines = (int) fichas.stream().filter(Ficha::esComodin).count();
        int siguiente = normales.get(normales.size() - 1).getValor() + 1;

        for (int i = 0; i < comodines; i++) {
            if (siguiente <= 13) {
                suma += siguiente++;
            }
        }
        return suma;
    }
}