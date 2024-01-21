package com.fiap.fila.exceptions.handlers;

import com.fiap.fila.exceptions.entities.ErroAoAtualizarStatusDoPedidoException;
import com.fiap.fila.exceptions.entities.PedidoJaNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoEncontradoNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoIncluidoNaFilaException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.fiap.fila.util.FilaHelper.gerarItemfila;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FilaExceptionHandlerTest {

    private FilaExceptionHandler handler = new FilaExceptionHandler();

    @Test
    void deveGerarExcecao_QuandoAtualizarStatusDoPedido() {
        var exception = new ErroAoAtualizarStatusDoPedidoException(UUID.randomUUID());
        var req = new MockHttpServletRequest();
        var res = handler.erroAoAtualizarStatusDoPedidoException(exception, req);

        assertThat(res).isInstanceOf(ResponseEntity.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Test
    void deveGerarExcecao_QuandoPedidoJaNaFila() {
        var exception = new PedidoJaNaFilaException(UUID.randomUUID());
        var req = new MockHttpServletRequest();
        var res = handler.pedidoJaNaFilaException(exception, req);

        assertThat(res).isInstanceOf(ResponseEntity.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void deveGerarExcecao_QuandoPedidoNaoIncluidoNaFila() {
        var exception = new PedidoNaoIncluidoNaFilaException(UUID.randomUUID());
        var req = new MockHttpServletRequest();
        var res = handler.pedidoNaoIncluidoNaFilaException(exception, req);

        assertThat(res).isInstanceOf(ResponseEntity.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Test
    void deveGerarExcecao_QuandoPedidoNaoEncontradoNaFila() {
        var exception = new PedidoNaoEncontradoNaFilaException();
        var req = new MockHttpServletRequest();
        var res = handler.pedidoNaoEncontradoNaFilaException(exception, req);

        assertThat(res).isInstanceOf(ResponseEntity.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
