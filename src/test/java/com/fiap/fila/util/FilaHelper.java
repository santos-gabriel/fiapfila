package com.fiap.fila.util;

import com.fiap.fila.entities.ItemFila;

import java.util.UUID;

public abstract class FilaHelper {
    public static ItemFila gerarItemfila() {
        return ItemFila.builder()
                .idFila(UUID.randomUUID())
                .idCliente(UUID.randomUUID())
                .idPedido(UUID.randomUUID())
                .numeroNaFila(1L)
                .build();
    }
}
