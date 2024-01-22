package com.fiap.fila.gateways;

import com.fiap.fila.entities.ItemFila;
import com.fiap.fila.gateways.entities.FilaEntity;
import com.fiap.fila.interfaces.gateways.IFilaRepositoryPort;
import com.fiap.fila.interfaces.repositories.FilaRepository;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.fiap.fila.util.FilaHelper.gerarItemfila;

class FilaRepositoryAdapterTest {

    private IFilaRepositoryPort filaRepository;
    @Mock
    private FilaRepository repository;
    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        filaRepository = new FilaRepositoryAdapter(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve inserir ItemFila no repository")
    void deveInserir() {
        var item = gerarItemfila();
        when(repository.save(any(FilaEntity.class))).thenAnswer((i) -> i.getArgument(0));

        var itemInserido = filaRepository.inserir(item);

        assertThat(itemInserido).isNotNull();
        assertThat(itemInserido).isInstanceOf(ItemFila.class);
        assertThat(itemInserido.getIdCliente()).isEqualTo(item.getIdCliente());
        assertThat(itemInserido.getIdPedido()).isEqualTo(item.getIdPedido());
        verify(repository, times(1)).save(any(FilaEntity.class));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve remover ItemFila no repository")
    void deveRemover() {
        filaRepository.removerItemFila(UUID.randomUUID());

        verify(repository, times(1)).deleteByIdPedido(any(UUID.class));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve obter pedido na fila utilizando id do peiddo")
    void deveObterPorIdPedido() {
        var item = gerarItemfila();
        when(repository.findByIdPedido(any(UUID.class))).thenReturn(Optional.of(new FilaEntity(item)));

        var itemObtido = filaRepository.obterPorIdPedido(item.getIdPedido());

        assertThat(itemObtido).isNotNull();
        assertThat(itemObtido).isPresent();
        assertThat(itemObtido.get().getIdCliente()).isEqualTo(item.getIdCliente());
        assertThat(itemObtido.get().getIdPedido()).isEqualTo(item.getIdPedido());

        verify(repository, times(1)).findByIdPedido(any(UUID.class));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve atualizar ItemFila no repository")
    void deveAtualizarItemNaFila() {
        var item = gerarItemfila();
        when(repository.save(any(FilaEntity.class))).thenAnswer((i) -> i.getArgument(0));

        var itemAtualizado = filaRepository.atualizarItemNaFila(item);

        assertThat(itemAtualizado).isNotNull();
        assertThat(itemAtualizado).isInstanceOf(ItemFila.class);
        assertThat(itemAtualizado.getIdCliente()).isEqualTo(item.getIdCliente());
        assertThat(itemAtualizado.getIdPedido()).isEqualTo(item.getIdPedido());
        verify(repository, times(1)).save(any(FilaEntity.class));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve obter itens na fila")
    void deveObterPedidos() {
        var item01 = gerarItemfila();
        var testConstructor = new FilaEntity(item01.getNumeroNaFila(), item01.getIdCliente(), item01.getIdPedido());

        var item02 = gerarItemfila();
        var testSetters = new FilaEntity();
        testSetters.setIdPedido(item02.getIdPedido());
        testSetters.setIdCliente(item02.getIdCliente());
        testSetters.setNumeroNaFila(item02.getNumeroNaFila());

        var item03 = gerarItemfila();
        var testBuilder = FilaEntity.builder().idPedido(item03.getIdPedido()).idCliente(item03.getIdCliente()).numeroNaFila(item03.getNumeroNaFila()).build();

        var itens = List.of(testConstructor, testSetters, testBuilder, new FilaEntity(gerarItemfila()), new FilaEntity(gerarItemfila()));

        when(repository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<FilaEntity>(itens));

        var itensObtidos = filaRepository.obterPedidos(0, 10);

        assertThat(itensObtidos).isNotNull();
        assertThat(itensObtidos).asList();
        assertThat(itensObtidos).hasSize(5);

        verify(repository, times(1)).findAll(any(PageRequest.class));
    }
}
