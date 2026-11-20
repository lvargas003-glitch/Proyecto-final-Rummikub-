package com.rummikub.domain;

import java.util.Objects;

public class Ficha implements IComponenteTablero {
    private final int valor;
    private final Color color;
    private final boolean esComodin;

    public Ficha(int valor, Color color, boolean esComodin) {
        this.valor = valor;
        this.color = color;
        this.esComodin = esComodin;
    }

    public int getValor() { return valor; }
    public Color getColor() { return color; }
    public boolean esComodin() { return esComodin; }

    @Override
    public int calcularPuntos() {
        return esComodin ? 0 : valor;
    }

    @Override
    public int contarFichas() {
        return 1;
    }

    @Override
    public String toString() {
        return esComodin ? "[JOKER]" : "[" + color + " " + valor + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ficha ficha)) return false;
        return valor == ficha.valor && esComodin == ficha.esComodin && color == ficha.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor, color, esComodin);
    }
}