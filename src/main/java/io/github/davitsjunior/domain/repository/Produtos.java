package io.github.davitsjunior.domain.repository;

import io.github.davitsjunior.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
