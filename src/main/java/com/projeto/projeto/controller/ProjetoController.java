package com.projeto.projeto.controller;

import com.projeto.projeto.dtos.PortfolioReportDTO;
import com.projeto.projeto.dtos.ProjetoDTO;
import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.StatusDoProjeto;
import com.projeto.projeto.service.ProjetoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/projetos")
@Tag(name = "Projetos", description = "Endpoints para gerenciar projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @Operation(summary = "Criar um novo projeto")
    @PostMapping
    public ProjetoEntity criarProjeto(@RequestBody ProjetoEntity projeto) {
        return projetoService.salvar(projeto);
    }

    @Operation(summary = "Listar todos os projetos (sem paginação)")
    @GetMapping("/todos")
    public List<ProjetoEntity> listarTodosProjetos() {
        return projetoService.listar();
    }

    @Operation(summary = "Atualizar um projeto existente")
    @PutMapping("/{id}")
    public ProjetoEntity atualizarProjeto(
            @PathVariable Long id,
            @RequestBody ProjetoEntity projetoAtualizado
    ) {
        return projetoService.editar(id, projetoAtualizado);
    }

    @Operation(summary = "Deletar um projeto pelo ID")
    @DeleteMapping("/{id}")
    public void deletarProjeto(@PathVariable Long id) {
        projetoService.deleteById(id);
    }

    @Operation(summary = "Atualizar o status de um projeto")
    @PatchMapping("/{id}/status")
    public ProjetoEntity atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusDoProjeto novoStatus
    ) {
        return projetoService.atualizarStatus(id, novoStatus);
    }

    @Operation(summary = "Listar projetos com filtros e paginação")
    @GetMapping
    public Page<ProjetoDTO> listarProjetosComFiltro(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) StatusDoProjeto status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return projetoService.listarProjetosComFiltro(nome, status, pageable);
    }
}
