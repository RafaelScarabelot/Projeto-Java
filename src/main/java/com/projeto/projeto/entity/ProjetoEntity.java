package com.projeto.projeto.entity;


import com.projeto.projeto.enums.StatusDoProjeto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;


@Getter
@Setter

@Entity
public class ProjetoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate previsaoTermino;
    private LocalDate dataRealTermino;
    private BigDecimal orcamentoTotal;

    @Lob
    private String descricao;

    @Column(name = "gerente_id")
    private Long gerenteId;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AlocacaoEntity> alocacoes;

    @Enumerated(EnumType.STRING)
    private StatusDoProjeto statusDoProjeto;

    @Transient
    private String classificacaoRisco;

    public String getClassificacaoRisco() {
        BigDecimal orcamento = getOrcamentoTotal();
        LocalDate inicio = getDataInicio();
        LocalDate previsao = getPrevisaoTermino();

        if (orcamentoTotal == null || dataInicio == null || previsaoTermino == null) {
            return "Desconhecido";
        }

        long meses = ChronoUnit.MONTHS.between(inicio, previsao);

        if (orcamento.compareTo(new BigDecimal("100000")) <= 0 && meses <= 3) {
            return "Baixo risco";
        }
        else if ((orcamento.compareTo(new BigDecimal("100001")) >= 0 && orcamento.compareTo(new BigDecimal("500000")) <= 0) || (meses > 3 && meses <= 6)) {
            return "Médio risco";
        }
        else if (orcamento.compareTo(new BigDecimal("500000")) > 0 || meses > 6) {
            return "Alto risco";
        }
        else {
            return "Desconhecido";
        }
    }





}
