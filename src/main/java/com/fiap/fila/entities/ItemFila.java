package com.fiap.fila.entities;

import com.fiap.fila.utils.enums.StatusPedido;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemFila {
    private UUID idFila;
    private UUID idCliente;
    private UUID idPedido;
    private Long numeroNaFila;
    private StatusPedido statusPedido;
}