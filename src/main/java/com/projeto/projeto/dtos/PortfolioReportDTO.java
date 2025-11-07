package com.projeto.projeto.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioReportDTO {


    private long projetosEmAnalise;
    private long projetosAnaliseRealizada;
    private long projetosAnaliseAprovada;
    private long projetosIniciados;
    private long projetosPlanejados;
    private long projetosEmAndamento;
    private long projetosEncerrados;
    private long projetosCancelados;


    private BigDecimal orcamentoTotalEmAnalise = BigDecimal.ZERO;
    private BigDecimal orcamentoTotalAnaliseRealizada = BigDecimal.ZERO;
    private BigDecimal orcamentoTotalAnaliseAprovada = BigDecimal.ZERO;
    private BigDecimal orcamentoTotalIniciados = BigDecimal.ZERO;
    private BigDecimal orcamentoTotalPlanejados = BigDecimal.ZERO;
    private BigDecimal orcamentoTotalEmAndamento = BigDecimal.ZERO;
    private BigDecimal orcamentoTotalEncerrados = BigDecimal.ZERO;
    private BigDecimal orcamentoTotalCancelados = BigDecimal.ZERO;



    private double mediaDuracaoEncerrados;


    private long totalMembrosUnicos;
}
