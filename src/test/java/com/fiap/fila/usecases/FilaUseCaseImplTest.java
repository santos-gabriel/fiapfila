package com.fiap.fila.usecases;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.exceptions.entities.ErroAoAtualizarStatusDoPedidoException;
import com.fiap.fila.exceptions.entities.PedidoJaNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoEncontradoNaFilaException;
import com.fiap.fila.exceptions.entities.PedidoNaoIncluidoNaFilaException;
import com.fiap.fila.gateways.PedidoRepositoryAdapter;
import com.fiap.fila.interfaces.gateways.IFilaRepositoryPort;
import com.fiap.fila.interfaces.gateways.IPedidoRepositoryPort;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;
import com.fiap.fila.utils.enums.StatusPedido;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.fiap.fila.util.FilaHelper.gerarItemfila;

class FilaUseCaseImplTest {

    private IFilaUseCasePort filaUseCase;
    @Mock
    private IFilaRepositoryPort filaRepository;
    @Mock
    private IPedidoRepositoryPort pedidoRepository;
    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        filaUseCase = new FilaUseCaseImpl(filaRepository, pedidoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve obter os pedidos na fila")
    void deveObterPedidosNaFila() {
        var pedidos = filaUseCase.obterPedidosNaFila(0, 10);

        assertThat(pedidos).isNotNull();
        verify(filaRepository, times(1)).obterPedidos(0, 10);
    }

    @Nested
    class InserirPedidoNaFila {
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Inserir pedido na fila")
        void deveInserirPedidoNaFila() throws Exception {
            var item = gerarItemfila();
            when(filaRepository.inserir(any(ItemFila.class))).thenAnswer((i) -> i.getArgument(0));

            var itemInserido = filaUseCase.inserirPedidoNaFila(item);

            assertThat(itemInserido).isNotNull();
            assertThat(itemInserido).isInstanceOf(ItemFila.class);
            assertThat(itemInserido.getIdPedido()).isEqualTo(item.getIdPedido());
            assertThat(itemInserido.getIdCliente()).isEqualTo(item.getIdCliente());

            verify(filaRepository, times(1)).inserir(any(ItemFila.class));
            verify(pedidoRepository, times(1)).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Gerar uma excecao ao inserir pedido na fila e o pedido ja estiver na fila")
        void deveGerarExcecao_QuandoInserirPedidoNaFila_PedidoJaNaFila() throws Exception {
            var item = gerarItemfila();
            when(filaUseCase.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.of(item));

            assertThatThrownBy(() -> filaUseCase.inserirPedidoNaFila(item))
                    .isInstanceOf(PedidoJaNaFilaException.class)
                    .hasMessage("Pedido com ID: " + item.getIdPedido() + " já está na fila.");

            verify(filaRepository, never()).inserir(any(ItemFila.class));
            verify(pedidoRepository, never()).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Gerar uma excecao ao inserir pedido na fila e o pedido e nao conseguir atualizar status do pedido")
        void deveGerarExcecao_QuandoInserirPedidoNaFila_PedidoNaoIncluidoNaFila() throws Exception {
            var item = gerarItemfila();
            when(filaRepository.inserir(any(ItemFila.class))).thenAnswer((i) -> i.getArgument(0));

            doThrow(new Exception()).when(pedidoRepository).atualizarPedido(isA(UUID.class), isA(StatusPedido.class));

            assertThatThrownBy(() -> filaUseCase.inserirPedidoNaFila(item))
                    .isInstanceOf(PedidoNaoIncluidoNaFilaException.class)
                    .hasMessage("Ocorreu um erro ao incluir o pedido: " + item.getIdPedido() + " na fila.");

            verify(filaRepository, times(1)).inserir(any(ItemFila.class));
            verify(filaRepository, times(1)).removerItemFila(any(UUID.class));
            verify(pedidoRepository, times(1)).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
    }

    @Nested
    class ConcluirPedidoNaFila {
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Deve concluir pedido na fila")
        void deveConcluirPedidoNaFila() throws Exception {
            var item = gerarItemfila();
            when(filaUseCase.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.of(item));

            filaUseCase.concluirPedidoNaFila(item.getIdPedido());

            verify(pedidoRepository, times(1)).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Deve gerar excecao ao concluir pedido na fila quando pedido nao for encontrado")
        void deveGerarExcecao_QuandoConcluirPedidoNaFila_PedidoNaoEncontrado() throws Exception {
            var item = gerarItemfila();
            when(filaUseCase.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> filaUseCase.concluirPedidoNaFila(item.getIdPedido()))
                    .isInstanceOf(PedidoNaoEncontradoNaFilaException.class)
                    .hasMessage("Não foi encontrado um pedido com esse número na fila");

            verify(pedidoRepository, never()).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Deve gerar excecao ao concluir pedido na fila quando nao atualizar status do pedido")
        void deveGerarExcecao_QuandoConcluirPedidoNaFila_StatusDoPedidoNaoAtualizado() throws Exception {
            var item = gerarItemfila();
            when(filaUseCase.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.of(item));
            doThrow(new Exception()).when(pedidoRepository).atualizarPedido(isA(UUID.class), isA(StatusPedido.class));

            assertThatThrownBy(() -> filaUseCase.concluirPedidoNaFila(item.getIdPedido()))
                    .isInstanceOf(ErroAoAtualizarStatusDoPedidoException.class)
                    .hasMessage("Ocorreu um erro ao atualizar o status do pedido: " + item.getIdPedido() + ".");

            verify(pedidoRepository, times(1)).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
    }

    @Nested
    class RemoverPedidoNaFila {
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Deve remover pedido na fila")
        void deveRemoverPedidoNaFila() throws Exception {
            var item = gerarItemfila();
            when(filaUseCase.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.of(item));

            filaUseCase.removerPedidoNaFila(item.getIdPedido());

            verify(pedidoRepository, times(1)).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Deve gerar excecao ao remover pedido na fila quando pedido nao for encontrado")
        void deveGerarExcecao_QuandoRemoverPedidoNaFila_PedidoNaoEncontrado() throws Exception {
            var item = gerarItemfila();
            when(filaUseCase.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> filaUseCase.removerPedidoNaFila(item.getIdPedido()))
                    .isInstanceOf(PedidoNaoEncontradoNaFilaException.class)
                    .hasMessage("Não foi encontrado um pedido com esse número na fila");

            verify(pedidoRepository, never()).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Deve gerar excecao ao remover pedido na fila quando nao atualizar status do pedido")
        void deveGerarExcecao_QuandoRemoverPedidoNaFila_StatusDoPedidoNaoAtualizado() throws Exception {
            var item = gerarItemfila();
            when(filaUseCase.obterPedidoNaFila(any(UUID.class))).thenReturn(Optional.of(item));
            doThrow(new Exception()).when(pedidoRepository).atualizarPedido(isA(UUID.class), isA(StatusPedido.class));

            assertThatThrownBy(() -> filaUseCase.removerPedidoNaFila(item.getIdPedido()))
                    .isInstanceOf(ErroAoAtualizarStatusDoPedidoException.class)
                    .hasMessage("Ocorreu um erro ao atualizar o status do pedido: " + item.getIdPedido() + ".");

            verify(pedidoRepository, times(1)).atualizarPedido(any(UUID.class), any(StatusPedido.class));
        }
    }
}
