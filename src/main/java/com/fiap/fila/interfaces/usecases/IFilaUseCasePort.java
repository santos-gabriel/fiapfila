package com.fiap.fila.interfaces.usecases;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.exceptions.entities.PedidoJaNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoEncontradoNaFilaException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFilaUseCasePort {

    public ItemFila inserirPedidoNaFila(ItemFila fila) throws PedidoJaNaFilaException;

    public Optional<ItemFila> obterPedidoNaFila(UUID id);

    public void concluirPedidoNaFila(UUID id);

    public List<ItemFila> obterPedidosNaFila(int page, int size);

}
