package io.github.davitsjunior.domain.repository;

import io.github.davitsjunior.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}
