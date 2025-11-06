package com.projeto.projeto.controller;

import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.service.ProjetoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @PostMapping
    public ProjetoEntity criarProjeto(@RequestBody ProjetoEntity projeto) {
        return projetoService.salvar(projeto);
    }

    @GetMapping
    public List<ProjetoEntity> listarProjetos() {
        return projetoService.listar();
    }

    @PutMapping("/{id}")
    public ProjetoEntity atualizarProjeto(@PathVariable Long id, @RequestBody ProjetoEntity projetoAtualizado) {
        return projetoService.editar(id, projetoAtualizado);
    }

    @DeleteMapping("/{id}")
    public void deletarProjeto(@PathVariable Long id) {
        projetoService.deleteById(id);
    }

}
