package com.projeto.projeto.repository;

import com.projeto.projeto.entity.ProjetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlocacaoEntity extends JpaRepository<ProjetoEntity,Long> {
}
