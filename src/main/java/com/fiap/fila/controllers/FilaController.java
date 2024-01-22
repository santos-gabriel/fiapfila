package com.fiap.fila.controllers;

import com.fiap.fila.adapters.FilaDTO;
import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fila")
@RequiredArgsConstructor
public class FilaController {

    @Autowired
    private final IFilaUseCasePort filaUseCasePort;

    @PostMapping("/clientes/{idCliente}/pedidos/{idPedido}")
    public ResponseEntity<FilaDTO> inserirPedidoNaFila(
            @PathVariable(name = "idPedido") UUID idPedido,
            @PathVariable(name = "idCliente") UUID idCliente) {

        var itemFila = ItemFila.builder()
                .idPedido(idPedido)
                .idCliente(idCliente)
                .build();
        var filaDTO = new FilaDTO().from(filaUseCasePort.inserirPedidoNaFila(itemFila));
        return ResponseEntity.ok().body(filaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> concluirPedidoNaFila(@PathVariable(name = "id") UUID idPedido) {
        filaUseCasePort.concluirPedidoNaFila(idPedido);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerPedidoNaFila(@PathVariable(name = "id") UUID idPedido) {
        filaUseCasePort.removerPedidoNaFila(idPedido);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilaDTO> obterPedidoNaFila(@PathVariable(name = "id") UUID idPedido) {
        var iTemFila = filaUseCasePort.obterPedidoNaFila(idPedido);

        if (iTemFila.isPresent()) {
            var filaDTO = new FilaDTO().from(iTemFila.get());
            return ResponseEntity.ok().body(filaDTO);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<FilaDTO>> buscarPedidosNaFila(
            @PageableDefault(size = 10, page = 0)
            @SortDefault(sort = "numeroNaFila", direction = Sort.Direction.ASC)
            Pageable paginacao) {
        var itemFila = filaUseCasePort.obterPedidosNaFila(paginacao.getPageNumber(), paginacao.getPageSize());
        var itensFilaDTO = itemFila
                .stream()
                .map(item -> new FilaDTO().from(item))
                .collect(Collectors.toList());
        return new ResponseEntity<>(itensFilaDTO, HttpStatus.OK);
    }

}
