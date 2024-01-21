package com.fiap.fila.interfaces.gateways;

import com.fiap.fila.utils.enums.StatusPedido;

import java.util.UUID;

public interface IPedidoRepositoryPort {
    void atualizarPedido(UUID idPedido, StatusPedido statusPedido) throws Exception;
}
