package com.projeto.projeto.controller;

import com.projeto.projeto.dtos.MembroDTO;
import com.projeto.projeto.service.AlocacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/projetos")
@Tag(name = "Alocação de Membros", description = "Endpoints para gerenciar alocação de membros em projetos")
@RequiredArgsConstructor
public class AlocacaoController {

    private final AlocacaoService alocacaoService;

    @Operation(summary = "Alocar membro ao projeto")
    @PostMapping("/{projetoId}/membros/{membroId}")
    public ResponseEntity<String> alocarMembro(
            @PathVariable Long projetoId,
            @PathVariable Long membroId
    ) {
        alocacaoService.alocarMembro(projetoId, membroId);
        return ResponseEntity.ok("Membro alocado com sucesso");
    }

    @Operation(summary = "Desalocar membro do projeto")
    @DeleteMapping("/{projetoId}/membros/{membroId}")
    public ResponseEntity<String> desalocarMembro(
            @PathVariable Long projetoId,
            @PathVariable Long membroId
    ) {
        alocacaoService.desalocarMembro(projetoId, membroId);
        return ResponseEntity.ok("Membro desalocado com sucesso");
    }

    @Operation(summary = "Listar membros do projeto")
    @GetMapping("/{projetoId}/membros")
    public List<MembroDTO> listarMembrosProj(@PathVariable Long projetoId) {
        return alocacaoService.listarMembrosProj(projetoId);
    }

    @Operation(summary = "Editar alocação de membro")
    @PutMapping("/{projetoId}/membros/{membroId}")
    public ResponseEntity<String> editarAlocacao(
            @PathVariable Long projetoId,
            @PathVariable Long membroId
    ) {
        alocacaoService.editarAlocacao(projetoId, membroId);
        return ResponseEntity.ok("Alocação editada com sucesso");
    }
}