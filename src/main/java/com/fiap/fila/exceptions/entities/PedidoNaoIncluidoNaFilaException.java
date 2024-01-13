package com.fiap.fila.exceptions.entities;

import java.util.UUID;

public class PedidoNaoIncluidoNaFilaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PedidoNaoIncluidoNaFilaException(UUID idPedido) {
        super("Ocorreu um erro ao incluir o pedido: " + idPedido + " na fila.");
    }
}
