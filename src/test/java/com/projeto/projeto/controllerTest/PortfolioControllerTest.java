package com.projeto.projeto.controllerTest;

import com.projeto.projeto.controller.PortfolioController;
import com.projeto.projeto.dtos.PortfolioReportDTO;
import com.projeto.projeto.service.ProjetoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioControllerTest {

    @Mock
    private ProjetoService projetoService;

    @InjectMocks
    private PortfolioController portfolioController;

    @Test
    @DisplayName("Teste de Integração - Deve retornar um relatório")
    void gerarRelatorio_DeveRetornarRelatorio() throws Exception {
        PortfolioReportDTO relatorio = new PortfolioReportDTO(
                1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(2000), BigDecimal.valueOf(3000),
                BigDecimal.valueOf(4000), BigDecimal.valueOf(5000), BigDecimal.valueOf(6000),
                BigDecimal.valueOf(7000), BigDecimal.valueOf(8000),
                30.5, 10L
        );

        when(projetoService.gerarRelatorio()).thenReturn(relatorio);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(portfolioController).build();

        mockMvc.perform(get("/api/portfolios/relatorio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projetosEmAnalise").value(1))
                .andExpect(jsonPath("$.projetosAnaliseRealizada").value(2))
                .andExpect(jsonPath("$.projetosAnaliseAprovada").value(3))
                .andExpect(jsonPath("$.projetosIniciados").value(4))
                .andExpect(jsonPath("$.projetosPlanejados").value(5))
                .andExpect(jsonPath("$.projetosEmAndamento").value(6))
                .andExpect(jsonPath("$.projetosEncerrados").value(7))
                .andExpect(jsonPath("$.projetosCancelados").value(8))
                .andExpect(jsonPath("$.orcamentoTotalEmAnalise").value(1000))
                .andExpect(jsonPath("$.mediaDuracaoEncerrados").value(30.5))
                .andExpect(jsonPath("$.totalMembrosUnicos").value(10));
    }
}