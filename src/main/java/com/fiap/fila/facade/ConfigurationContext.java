package com.fiap.fila.facade;

import com.fiap.fila.interfaces.gateways.IFilaRepositoryPort;
import com.fiap.fila.interfaces.gateways.IPedidoRepositoryPort;
import com.fiap.fila.interfaces.usecases.IFilaUseCasePort;
import com.fiap.fila.usecases.FilaUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationContext {
    @Bean
    public IFilaUseCasePort ifilaUseCasePort(IFilaRepositoryPort filaRepositoryPort, IPedidoRepositoryPort pedidoRepositoryPort) {
        return new FilaUseCaseImpl(filaRepositoryPort, pedidoRepositoryPort);
    }

}
