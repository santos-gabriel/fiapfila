package com.fiap.fila.interfaces.repositories;

import com.fiap.fila.gateways.entities.FilaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilaRepository extends JpaRepository<FilaEntity, Long> {
    Optional<FilaEntity> findByIdPedido(UUID idPedido);

    void deleteByIdPedido(UUID idPedido);
}
