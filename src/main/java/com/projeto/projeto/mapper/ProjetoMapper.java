package com.projeto.projeto.mapper;

import com.projeto.projeto.dtos.ProjetoDTO;
import com.projeto.projeto.entity.MembroEntity;
import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.StatusDoProjeto;
import org.springframework.stereotype.Component;

@Component
public class ProjetoMapper {

    public ProjetoDTO toDTO(ProjetoEntity entity) {
        if (entity == null) return null;
        return new ProjetoDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDataInicio(),
                entity.getPrevisaoTermino(),
                entity.getDataRealTermino(),
                entity.getOrcamentoTotal(),
                entity.getDescricao(),
                entity.getGerenteId(),
                entity.getStatusDoProjeto(),
                entity.getClassificacaoRisco()
        );
    }

    public ProjetoEntity toEntity(ProjetoDTO dto) {
        if (dto == null) return null;

        ProjetoEntity entity = new ProjetoEntity();
        entity.setNome(dto.getNome());
        entity.setDataInicio(dto.getDataInicio());
        entity.setPrevisaoTermino(dto.getPrevisaoTermino());
        entity.setDataRealTermino(dto.getDataRealTermino());
        entity.setOrcamentoTotal(dto.getOrcamentoTotal());
        entity.setDescricao(dto.getDescricao());
        entity.setStatusDoProjeto(dto.getStatusDoProjeto() != null ? dto.getStatusDoProjeto() : StatusDoProjeto.EM_ANALISE);
        entity.setGerenteId(dto.getGerenteId());

        return entity;
    }
}
