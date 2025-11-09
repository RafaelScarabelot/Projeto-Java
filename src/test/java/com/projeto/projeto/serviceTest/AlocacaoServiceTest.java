package com.projeto.projeto.serviceTest;

import com.projeto.projeto.dtos.MembroDTO;
import com.projeto.projeto.entity.AlocacaoEntity;
import com.projeto.projeto.entity.MembroEntity;
import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.enums.AtribuicaoMembro;
import com.projeto.projeto.enums.StatusDoProjeto;
import com.projeto.projeto.repository.AlocacaoRepository;
import com.projeto.projeto.repository.ProjetoRepository;
import com.projeto.projeto.service.AlocacaoService;
import com.projeto.projeto.service.MembroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlocacaoServiceTest {

    @Mock
    private AlocacaoRepository alocacaoRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private MembroService membroService;

    @InjectMocks
    private AlocacaoService alocacaoService;

    @Test
    @DisplayName("Teste Unitário - Deve alocar membro com sucesso")
    void alocarMembro_DeveAlocarComSucesso() {
        ProjetoEntity projeto = criarProjetoTeste();
        MembroDTO membro = new MembroDTO(1L, "João", AtribuicaoMembro.FUNCIONARIO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroService.buscarPorId(1L)).thenReturn(membro);
        when(projetoRepository.countProjetosAtivosByMembroId(1L)).thenReturn(0L);

        alocacaoService.alocarMembro(1L, 1L);

        verify(alocacaoRepository).save(any(AlocacaoEntity.class));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção quando projeto não existe")
    void alocarMembro_DeveLancarExcecao_ProjetoNaoExiste() {
        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> alocacaoService.alocarMembro(999L, 1L)
        );

        assertEquals("Projeto não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção quando projeto está encerrado")
    void alocarMembro_DeveLancarExcecao_ProjetoEncerrado() {
        ProjetoEntity projeto = criarProjetoTeste();
        projeto.setStatusDoProjeto(StatusDoProjeto.ENCERRADO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> alocacaoService.alocarMembro(1L, 1L)
        );

        assertTrue(exception.getMessage().contains("Não é possível alocar membros"));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção quando membro não é funcionário")
    void alocarMembro_DeveLancarExcecao_MembroNaoFuncionario() {
        ProjetoEntity projeto = criarProjetoTeste();
        MembroDTO gerente = new MembroDTO(1L, "João", AtribuicaoMembro.GERENTE);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroService.buscarPorId(1L)).thenReturn(gerente);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> alocacaoService.alocarMembro(1L, 1L)
        );

        assertEquals("Apenas funcionários podem ser alocados em projetos", exception.getMessage());
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção quando projeto já tem 10 membros")
    void alocarMembro_DeveLancarExcecao_ProjetoLotado() {
        ProjetoEntity projeto = criarProjetoTeste();
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            alocacoes.add(new AlocacaoEntity());
        }
        projeto.setAlocacoes(alocacoes);

        MembroDTO membro = new MembroDTO(1L, "João", AtribuicaoMembro.FUNCIONARIO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroService.buscarPorId(1L)).thenReturn(membro);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> alocacaoService.alocarMembro(1L, 1L)
        );

        assertEquals("Projeto já possui o máximo de 10 membros", exception.getMessage());
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção quando membro já está em 3 projetos ativos")
    void alocarMembro_MembroEm3ProjetosAtivos() {
        ProjetoEntity projeto = criarProjetoTeste();
        MembroDTO membro = new MembroDTO(1L, "João", AtribuicaoMembro.FUNCIONARIO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroService.buscarPorId(1L)).thenReturn(membro);
        when(projetoRepository.countProjetosAtivosByMembroId(1L)).thenReturn(3L);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> alocacaoService.alocarMembro(1L, 1L)
        );

        assertEquals("Membro já está alocado em 3 projetos ativos", exception.getMessage());
    }

    @Test
    @DisplayName("Teste Unitário - Deve desalocar membro com sucesso")
    void desalocarMembro_DeveDesalocarComSucesso() {
        ProjetoEntity projeto = criarProjetoTeste();
        AlocacaoEntity alocacao = criarAlocacaoTeste(1L);
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        alocacoes.add(alocacao);
        alocacoes.add(criarAlocacaoTeste(2L));
        projeto.setAlocacoes(alocacoes);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        alocacaoService.desalocarMembro(1L, 1L);

        verify(alocacaoRepository).delete(alocacao);
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção ao desalocar último membro")
    void desalocarMembro_DeveLancarExcecao_UltimoMembro() {
        ProjetoEntity projeto = criarProjetoTeste();
        AlocacaoEntity alocacao = criarAlocacaoTeste(1L);
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        alocacoes.add(alocacao);
        projeto.setAlocacoes(alocacoes);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> alocacaoService.desalocarMembro(1L, 1L)
        );

        assertEquals("Projeto deve ter pelo menos 1 membro alocado", exception.getMessage());
    }

    @Test
    @DisplayName("Teste Unitário - Deve listar membros do projeto")
    void listarMembrosProj_DeveRetornarLista() {
        ProjetoEntity projeto = criarProjetoTeste();
        AlocacaoEntity alocacao = criarAlocacaoTeste(1L);
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        alocacoes.add(alocacao);
        projeto.setAlocacoes(alocacoes);

        MembroDTO membro = new MembroDTO(1L, "João", AtribuicaoMembro.FUNCIONARIO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroService.buscarPorId(1L)).thenReturn(membro);

        List<MembroDTO> resultado = alocacaoService.listarMembrosProj(1L);

        assertEquals(1, resultado.size());
        assertEquals("João", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Teste Unitário - Deve editar alocação com sucesso")
    void editarAlocacao_DeveAtualizarData() {
        ProjetoEntity projeto = criarProjetoTeste();
        AlocacaoEntity alocacao = criarAlocacaoTeste(1L);
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        alocacoes.add(alocacao);
        projeto.setAlocacoes(alocacoes);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        alocacaoService.editarAlocacao(1L, 1L);

        verify(alocacaoRepository).save(alocacao);
        assertEquals(LocalDate.now(), alocacao.getDataAlocacao());
    }

    private ProjetoEntity criarProjetoTeste() {
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setId(1L);
        projeto.setStatusDoProjeto(StatusDoProjeto.EM_ANDAMENTO);
        projeto.setAlocacoes(new HashSet<>());
        return projeto;
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção quando membro já está alocado no projeto")
    void alocarMembro_MembroJaAlocado() {
        ProjetoEntity projeto = criarProjetoTeste();
        AlocacaoEntity alocacao = criarAlocacaoTeste(1L);
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        alocacoes.add(alocacao);
        projeto.setAlocacoes(alocacoes);
        
        MembroDTO membro = new MembroDTO(1L, "João", AtribuicaoMembro.FUNCIONARIO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroService.buscarPorId(1L)).thenReturn(membro);

        assertThrows(IllegalArgumentException.class, 
            () -> alocacaoService.alocarMembro(1L, 1L));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção quando projeto está cancelado")
    void alocarMembro_ProjetoCancelado() {
        ProjetoEntity projeto = criarProjetoTeste();
        projeto.setStatusDoProjeto(StatusDoProjeto.CANCELADO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        assertThrows(IllegalArgumentException.class, 
            () -> alocacaoService.alocarMembro(1L, 1L));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção ao desalocar membro não alocado")
    void desalocarMembro_MembroNaoAlocado() {
        ProjetoEntity projeto = criarProjetoTeste();
        AlocacaoEntity alocacao = criarAlocacaoTeste(2L);
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        alocacoes.add(alocacao);
        projeto.setAlocacoes(alocacoes);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        assertThrows(IllegalArgumentException.class, 
            () -> alocacaoService.desalocarMembro(1L, 1L));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção ao desalocar de projeto inexistente")
    void desalocarMembro_ProjetoNaoExiste() {
        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> alocacaoService.desalocarMembro(999L, 1L));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção ao listar membros de projeto inexistente")
    void listarMembrosProj_ProjetoNaoExiste() {
        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> alocacaoService.listarMembrosProj(999L));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção ao editar alocação de projeto inexistente")
    void editarAlocacao_ProjetoNaoExiste() {
        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> alocacaoService.editarAlocacao(999L, 1L));
    }

    @Test
    @DisplayName("Teste Unitário - Deve lançar exceção ao editar alocação de membro não alocado")
    void editarAlocacao_MembroNaoAlocado() {
        ProjetoEntity projeto = criarProjetoTeste();
        AlocacaoEntity alocacao = criarAlocacaoTeste(2L);
        Set<AlocacaoEntity> alocacoes = new HashSet<>();
        alocacoes.add(alocacao);
        projeto.setAlocacoes(alocacoes);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        assertThrows(IllegalArgumentException.class, 
            () -> alocacaoService.editarAlocacao(1L, 1L));
    }

    private AlocacaoEntity criarAlocacaoTeste(Long membroId) {
        MembroEntity membro = new MembroEntity();
        membro.setId(membroId);

        AlocacaoEntity alocacao = new AlocacaoEntity();
        alocacao.setMembro(membro);
        alocacao.setDataAlocacao(LocalDate.now().minusDays(1));
        return alocacao;
    }
}