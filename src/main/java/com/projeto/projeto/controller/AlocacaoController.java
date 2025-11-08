package com.projeto.projeto.controller;

import com.projeto.projeto.entity.AlocacaoEntity;
import com.projeto.projeto.service.AlocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alocacoes")
public class AlocacaoController {

    @Autowired
    private AlocacaoService alocacaoService;

    @GetMapping
    public List<AlocacaoEntity> listarTodas() {
        return alocacaoService.listarTodas();
    }

    @GetMapping("/{id}")
    public AlocacaoEntity buscarPorId(@PathVariable Long id) {
        return alocacaoService.buscarPorId(id);
    }

    @PostMapping
    public AlocacaoEntity criarAlocacao(@RequestBody AlocacaoEntity alocacao) {
        return alocacaoService.criarAlocacao(alocacao);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        alocacaoService.deletar(id);
    }
}
