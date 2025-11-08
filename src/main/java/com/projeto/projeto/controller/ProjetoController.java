package com.projeto.projeto.controller;


import com.projeto.projeto.dtos.ProjetoDTO;
import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.StatusDoProjeto;
import com.projeto.projeto.mapper.ProjetoMapper;
import com.projeto.projeto.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/projetos")
@Tag(name = "Projetos", description = "Endpoints para gerenciar projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;
    private final ProjetoMapper projetoMapper;



    @PostMapping("/criar")
    public ResponseEntity<?> criarProjeto(@RequestBody ProjetoDTO projetoDTO) {
        try {
            ProjetoEntity projeto = projetoMapper.toEntity(projetoDTO); // <--- aqui
            ProjetoEntity salvo = projetoService.salvarProjeto(projeto);
            return ResponseEntity.status(HttpStatus.CREATED).body(projetoMapper.toDTO(salvo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Listar todos os projetos (sem paginação)")
    @GetMapping("/todos")
    public List<ProjetoDTO> listarTodosProjetos() {
        return projetoService.listar()
                .stream()
                .map(projetoMapper::toDTO)
                .toList(); // converte cada ProjetoEntity para ProjetoDTO
    }

    @Operation(summary = "Atualizar um projeto existente")
    @PutMapping("/{id}")
    public ProjetoDTO atualizarProjeto(
            @PathVariable Long id,
            @RequestBody ProjetoDTO projetoAtualizadoDTO
    ) {
        ProjetoEntity projetoAtualizado = projetoMapper.toEntity(projetoAtualizadoDTO);
        ProjetoEntity salvo = projetoService.editar(id, projetoAtualizado);
        return projetoMapper.toDTO(salvo);
    }

    @Operation(summary = "Deletar um projeto pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable Long id) {
        projetoService.deleteById(id);
        return ResponseEntity.noContent().build(); // retorna 204 NO CONTENT
    }

    @Operation(summary = "Atualizar o status de um projeto")
    @PatchMapping("/{id}/status")
    public ProjetoDTO atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusDoProjeto novoStatus
    ) {
        ProjetoEntity atualizado = projetoService.atualizarStatus(id, novoStatus);
        return projetoMapper.toDTO(atualizado);
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
