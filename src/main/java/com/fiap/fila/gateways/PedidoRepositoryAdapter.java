package com.fiap.fila.gateways;

import com.fiap.fila.interfaces.gateways.IPedidoRepositoryPort;
import com.fiap.fila.interfaces.repositories.PedidoRepository;
import com.fiap.fila.utils.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoRepositoryAdapter implements IPedidoRepositoryPort {

    private final PedidoRepository pedidoRepository;

    @Override
    public void atualizarPedido(UUID idPedido, StatusPedido statusPedido) {
        this.pedidoRepository.atualizarPedido(idPedido, statusPedido);
    }
}
