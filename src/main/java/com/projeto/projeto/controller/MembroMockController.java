package com.projeto.projeto.controller;

import com.projeto.projeto.dtos.MembroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/membros")
@Tag(name = "Membros", description = "Endpoints para gerenciar membros")
public class MembroMockController {

    private final List<MembroDTO> membros = new ArrayList<>();
    private long nextId = 1;

    @Operation(summary = "Listar membros")
    @GetMapping
    public List<MembroDTO> listarMembros() {
        return membros;
    }
    
    @Operation(summary = "Buscar membro por id")
    @GetMapping("/{id}")
    public MembroDTO buscarPorId(@PathVariable Long id) {
        return membros.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));
    }

    @Operation(summary = "Criar Membro")
    @PostMapping
    public MembroDTO criarMembro(@RequestBody MembroDTO membro) {
        membro.setId(nextId++);
        membros.add(membro);
        return membro;
    }

    @Operation(summary = "Editar membro")
    @PutMapping("/{id}")
    public MembroDTO editarMembro(@PathVariable Long id, @RequestBody MembroDTO membroAtualizado) {
        MembroDTO membro = buscarPorId(id);
        membro.setNome(membroAtualizado.getNome());
        membro.setAtribuicaoMembro(membroAtualizado.getAtribuicaoMembro());
        return membro;
    }

    @Operation(summary = "Deletar membro")
    @DeleteMapping("/{id}")
    public void deletarMembro(@PathVariable Long id) {
        membros.removeIf(m -> m.getId().equals(id));
    }
}