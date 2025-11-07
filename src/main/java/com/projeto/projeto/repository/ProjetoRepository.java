package com.projeto.projeto.repository;


import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.StatusDoProjeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjetoRepository extends JpaRepository<ProjetoEntity, Long> {
    @Query("SELECT COUNT(DISTINCT p) FROM ProjetoEntity p JOIN p.alocacoes a " +
            "WHERE a.membro.id = :membroId AND p.statusDoProjeto NOT IN ('ENCERRADO', 'CANCELADO')")
    long countProjetosAtivosByMembroId(Long membroId);


    Page<ProjetoEntity> findAll(Pageable pageable);

    Page<ProjetoEntity> findByStatusDoProjeto(StatusDoProjeto status, Pageable pageable);

    Page<ProjetoEntity> findByNomeContainingIgnoreCase(String nome, Pageable pageable);


    Page<ProjetoEntity> findByStatusDoProjetoAndNomeContainingIgnoreCase(
            StatusDoProjeto status, String nome, Pageable pageable);

}
