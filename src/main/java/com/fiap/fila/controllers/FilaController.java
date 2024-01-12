package com.fiap.fila.controllers;

import com.fiap.fila.adapters.FilaDTO;
import com.fiap.fila.controllers.requestValidations.FilaRequest;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;
import jakarta.validation.constraints.NotNull;
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

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tech-challenge")
@RequiredArgsConstructor
public class FilaController {

    @Autowired
    private IFilaUseCasePort filaUseCasePort;

    @PostMapping("/fila")
    public ResponseEntity<FilaDTO> inserirPedidoNaFila(@RequestBody @NotNull FilaRequest filaRequest) {
        var filaDTO = new FilaDTO().from(filaUseCasePort.inserirPedidoNaFila(filaRequest.toItemFila()));
        return ResponseEntity.ok().body(filaDTO);
    }

    @PutMapping("/fila/{id}")
    public ResponseEntity<FilaDTO> atualizarPedidoNaFila(@PathVariable(name = "id") UUID idPedido) {
        var filaDTO = new FilaDTO().from(filaUseCasePort.atualizarPedidoNaFilaPorIdPedido(idPedido));
        return ResponseEntity.ok().body(filaDTO);
    }

    @GetMapping("/fila/{id}")
    public ResponseEntity<FilaDTO> buscarPedidoNaFilaPorID(@PathVariable(name = "id") UUID idPedido) {
        var iTemFila = filaUseCasePort.obterPedidoNaFilaPorIdPedido(idPedido);

        if (iTemFila.isPresent()) {
            var filaDTO = new FilaDTO().from(iTemFila.get());
            return ResponseEntity.ok().body(filaDTO);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/fila/pedidos")
    public Page<FilaDTO> buscarPedidosNaFila(
            @PageableDefault(size = 10, page = 0)
            @SortDefault(sort = "numeroNaFila", direction = Sort.Direction.ASC)
            Pageable paginacao) {
        var itemFila = filaUseCasePort.obterPedidosNaFila(paginacao.getPageNumber(), paginacao.getPageSize());
        var itensFilaDTO = itemFila
                .stream()
                .map(item -> new FilaDTO().from(item))
                .collect(Collectors.toList());
        return new PageImpl<>(itensFilaDTO);
    }

    @DeleteMapping("/fila/{id}")
    public ResponseEntity<?> removerPedidoDaFila(@PathVariable(name = "id") UUID idPedido) {
        filaUseCasePort.removerPedidoDaFila(idPedido);
        return ResponseEntity.ok().build();
    }

}
