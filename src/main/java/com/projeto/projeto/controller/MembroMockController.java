package com.projeto.projeto.controller;

import com.projeto.projeto.dtos.MembroDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/membros")
public class MembroMockController {

    private final List<MembroDTO> membros = new ArrayList<>();
    private long nextId = 1;

    @GetMapping
    public List<MembroDTO> listarMembros() {
        return membros;
    }

    @GetMapping("/{id}")
    public MembroDTO buscarPorId(@PathVariable Long id) {
        return membros.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));
    }

    @PostMapping
    public MembroDTO criarMembro(@RequestBody MembroDTO membro) {
        membro.setId(nextId++);
        membros.add(membro);
        return membro;
    }
}
