package com.fiap.fila.usecases;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.exceptions.entities.ErroAoAtualizarStatusDoPedidoException;
import com.fiap.fila.exceptions.entities.PedidoJaNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoEncontradoNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoIncluidoNaFilaException;
import com.fiap.fila.interfaces.gateways.IFilaRepositoryPort;
import com.fiap.fila.interfaces.gateways.IPedidoRepositoryPort;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;
import com.fiap.fila.utils.enums.StatusPedido;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class FilaUseCaseImpl implements IFilaUseCasePort {

    private final IFilaRepositoryPort filaRepositoryPort;
    private final IPedidoRepositoryPort pedidoRepositoryPort;

    @Override
    public ItemFila inserirPedidoNaFila(ItemFila fila) throws PedidoJaNaFilaException {
        if (this.obterPedidoNaFila(fila.getIdPedido()).isPresent())
            throw new PedidoJaNaFilaException(fila.getIdPedido());

        var itemFila = filaRepositoryPort.inserir(fila);

        try {
            this.pedidoRepositoryPort.atualizarPedido(fila.getIdPedido(), StatusPedido.E);
            return itemFila;
        } catch (Exception e) {
            this.filaRepositoryPort.removerItemFila(fila.getIdPedido());
            throw new PedidoNaoIncluidoNaFilaException(fila.getIdPedido());
        }
    }

    @Override
    public void concluirPedidoNaFila(UUID idPedido) throws
            PedidoNaoEncontradoNaFilaException,
            ErroAoAtualizarStatusDoPedidoException {
        this.finalizarPedidoNaFila(this.obterPedido(idPedido));
    }

    @Override
    public void removerPedidoDaFila(UUID idPedido) throws
            PedidoNaoEncontradoNaFilaException,
            ErroAoAtualizarStatusDoPedidoException {
        this.finalizarPedidoNaFila(this.obterPedido(idPedido));
    }

    @Override
    public Optional<ItemFila> obterPedidoNaFila(UUID idPedido) {
        return filaRepositoryPort.obterPorIdPedido(idPedido);
    }

    @Override
    public List<ItemFila> obterPedidosNaFila(int page, int size) {
        return filaRepositoryPort.obterPedidos(page, size);
    }

    private ItemFila obterPedido(UUID idPedido) {
        var pedidoFilaOptional = this.obterPedidoNaFila(idPedido);
        if (pedidoFilaOptional.isEmpty()) throw new PedidoNaoEncontradoNaFilaException();
        return pedidoFilaOptional.get();
    }

    private void finalizarPedidoNaFila(ItemFila itemFila) {
        filaRepositoryPort.removerItemFila(itemFila.getIdPedido());
        try {
            this.pedidoRepositoryPort.atualizarPedido(itemFila.getIdPedido(), StatusPedido.F);
        } catch (Exception e) {
            throw new ErroAoAtualizarStatusDoPedidoException(itemFila.getIdPedido());
        }
    }
}
