package com.fiap.fila.utils.enums;

public enum StatusPedido {
    E("Em preparação"),
    F("Finalizado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
