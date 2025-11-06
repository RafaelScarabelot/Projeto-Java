package com.projeto.projeto.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjetoDTO(
        String nome,
        LocalDate dataInicio,
        LocalDate previsaoTermino,
        BigDecimal orcamentoTotal,
        String descricao

) {}

