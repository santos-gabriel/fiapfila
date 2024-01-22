package com.fiap.fila.controllers;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.fiap.fila.util.FilaHelper.gerarItemfila;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FilaControllerTest {

    private MockMvc mockMvc;
    @Mock
    private IFilaUseCasePort filaUseCasePort;
    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        FilaController filaController = new FilaController(filaUseCasePort);
        mockMvc = MockMvcBuilders.standaloneSetup(filaController).addFilter((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        })
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve inserir pedido na fila")
    void deveInserirPedidoNaFila() throws Exception {
        var idPedido = UUID.randomUUID();
        var idCliente = UUID.randomUUID();
        when(filaUseCasePort.inserirPedidoNaFila(any(ItemFila.class))).thenAnswer((i) -> i.getArgument(0));

        mockMvc.perform(post("/fila/clientes/{idCliente}/pedidos/{idPedido}", idCliente, idPedido))
                .andExpect(status().isOk());

        verify(filaUseCasePort, times(1)).inserirPedidoNaFila(any(ItemFila.class));
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve concluir pedido na fila")
    void deveConcluirPedidoNaFila() throws Exception  {
        var idPedido = UUID.randomUUID();

        mockMvc.perform(put("/fila/{id}", idPedido)).andExpect(status().isOk());

        verify(filaUseCasePort, times(1)).concluirPedidoNaFila(any(UUID.class));
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve remover pedido na fila")
    void deveRemoverPedidoNaFila()  throws Exception  {
        var idPedido = UUID.randomUUID();

        mockMvc.perform(delete("/fila/{id}", idPedido)).andExpect(status().isOk());

        verify(filaUseCasePort, times(1)).removerPedidoNaFila(any(UUID.class));
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve obter pedido na fila")
    void deveObterPedidoNaFila() throws Exception {
        var idPedido = UUID.randomUUID();
        when(filaUseCasePort.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.of(gerarItemfila()));

        mockMvc.perform(get("/fila/{id}", idPedido))
                .andExpect(status().isOk());

        verify(filaUseCasePort, times(1)).obterPedidoNaFila(any(UUID.class));
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve obter os pedidos na fila")
    void deveObterPedidosNaFila() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        mockMvc.perform(
                get("/fila/pedidos")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))

                )
                .andExpect(status().isOk());

        verify(filaUseCasePort, times(1)).obterPedidosNaFila(0, 10);
    }

}
