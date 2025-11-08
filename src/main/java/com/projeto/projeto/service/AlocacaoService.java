package com.projeto.projeto.service;

import com.projeto.projeto.entity.AlocacaoEntity;
import com.projeto.projeto.entity.MembroEntity;
import com.projeto.projeto.repository.AlocacaoRepository;
import com.projeto.projeto.repository.MembroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlocacaoService {

    private final AlocacaoRepository alocacaoRepository;
    private final MembroRepository membroRepository;

    public List<AlocacaoEntity> listarTodas() {
        return alocacaoRepository.findAll();
    }

    public AlocacaoEntity buscarPorId(Long id) {
        return alocacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alocação com ID " + id + " não encontrada."));
    }

    public AlocacaoEntity criarAlocacao(AlocacaoEntity alocacao) {
        if (alocacao.getMembro() == null || alocacao.getMembro().getId() == null) {
            throw new RuntimeException("O campo 'membro.id' é obrigatório.");
        }

        MembroEntity membro = membroRepository.findById(alocacao.getMembro().getId())
                .orElseThrow(() -> new RuntimeException("Membro com ID " + alocacao.getMembro().getId() + " não encontrado."));

        alocacao.setMembro(membro);

        if (alocacao.getDataAlocacao() == null) {
            alocacao.setDataAlocacao(LocalDate.now());
        }

        return alocacaoRepository.save(alocacao);
    }

    public void deletar(Long id) {
        if (!alocacaoRepository.existsById(id)) {
            throw new RuntimeException("Alocação com ID " + id + " não existe.");
        }
        alocacaoRepository.deleteById(id);
    }
}
