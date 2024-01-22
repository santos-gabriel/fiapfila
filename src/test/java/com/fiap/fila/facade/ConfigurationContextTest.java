package com.fiap.fila.facade;

import com.fiap.fila.interfaces.gateways.IFilaRepositoryPort;
import com.fiap.fila.interfaces.gateways.IPedidoRepositoryPort;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConfigurationContextTest {

    private ConfigurationContext config = new ConfigurationContext();
    @Mock
    private IFilaRepositoryPort filaRepositoryPort;
    @Mock
    private IPedidoRepositoryPort pedidoRepositoryPort;
    AutoCloseable mock;
    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void deveConfigurarFilaUseCase() {
        var filaUseCase = config.ifilaUseCasePort(filaRepositoryPort, pedidoRepositoryPort);

        assertThat(filaUseCase).isNotNull();
        assertThat(filaUseCase).isInstanceOf(IFilaUseCasePort.class);
    }

}
