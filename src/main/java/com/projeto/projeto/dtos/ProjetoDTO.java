package com.projeto.projeto.dtos;

import com.projeto.projeto.enums.StatusDoProjeto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoDTO {
    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate previsaoTermino;
    private LocalDate dataRealTermino;
    private BigDecimal orcamentoTotal;
    private String descricao;
    private Long gerenteId;
    private StatusDoProjeto statusDoProjeto;
    private String classificacaoRisco;
}
