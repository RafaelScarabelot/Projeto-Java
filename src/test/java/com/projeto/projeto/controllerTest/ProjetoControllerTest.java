package com.projeto.projeto.controllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.AtribuicaoMembro;
import com.projeto.projeto.enums.StatusDoProjeto;
import com.projeto.projeto.repository.AlocacaoRepository;
import com.projeto.projeto.repository.MembroRepository;
import com.projeto.projeto.repository.ProjetoRepository;
import com.projeto.projeto.service.MembroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.mockito.Mockito.when;
import com.projeto.projeto.dtos.MembroDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjetoControllerTest {

    @Autowired
    MockMvc mockMvc;

    //⚠️Está marcado como **deprecated** a partir do Spring Boot 3.x (desde 3.4.0)
// Por que usei:
// – Praticidade: rápido de rodar, sem levantar aplicação.

    @MockBean
     MembroService membroService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProjetoRepository projetoRepository;

    @Autowired
    MembroRepository membroRepository;

    @Autowired
    AlocacaoRepository alocacaoRepository;

    @BeforeEach
    void setUp() {
        projetoRepository.deleteAll();
        alocacaoRepository.deleteAll();
        membroRepository.deleteAll();
    }

    @Test
    @DisplayName("Teste de Integração - Deve salvar projeto com sucesso e retornar 201 CREATED")
    public void criarProjeto() throws Exception {
        when(membroService.buscarPorId(1L)).thenReturn(new MembroDTO(1L, "João Silva", AtribuicaoMembro.GERENTE));
        
        String projetoJson = """
            {
            "nome": "Sistema Vendas",
            "dataInicio": "2024-01-15",
            "previsaoTermino": "2024-04-15",
            "orcamentoTotal": 150000.00,
            "descricao": "Sistema de vendas online",
            "gerenteId": 1
        }""";

        mockMvc.perform(post("/projetos/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projetoJson)
                .with(httpBasic("admin", "1234")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Sistema Vendas"))
                .andExpect(jsonPath("$.orcamentoTotal").value(150000.00))
                .andExpect(jsonPath("$.gerenteId").value(1));
    }

    @Test
    @DisplayName("Teste de Integração - Deve dar erro ao criar projeto e retornar Bad Request")
    public void deveFalharQuandoGerenteNaoExiste() throws Exception {
        String projetoJson = """
            {
            "nome": "Sistema Vendas",
            "dataInicio": "2024-01-15",
            "previsaoTermino": "2024-04-15",
            "orcamentoTotal": 150000.00,
            "descricao": "Sistema de vendas online",
            "gerenteId": 999
        }""";

        mockMvc.perform(post("/projetos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projetoJson)
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Gerente com id 999 não encontrado")));
    }

    @Test
    @DisplayName("Teste de Integração - Deve buscar projeto por meio do id")
    public void deveRetornarOProjetoPeloIdEnviado() throws Exception{
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setNome("Projeto1");
        projeto.setDescricao("Sistema de gestão interna da empresa.");
        projeto.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto.setOrcamentoTotal(new BigDecimal("250000"));
        projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANDAMENTO);
        projeto.setGerenteId(1L);

        ProjetoEntity projetoSalvo = projetoRepository.save(projeto);

        mockMvc.perform(get("/projetos/{id}", projetoSalvo.getId())
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Projeto1"));
    }

    @Test
    @DisplayName("Teste de Integração - Deve retornar erro 404 quando projeto não existe")
    public void deveRetornarErroQuandoProjetoNaoExiste() throws Exception{
        Long idInexistente = 999L;

        mockMvc.perform(get("/projetos/{id}", idInexistente)
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Teste de Integração - Deve atualizar projeto existente")
    public void deveAtualizarProjetoExistente() throws Exception {
        when(membroService.buscarPorId(1L)).thenReturn(new MembroDTO(1L, "João Silva", AtribuicaoMembro.GERENTE));
        
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setNome("Projeto1");
        projeto.setDescricao("Sistema de gestão interna da empresa.");
        projeto.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto.setOrcamentoTotal(new BigDecimal("250000"));
        projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto.setGerenteId(1L);

        ProjetoEntity projetoSalvo = projetoRepository.save(projeto);

        String projetoJson = """
            {
                "nome": "Projeto Atualizado",
                "dataInicio": "2025-01-15",
                "previsaoTermino": "2025-05-15",
                "orcamentoTotal": 150000.00,
                "descricao": "Descrição atualizada",
                "gerenteId": 1
            }""";

        mockMvc.perform(put("/projetos/{id}",projetoSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projetoJson)
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Projeto Atualizado"))
                .andExpect(jsonPath("$.orcamentoTotal").value(150000.00));
    }

    @Test
    @DisplayName("Teste de Integração - Deve retornar erro ao atualizar projeto iniciado")
    public void deveRetornarErroAoTentarAtualizarProjetoIniciado() throws Exception {
        when(membroService.buscarPorId(1L)).thenReturn(new MembroDTO(1L, "João Silva", AtribuicaoMembro.GERENTE));
        
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setNome("Projeto1");
        projeto.setDescricao("Sistema de gestão interna da empresa.");
        projeto.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto.setOrcamentoTotal(new BigDecimal("250000"));
        projeto.setStatusDoProjeto(StatusDoProjeto.INICIADO);
        projeto.setGerenteId(1L);

        ProjetoEntity projetoSalvo = projetoRepository.save(projeto);

        String projetoJson = """
            {
                "nome": "Projeto Atualizado",
                "dataInicio": "2025-01-15",
                "previsaoTermino": "2025-05-15",
                "orcamentoTotal": 150000.00,
                "descricao": "Descrição atualizada",
                "gerenteId": 1
            }""";

        mockMvc.perform(put("/projetos/{id}",projetoSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projetoJson)
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("não podem ser alterados")));
    }

    @Test
    @DisplayName("Teste de Integração - Deve listar todos os projetos")
    public void deveListarTodosProjetos() throws Exception {
        ProjetoEntity projeto1 = new ProjetoEntity();
        projeto1.setNome("Projeto1");
        projeto1.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto1.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto1.setOrcamentoTotal(new BigDecimal("250000"));
        projeto1.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto1.setGerenteId(1L);
        projetoRepository.save(projeto1);

        ProjetoEntity projeto2 = new ProjetoEntity();
        projeto2.setNome("Projeto2");
        projeto2.setDataInicio(LocalDate.of(2025, 2, 1));
        projeto2.setPrevisaoTermino(LocalDate.of(2025, 6, 1));
        projeto2.setOrcamentoTotal(new BigDecimal("300000"));
        projeto2.setStatusDoProjeto(StatusDoProjeto.INICIADO);
        projeto2.setGerenteId(1L);
        projetoRepository.save(projeto2);

        mockMvc.perform(get("/projetos/todos")
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("Projeto1"))
                .andExpect(jsonPath("$[1].nome").value("Projeto2"));
    }

    @Test
    @DisplayName("Teste de Integração - Deve retornar lista vazia quando não há projetos")
    public void deveRetornarListaVaziaQuandoNaoHaProjetos() throws Exception {
        mockMvc.perform(get("/projetos/todos")
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Teste de Integração - Deve deletar projeto com sucesso")
    public void deveDeletarProjeto() throws Exception {
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setNome("Projeto Para Deletar");
        projeto.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto.setOrcamentoTotal(new BigDecimal("100000"));
        projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto.setGerenteId(1L);

        ProjetoEntity projetoSalvo = projetoRepository.save(projeto);

        mockMvc.perform(delete("/projetos/{id}", projetoSalvo.getId())
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Teste de Integração - Deve retornar erro ao deletar projeto iniciado")
    public void deveRetornarErroAoDeletarProjetoIniciado() throws Exception {
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setNome("Projeto Iniciado");
        projeto.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto.setOrcamentoTotal(new BigDecimal("100000"));
        projeto.setStatusDoProjeto(StatusDoProjeto.INICIADO);
        projeto.setGerenteId(1L);

        ProjetoEntity projetoSalvo = projetoRepository.save(projeto);

        mockMvc.perform(delete("/projetos/{id}", projetoSalvo.getId())
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Não é permitido excluir projeto iniciado")));
    }

    @Test
    @DisplayName("Teste de Integração - Deve atualizar status do projeto")
    public void deveAtualizarStatusProjeto() throws Exception {
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setNome("Projeto Status");
        projeto.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto.setOrcamentoTotal(new BigDecimal("100000"));
        projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto.setGerenteId(1L);

        ProjetoEntity projetoSalvo = projetoRepository.save(projeto);

        mockMvc.perform(patch("/projetos/{id}/status", projetoSalvo.getId())
                        .param("novoStatus", "ANALISE_REALIZADA")
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDoProjeto").value("ANALISE_REALIZADA"));
    }

    @Test
    @DisplayName("Teste de Integração - Deve retornar erro ao pular etapa de status")
    public void deveRetornarErroAoPularEtapaStatus() throws Exception {
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setNome("Projeto Status");
        projeto.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto.setOrcamentoTotal(new BigDecimal("100000"));
        projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto.setGerenteId(1L);

        ProjetoEntity projetoSalvo = projetoRepository.save(projeto);

        mockMvc.perform(patch("/projetos/{id}/status", projetoSalvo.getId())
                        .param("novoStatus", "INICIADO")
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Transição de status inválida")));
    }

    @Test
    @DisplayName("Teste de Integração - Deve listar projetos com filtro por nome")
    public void deveListarProjetosComFiltroNome() throws Exception {
        ProjetoEntity projeto1 = new ProjetoEntity();
        projeto1.setNome("Sistema Vendas");
        projeto1.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto1.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto1.setOrcamentoTotal(new BigDecimal("100000"));
        projeto1.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto1.setGerenteId(1L);
        projetoRepository.save(projeto1);

        ProjetoEntity projeto2 = new ProjetoEntity();
        projeto2.setNome("Sistema Compras");
        projeto2.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto2.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto2.setOrcamentoTotal(new BigDecimal("100000"));
        projeto2.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto2.setGerenteId(1L);
        projetoRepository.save(projeto2);

        mockMvc.perform(get("/projetos")
                        .param("nome", "Vendas")
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("Teste de Integração - Deve listar projetos com filtro por status")
    public void deveListarProjetosComFiltroStatus() throws Exception {
        ProjetoEntity projeto1 = new ProjetoEntity();
        projeto1.setNome("Projeto1");
        projeto1.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto1.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto1.setOrcamentoTotal(new BigDecimal("100000"));
        projeto1.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
        projeto1.setGerenteId(1L);
        projetoRepository.save(projeto1);

        ProjetoEntity projeto2 = new ProjetoEntity();
        projeto2.setNome("Projeto2");
        projeto2.setDataInicio(LocalDate.of(2025, 1, 15));
        projeto2.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
        projeto2.setOrcamentoTotal(new BigDecimal("100000"));
        projeto2.setStatusDoProjeto(StatusDoProjeto.INICIADO);
        projeto2.setGerenteId(1L);
        projetoRepository.save(projeto2);

        mockMvc.perform(get("/projetos")
                        .param("status", "EM_ANALISE")
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].statusDoProjeto").value("EM_ANALISE"));
    }

    @Test
    @DisplayName("Teste de Integração - Deve listar projetos com paginação")
    public void deveListarProjetosComPaginacao() throws Exception {
        // Criar 3 projetos
        for (int i = 1; i <= 3; i++) {
            ProjetoEntity projeto = new ProjetoEntity();
            projeto.setNome("Projeto" + i);
            projeto.setDataInicio(LocalDate.of(2025, 1, 15));
            projeto.setPrevisaoTermino(LocalDate.of(2025, 5, 15));
            projeto.setOrcamentoTotal(new BigDecimal("100000"));
            projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANALISE);
            projeto.setGerenteId(1L);
            projetoRepository.save(projeto);
        }

        mockMvc.perform(get("/projetos")
                        .param("page", "0")
                        .param("size", "2")
                        .with(httpBasic("admin", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2));
    }
}