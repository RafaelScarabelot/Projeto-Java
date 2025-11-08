package com.projeto.projeto.service;

import com.projeto.projeto.dtos.MembroDTO;
import com.projeto.projeto.entity.AlocacaoEntity;
import com.projeto.projeto.entity.MembroEntity;
import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.AtribuicaoMembro;
import com.projeto.projeto.enums.StatusDoProjeto;
import com.projeto.projeto.repository.AlocacaoRepository;
import com.projeto.projeto.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlocacaoService {

    private final AlocacaoRepository alocacaoRepository;
    private final ProjetoRepository projetoRepository;
    private final MembroService membroService;

    public void alocarMembro(Long projetoId, Long membroId) {
        ProjetoEntity projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        if (projeto.getStatusDoProjeto() == StatusDoProjeto.ENCERRADO || 
            projeto.getStatusDoProjeto() == StatusDoProjeto.CANCELADO) {
            throw new IllegalArgumentException("Não é possível alocar membros em projetos encerrados ou cancelados");
        }

        MembroDTO membro = membroService.buscarPorId(membroId);
        if (membro.getAtribuicaoMembro() != AtribuicaoMembro.FUNCIONARIO) {
            throw new IllegalArgumentException("Apenas funcionários podem ser alocados em projetos");
        }

        if (projeto.getAlocacoes().size() >= 10) {
            throw new IllegalArgumentException("Projeto já possui o máximo de 10 membros");
        }

        long projetosAtivos = projetoRepository.countProjetosAtivosByMembroId(membroId);
        if (projetosAtivos >= 3) {
            throw new IllegalArgumentException("Membro já está alocado em 3 projetos ativos");
        }

        boolean jaAlocado = projeto.getAlocacoes().stream()
                .anyMatch(a -> a.getMembro().getId().equals(membroId));
        if (jaAlocado) {
            throw new IllegalArgumentException("Membro já está alocado neste projeto");
        }

        MembroEntity membroEntity = new MembroEntity();
        membroEntity.setId(membroId);

        AlocacaoEntity alocacao = new AlocacaoEntity();
        alocacao.setProjeto(projeto);
        alocacao.setMembro(membroEntity);
        alocacao.setDataAlocacao(LocalDate.now());

        alocacaoRepository.save(alocacao);
    }

    public void desalocarMembro(Long projetoId, Long membroId) {
        ProjetoEntity projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        AlocacaoEntity alocacao = projeto.getAlocacoes().stream()
                .filter(a -> a.getMembro().getId().equals(membroId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membro não está alocado neste projeto"));

        if (projeto.getAlocacoes().size() <= 1) {
            throw new IllegalArgumentException("Projeto deve ter pelo menos 1 membro alocado");
        }

        alocacaoRepository.delete(alocacao);
    }

    public List<MembroDTO> listarMembrosProj(Long projetoId) {
        ProjetoEntity projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        return projeto.getAlocacoes().stream()
                .map(a -> membroService.buscarPorId(a.getMembro().getId()))
                .collect(Collectors.toList());
    }

    public void editarAlocacao(Long projetoId, Long membroId) {
        ProjetoEntity projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        AlocacaoEntity alocacao = projeto.getAlocacoes().stream()
                .filter(a -> a.getMembro().getId().equals(membroId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membro não está alocado neste projeto"));

        alocacao.setDataAlocacao(LocalDate.now());
        alocacaoRepository.save(alocacao);
    }
}
