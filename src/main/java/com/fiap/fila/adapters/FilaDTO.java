package com.fiap.fila.adapters;


import com.fiap.fila.entities.ItemFila;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FilaDTO {
    private Long numeroNaFila;
    private UUID idPedido;
    private UUID idCliente;

    public FilaDTO from(ItemFila fila) {
        return FilaDTO.builder()
                .numeroNaFila(fila.getNumeroNaFila())
                .idPedido(fila.getIdPedido())
                .idCliente(fila.getIdCliente())
                .build();
    }
}
