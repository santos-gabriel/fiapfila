package com.fiap.fila.exceptions.handlers;

import com.fiap.fila.controllers.FilaController;
import com.fiap.fila.exceptions.entities.ErroAoAtualizarStatusDoPedidoException;
import com.fiap.fila.exceptions.entities.PedidoJaNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoEncontradoNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoIncluidoNaFilaException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {FilaController.class})
public class FilaExceptionHandler {
    @ExceptionHandler(ErroAoAtualizarStatusDoPedidoException.class)
    public ResponseEntity<StandardError> erroAoAtualizarStatusDoPedidoException(ErroAoAtualizarStatusDoPedidoException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(PedidoJaNaFilaException.class)
    public ResponseEntity<StandardError> pedidoJaNaFilaException(PedidoJaNaFilaException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "O pedido já está na fila", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(PedidoNaoIncluidoNaFilaException.class)
    public ResponseEntity<StandardError> pedidoNaoIncluidoNaFilaException(PedidoNaoIncluidoNaFilaException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(PedidoNaoEncontradoNaFilaException.class)
    public ResponseEntity<StandardError> pedidoNaoEncontradoNaFilaException(PedidoNaoEncontradoNaFilaException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
