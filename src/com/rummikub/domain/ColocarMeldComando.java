package com.rummikub.domain;

public class ColocarMeldComando implements IComando {
    private final Mesa mesa;
    private final Meld meld;

    public ColocarMeldComando(Mesa mesa, Meld meld) {
        this.mesa = mesa;
        this.meld = meld;
    }

    @Override
    public void ejecutar() {
        mesa.agregarMeld(meld);
    }
}