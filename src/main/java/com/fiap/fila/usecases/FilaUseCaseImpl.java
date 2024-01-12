package com.fiap.fila.usecases;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.exceptions.entities.PedidoJaNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoEncontradoNaFilaException;
import com.fiap.fila.interfaces.gateways.IFilaRepositoryPort;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class FilaUseCaseImpl implements IFilaUseCasePort {

    private final IFilaRepositoryPort filaRepositoryPort;

    public FilaUseCaseImpl(IFilaRepositoryPort filaRepositoryPort) {
        this.filaRepositoryPort = filaRepositoryPort;
    }

    @Override
    public ItemFila inserirPedidoNaFila(ItemFila fila) throws PedidoJaNaFilaException {

        if (obterPedidoNaFilaPorIdPedido(fila.getIdPedido()).isPresent())
            throw new PedidoJaNaFilaException(fila.getIdPedido());
        return filaRepositoryPort.inserir(fila);
        // TODO - mudar status do pedido
    }

    @Override
    public Optional<ItemFila> obterPedidoNaFilaPorIdPedido(UUID idPedido) {
        return filaRepositoryPort.obterPorIdPedido(idPedido);
    }

    @Override
    public ItemFila atualizarPedidoNaFilaPorIdPedido(UUID id) {
        var pedidoFilaOptional = this.obterPedidoNaFilaPorIdPedido(id);
        if (pedidoFilaOptional.isEmpty()) throw new PedidoNaoEncontradoNaFilaException();
        return filaRepositoryPort.atualizarItemNaFila(pedidoFilaOptional.get());
        // TODO - mudar status do pedido
    }

    @Override
    public List<ItemFila> obterPedidosNaFila(int page, int size) {
        return filaRepositoryPort.obterPedidos(page, size);
    }

    @Override
    public void removerPedidoDaFila(UUID idPedido) throws PedidoNaoEncontradoNaFilaException {
        var pedidoFilaOptional = this.obterPedidoNaFilaPorIdPedido(idPedido);
        if (pedidoFilaOptional.isEmpty()) throw new PedidoNaoEncontradoNaFilaException();
        this.filaRepositoryPort.removerItemFila(idPedido);
        // TODO - mudar status do pedido
    }
}
