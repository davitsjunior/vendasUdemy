package io.github.davitsjunior.service.impl;

import io.github.davitsjunior.domain.entity.Cliente;
import io.github.davitsjunior.domain.entity.ItemPedido;
import io.github.davitsjunior.domain.entity.Pedido;
import io.github.davitsjunior.domain.entity.Produto;
import io.github.davitsjunior.domain.enums.StatusPedido;
import io.github.davitsjunior.domain.repository.Clientes;
import io.github.davitsjunior.domain.repository.ItensPedido;
import io.github.davitsjunior.domain.repository.Pedidos;
import io.github.davitsjunior.domain.repository.Produtos;
import io.github.davitsjunior.exception.PedidoNaoEncontradoException;
import io.github.davitsjunior.exception.RegraNegocioException;
import io.github.davitsjunior.rest.dto.ItemPedidoDTO;
import io.github.davitsjunior.rest.dto.PedidoDTO;
import io.github.davitsjunior.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;



    @Override
    @Transactional //caso dê erro em algum método no bd, é feio o rollback
    public Pedido salvar(PedidoDTO dto) {

        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository.findById(idCliente).orElseThrow(()->
                new RegraNegocioException("Código de Cliente Inválido!"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedidos = converterItens(pedido, dto.getItens());
        repository.save(pedido);
        itensPedidoRepository.saveAll(itensPedidos);
        pedido.setItens(itensPedidos);
        return pedido;
    }

    @Override
    public Optional<Pedido> obeterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(PedidoNaoEncontradoException::new);
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens){
        if (itens.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens!");
        }

        return itens
                .stream()
                .map(dto ->{
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("Produto não encontrado. COD: " + idProduto));
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
