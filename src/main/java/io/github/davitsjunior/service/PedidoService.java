package io.github.davitsjunior.service;

import io.github.davitsjunior.domain.entity.Pedido;
import io.github.davitsjunior.domain.enums.StatusPedido;
import io.github.davitsjunior.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar (PedidoDTO dto);

    Optional<Pedido> obeterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
