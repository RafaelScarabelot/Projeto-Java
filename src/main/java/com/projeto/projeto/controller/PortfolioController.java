package com.projeto.projeto.controller;

import com.projeto.projeto.dtos.PortfolioReportDTO;
import com.projeto.projeto.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final ProjetoService projetoService;


    @GetMapping("/relatorio")
    public PortfolioReportDTO gerarRelatorio() {
        return projetoService.gerarRelatorio();
    }
}
