package com.fiap.fila.interfaces.gateways;

import com.fiap.fila.entities.ItemFila;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFilaRepositoryPort {

    ItemFila inserir(ItemFila fila);

    void removerItemFila(UUID idPedido);

    Optional<ItemFila> obterPorIdPedido(UUID idPedido);

    ItemFila atualizarItemNaFila(ItemFila itemFila);

    List<ItemFila> obterPedidos(int page, int size);
}
