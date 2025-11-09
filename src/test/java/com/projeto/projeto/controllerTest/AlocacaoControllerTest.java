package com.projeto.projeto.controllerTest;

import com.projeto.projeto.controller.AlocacaoController;
import com.projeto.projeto.dtos.MembroDTO;
import com.projeto.projeto.enums.AtribuicaoMembro;
import com.projeto.projeto.service.AlocacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AlocacaoControllerTest {

    @Mock
    private AlocacaoService alocacaoService;

    @InjectMocks
    private AlocacaoController alocacaoController;

    @Test
    @DisplayName("Teste de Integração - Deve alocar membro ao projeto")
    void alocarMembro_DeveRetornarSucesso() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(alocacaoController).build();

        mockMvc.perform(post("/projetos/1/membros/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Membro alocado com sucesso"));

        verify(alocacaoService).alocarMembro(1L, 1L);
    }

    @Test
    @DisplayName("Teste de Integração - Deve desalocar membro do projeto")
    void desalocarMembro_DeveRetornarSucesso() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(alocacaoController).build();

        mockMvc.perform(delete("/projetos/1/membros/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Membro desalocado com sucesso"));

        verify(alocacaoService).desalocarMembro(1L, 1L);
    }

    @Test
    @DisplayName("Teste de Integração - Deve listar membros do projeto")
    void listarMembrosProj_DeveRetornarLista() throws Exception {
        List<MembroDTO> membros = Arrays.asList(
                new MembroDTO(1L, "João", AtribuicaoMembro.FUNCIONARIO),
                new MembroDTO(2L, "Maria", AtribuicaoMembro.GERENTE)
        );

        when(alocacaoService.listarMembrosProj(1L)).thenReturn(membros);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(alocacaoController).build();

        mockMvc.perform(get("/projetos/1/membros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[1].nome").value("Maria"));

        verify(alocacaoService).listarMembrosProj(1L);
    }

    @Test
    @DisplayName("Teste de Integração - Deve editar alocação de membro")
    void editarAlocacao_DeveRetornarSucesso() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(alocacaoController).build();

        mockMvc.perform(put("/projetos/1/membros/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Alocação editada com sucesso"));

        verify(alocacaoService).editarAlocacao(1L, 1L);
    }
}