package com.fiap.fila.gateways;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.gateways.entities.FilaEntity;
import com.fiap.fila.interfaces.gateways.IFilaRepositoryPort;
import com.fiap.fila.interfaces.repositories.FilaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilaRepositoryAdapter implements IFilaRepositoryPort {

    @Autowired
    private final FilaRepository filaRepository;

    @Override
    @Transactional
    public ItemFila inserir(ItemFila fila) {
        return filaRepository
                .save(new FilaEntity(fila))
                .toItemFila();
    }

    @Override
    @Transactional
    public void removerItemFila(UUID idPedido) {
        filaRepository.deleteByIdPedido(idPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemFila> obterPorIdPedido(UUID idPedido) {
        var pedidoFilaEntity = filaRepository.findByIdPedido(idPedido);
        return pedidoFilaEntity.map(FilaEntity::toItemFila);
    }

    @Override
    @Transactional
    public ItemFila atualizarItemNaFila(ItemFila itemFila) {
        return filaRepository
                .save(new FilaEntity(itemFila))
                .toItemFila();

    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemFila> obterPedidos(int page, int size) {
        return filaRepository.findAll(PageRequest.of(page, size))
                .toList()
                .stream()
                .map(FilaEntity::toItemFila)
                .collect(Collectors.toList());
    }
}
