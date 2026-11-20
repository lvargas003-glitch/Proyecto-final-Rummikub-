package com.rummikub.domain;

public interface IComando {
    boolean ejecutar();
    boolean deshacer();
    String descripcion();
}