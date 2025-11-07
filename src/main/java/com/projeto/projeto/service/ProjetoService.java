package com.projeto.projeto.service;


import com.projeto.projeto.dtos.MembroDTO;
import com.projeto.projeto.dtos.PortfolioReportDTO;
import com.projeto.projeto.dtos.ProjetoDTO;
import com.projeto.projeto.entity.AlocacaoEntity;
import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.AtribuicaoMembro;
import com.projeto.projeto.enums.StatusDoProjeto;
import com.projeto.projeto.exeception.OperacaoNaoPermitidaException;
import com.projeto.projeto.exeception.ProjetoNotFoundException;
import com.projeto.projeto.repository.ProjetoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    private final MembroService membroService;

    public ProjetoService(ProjetoRepository projetoRepository, MembroService membroService) {
        this.projetoRepository = projetoRepository;
        this.membroService = membroService;
    }

    public ProjetoEntity salvar(ProjetoEntity projeto) {

        if (projeto.getDataInicio() == null || projeto.getPrevisaoTermino() == null || projeto.getOrcamentoTotal() == null) {
            throw new IllegalArgumentException("Data de início, previsão de término e orçamento são obrigatórios.");
        }

        String resultadoClassificacao = projeto.getClassificacaoRisco();
        projeto.setClassificacaoRisco(resultadoClassificacao);

        if (projeto.getStatusDoProjeto() == null) {
            projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        }

        validarAlocacoes(projeto);

        return projetoRepository.save(projeto);

    }

    public ProjetoEntity buscarPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new ProjetoNotFoundException(id));
    }

    public List<ProjetoEntity> listar() {
        return projetoRepository.findAll();
    }

    public void deleteById(Long id) {

        ProjetoEntity projeto = buscarPorId(id);
        if (projeto.getStatusDoProjeto() == StatusDoProjeto.INICIADO ||
                projeto.getStatusDoProjeto() == StatusDoProjeto.EM_ANDAMENTO ||
                projeto.getStatusDoProjeto() == StatusDoProjeto.ENCERRADO) {
            throw new OperacaoNaoPermitidaException("Não é permitido excluir projeto iniciado, em andamento ou encerrado.");
        }

        projetoRepository.deleteById(id);
    }


    public ProjetoEntity editar(Long id, ProjetoEntity projetoAtualizado) {
        ProjetoEntity projetoExistente = buscarPorId(id);

        if (projetoExistente.getStatusDoProjeto() == StatusDoProjeto.INICIADO ||
                projetoExistente.getStatusDoProjeto() == StatusDoProjeto.EM_ANDAMENTO ||
                projetoExistente.getStatusDoProjeto() == StatusDoProjeto.ENCERRADO) {

            throw new OperacaoNaoPermitidaException(
                    "Projetos iniciados, em andamento ou encerrados não podem ser alterados."
            );
        }
        projetoExistente.setNome(projetoAtualizado.getNome());
        projetoExistente.setDataInicio(projetoAtualizado.getDataInicio());
        projetoExistente.setPrevisaoTermino(projetoAtualizado.getPrevisaoTermino());
        projetoExistente.setDataRealTermino(projetoAtualizado.getDataRealTermino());
        projetoExistente.setOrcamentoTotal(projetoAtualizado.getOrcamentoTotal());
        projetoExistente.setDescricao(projetoAtualizado.getDescricao());
        projetoExistente.setGerente(projetoAtualizado.getGerente());

        if (projetoAtualizado.getAlocacoes() != null) {
            projetoExistente.getAlocacoes().clear();
            projetoExistente.getAlocacoes().addAll(projetoAtualizado.getAlocacoes());
            validarAlocacoes(projetoExistente);
        }

        String resultadoClassificacao = projetoExistente.getClassificacaoRisco();
        projetoExistente.setClassificacaoRisco(resultadoClassificacao);

        return projetoRepository.save(projetoExistente);
    }


    public ProjetoEntity atualizarStatus(Long id, StatusDoProjeto novoStatus) {
        ProjetoEntity projeto = buscarPorId(id);


        if (novoStatus == StatusDoProjeto.CANCELADO) {
            projeto.setStatusDoProjeto(novoStatus);
            return projetoRepository.save(projeto);
        }

        if (!projeto.getStatusDoProjeto().proximoStatusPermitido().contains(novoStatus)) {
            throw new OperacaoNaoPermitidaException("Transição de status inválida.");
        }


        projeto.setStatusDoProjeto(novoStatus);
        return projetoRepository.save(projeto);
    }


    private void validarAlocacoes(ProjetoEntity projeto) {
        Set<AlocacaoEntity> alocacoes = projeto.getAlocacoes();

        if (alocacoes == null || alocacoes.isEmpty()) {
            throw new IllegalArgumentException("O projeto deve ter pelo menos 1 membro alocado.");
        }

        if (alocacoes.size() > 10) {
            throw new IllegalArgumentException("O projeto não pode ter mais de 10 membros alocados.");
        }

        for (AlocacaoEntity alocacao : alocacoes) {
            Long membroId = alocacao.getMembro().getId();


            MembroDTO membroDTO;
            try {
                membroDTO = membroService.buscarPorId(membroId);
            } catch (Exception e) {
                throw new IllegalArgumentException("Membro com id " + membroId + " não encontrado na API de membros.");
            }


            if (membroDTO.getAtribuicaoMembro() != AtribuicaoMembro.FUNCIONARIO) {
                throw new IllegalArgumentException(
                        "Apenas membros com atribuição FUNCIONARIO podem ser alocados em projetos. Membro: " + membroDTO.getNome()
                );
            }

            // 3. Verifica se o membro já está em mais de 3 projetos ativos (consultando banco)
            long qtdProjetosAtivos = projetoRepository.countProjetosAtivosByMembroId(membroId);
            if (qtdProjetosAtivos >= 3) {
                throw new IllegalArgumentException(
                        "O membro " + membroDTO.getNome() + " já está alocado em 3 projetos ativos."
                );
            }
        }
    }


        public PortfolioReportDTO gerarRelatorio() {
            List<ProjetoEntity> projetos = listar(); // pega todos os projetos do banco

            PortfolioReportDTO report = new PortfolioReportDTO();

            // Inicializa BigDecimals
            report.setOrcamentoTotalEmAnalise(BigDecimal.ZERO);
            report.setOrcamentoTotalAnaliseRealizada(BigDecimal.ZERO);
            report.setOrcamentoTotalAnaliseAprovada(BigDecimal.ZERO);
            report.setOrcamentoTotalIniciados(BigDecimal.ZERO);
            report.setOrcamentoTotalPlanejados(BigDecimal.ZERO);
            report.setOrcamentoTotalEmAndamento(BigDecimal.ZERO);
            report.setOrcamentoTotalEncerrados(BigDecimal.ZERO);
            report.setOrcamentoTotalCancelados(BigDecimal.ZERO);

            Set<Long> membrosUnicos = new HashSet<>();
            double somaDuracaoEncerrados = 0;
            long countEncerrados = 0;

            for (ProjetoEntity p : projetos) {
                // Contagem por status
                switch (p.getStatusDoProjeto()) {
                    case EM_ANALISE -> report.setProjetosEmAnalise(report.getProjetosEmAnalise() + 1);
                    case ANALISE_REALIZADA -> report.setProjetosAnaliseRealizada(report.getProjetosAnaliseRealizada() + 1);
                    case ANALISE_APROVADA -> report.setProjetosAnaliseAprovada(report.getProjetosAnaliseAprovada() + 1);
                    case INICIADO -> report.setProjetosIniciados(report.getProjetosIniciados() + 1);
                    case PLANEJADO -> report.setProjetosPlanejados(report.getProjetosPlanejados() + 1);
                    case EM_ANDAMENTO -> report.setProjetosEmAndamento(report.getProjetosEmAndamento() + 1);
                    case ENCERRADO -> {
                        report.setProjetosEncerrados(report.getProjetosEncerrados() + 1);
                        if (p.getDataInicio() != null && p.getDataRealTermino() != null) {
                            long duracao = ChronoUnit.DAYS.between(p.getDataInicio(), p.getDataRealTermino());
                            somaDuracaoEncerrados += duracao;
                            countEncerrados++;
                        }
                    }
                    case CANCELADO -> report.setProjetosCancelados(report.getProjetosCancelados() + 1);
                }

                // Soma orçamento por status
                BigDecimal orcamento = p.getOrcamentoTotal() != null ? p.getOrcamentoTotal() : BigDecimal.ZERO;
                switch (p.getStatusDoProjeto()) {
                    case EM_ANALISE -> report.setOrcamentoTotalEmAnalise(report.getOrcamentoTotalEmAnalise().add(orcamento));
                    case ANALISE_REALIZADA -> report.setOrcamentoTotalAnaliseRealizada(report.getOrcamentoTotalAnaliseRealizada().add(orcamento));
                    case ANALISE_APROVADA -> report.setOrcamentoTotalAnaliseAprovada(report.getOrcamentoTotalAnaliseAprovada().add(orcamento));
                    case INICIADO -> report.setOrcamentoTotalIniciados(report.getOrcamentoTotalIniciados().add(orcamento));
                    case PLANEJADO -> report.setOrcamentoTotalPlanejados(report.getOrcamentoTotalPlanejados().add(orcamento));
                    case EM_ANDAMENTO -> report.setOrcamentoTotalEmAndamento(report.getOrcamentoTotalEmAndamento().add(orcamento));
                    case ENCERRADO -> report.setOrcamentoTotalEncerrados(report.getOrcamentoTotalEncerrados().add(orcamento));
                    case CANCELADO -> report.setOrcamentoTotalCancelados(report.getOrcamentoTotalCancelados().add(orcamento));
                }

                // Membros únicos
                p.getAlocacoes().forEach(a -> membrosUnicos.add(a.getMembro().getId()));
            }

            // Média de duração dos encerrados
            report.setMediaDuracaoEncerrados(countEncerrados > 0 ? somaDuracaoEncerrados / countEncerrados : 0);
            report.setTotalMembrosUnicos(membrosUnicos.size());

            return report;
        }


    public Page<ProjetoDTO> listarProjetosComFiltro(String nome, StatusDoProjeto status, Pageable pageable) {
        Page<ProjetoEntity> projetos;

        if (nome != null && status != null) {
            projetos = projetoRepository.findByStatusDoProjetoAndNomeContainingIgnoreCase(status, nome, pageable);
        } else if (nome != null) {
            projetos = projetoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (status != null) {
            projetos = projetoRepository.findByStatusDoProjeto(status, pageable);
        } else {
            projetos = projetoRepository.findAll(pageable);
        }

        return projetos.map(this::mapToDTO);
    }

    private ProjetoDTO mapToDTO(ProjetoEntity projeto) {
        return new ProjetoDTO(
                projeto.getId(),
                projeto.getNome(),
                projeto.getStatusDoProjeto(),
                projeto.getOrcamentoTotal()
        );
    }

    }
