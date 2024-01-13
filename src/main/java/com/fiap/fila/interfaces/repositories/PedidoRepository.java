package com.fiap.fila.interfaces.repositories;

import com.fiap.fila.utils.enums.StatusPedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "consulta-pagamento", url = "http://localhost:4000")
public interface PedidoRepository {
    @PutMapping(value = "/pedido/{idPedido}/status/{status}", headers = {"Content-Type: application/json"})
//    @Headers("Content-Type: application/json")
    void atualizarPedido(@PathVariable("idPedido") UUID idPedido, @PathVariable("status")StatusPedido statusPedido);
}