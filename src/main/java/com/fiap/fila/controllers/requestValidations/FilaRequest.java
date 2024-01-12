package com.fiap.fila.controllers.requestValidations;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.utils.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilaRequest {

    @NotNull(message = "id do pedido não pode estar vazio")
    private UUID idPedido;
    private UUID idCliente;
    private StatusPedido status;

    public ItemFila toItemFila() {
        return ItemFila.builder()
                .idCliente(this.idCliente)
                .idPedido(this.idPedido)
                .statusPedido(this.status)
                .build();

    }
}
