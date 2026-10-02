package com.rummikub.domain;

public class Ficha {
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
    public String toString() {
        return esComodin ? "[JOKER]" : "[" + color + " " + valor + "]";
    }
}