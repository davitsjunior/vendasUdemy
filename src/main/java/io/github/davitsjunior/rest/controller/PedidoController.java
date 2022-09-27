package io.github.davitsjunior.rest.controller;

import io.github.davitsjunior.domain.entity.ItemPedido;
import io.github.davitsjunior.domain.entity.Pedido;
import io.github.davitsjunior.domain.enums.StatusPedido;
import io.github.davitsjunior.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.davitsjunior.rest.dto.InformacaoItemPedidoDTO;
import io.github.davitsjunior.rest.dto.InformacoesPedidosDTO;
import io.github.davitsjunior.rest.dto.PedidoDTO;
import io.github.davitsjunior.service.PedidoService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {


    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }
    @GetMapping("{id}")
   public InformacoesPedidosDTO getById(@PathVariable Integer id){
        return service.obeterPedidoCompleto(id)
                .map(p -> converter(p) )
                .orElseThrow(()->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado!"));
   }

   @PatchMapping("{id}")
   @ResponseStatus(NO_CONTENT)
   public void updateStatus(@PathVariable Integer id,
                            @RequestBody AtualizacaoStatusPedidoDTO dto){

        String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));

   }

   private InformacoesPedidosDTO converter(Pedido pedido){
        return InformacoesPedidosDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens){
        if (CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }

        return itens.stream().map(
                item -> InformacaoItemPedidoDTO
                        .builder().descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }
}
