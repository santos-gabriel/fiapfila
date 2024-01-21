# language: pt
Funcionalidade: Fila
  Cenario: inserir pedido na fila
    Quando inserir pedido na fila
    Entao o pedido e inserido com sucesso
    E deve ser apresentado

  Cenario: concluir pedido na fila
    Dado que um pedido foi inserido na fila
    Quando concluir um pedido
    Entao o pedido e concluido na fila

  Cenario: remover um pedido da fila
    Dado que um pedido foi inserido na fila
    Quando remover um pedido
    Entao o pedido deve ser removido da fila

  Cenario: obter pedido na fila
    Dado que um pedido foi inserido na fila
    Quando obter um pedido na fila
    Entao o pedido deve ser obtido
    E deve ser apresentado

  Cenario: buscar pedidos na fila
    Quando buscar pedidos na fila
    Entao deve buscar os pedidos
    E os pedidos devem ser apresentados