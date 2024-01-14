package com.fiap.fila.gateways.entities;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.utils.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fila")
public class FilaEntity {

    @Id
    @GeneratedValue
    private Long numeroNaFila;

    @NotNull
    private UUID idPedido;

    @NotNull
    private UUID idCliente;

    public FilaEntity(ItemFila pedidoFila) {
        this.idPedido = pedidoFila.getIdPedido();
        this.idCliente = pedidoFila.getIdCliente();
    }

    public ItemFila toItemFila() {
        var pedidoFila = new ItemFila();
        pedidoFila.setNumeroNaFila(this.numeroNaFila);
        pedidoFila.setIdCliente(this.idCliente);
        pedidoFila.setIdPedido(this.idPedido);
        return pedidoFila;
    }
}
