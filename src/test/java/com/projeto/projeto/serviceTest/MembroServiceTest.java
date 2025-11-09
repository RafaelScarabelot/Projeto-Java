package com.projeto.projeto.serviceTest;


import com.projeto.projeto.dtos.MembroDTO;
import com.projeto.projeto.enums.AtribuicaoMembro;
import com.projeto.projeto.service.MembroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MembroServiceTest {

     @Mock
    private RestTemplate restTemplate;

     @InjectMocks
     private MembroService membroService;


     @Test
     @DisplayName("Teste Unitário - Deve retornar Lista de membros")
      public void deveRetornarListaDeMembros(){

         MembroDTO membro1 = new MembroDTO();
         membro1.setNome("Rafael");
         membro1.setAtribuicaoMembro(AtribuicaoMembro.FUNCIONARIO);

         MembroDTO membro2 = new MembroDTO();
         membro2.setNome("Carlos");
         membro2.setAtribuicaoMembro(AtribuicaoMembro.GERENTE);

         List<MembroDTO> membros = Arrays.asList(membro1,membro2);

         MembroDTO[] membrosArray = {membro1, membro2};
         when(restTemplate.getForObject("http://localhost:8080/api/membros", MembroDTO[].class))
                 .thenReturn(membrosArray);

         List<MembroDTO> resultado = membroService.listarMembros();

          assertEquals(2,resultado.size());
          assertEquals("Rafael", resultado.get(0).getNome());
          assertEquals("Carlos", resultado.get(1).getNome());

     }

     @Test
     @DisplayName("Teste Unitário - Deve retornar um membro por id")
     public void deveRetornarUmMembroPorId(){

         MembroDTO membro1 = new MembroDTO();
         membro1.setId(1L);
         membro1.setNome("Rafael");
         membro1.setAtribuicaoMembro(AtribuicaoMembro.FUNCIONARIO);

         when(restTemplate.getForObject("http://localhost:8080/api/membros/1", MembroDTO.class))
                 .thenReturn(membro1);

         MembroDTO resultado = membroService.buscarPorId(1L);

         assertEquals(1L, resultado.getId());
         assertEquals("Rafael", resultado.getNome());
         assertEquals(AtribuicaoMembro.FUNCIONARIO, resultado.getAtribuicaoMembro());
     }

     @Test
     @DisplayName("Teste Unitário - Deve criar um membro")
     public void deveCriarUmMembro(){
         MembroDTO membro1 = new MembroDTO();
         membro1.setId(1L);
         membro1.setNome("Rafael");
         membro1.setAtribuicaoMembro(AtribuicaoMembro.FUNCIONARIO);

         when(restTemplate.postForObject("http://localhost:8080/api/membros", membro1, MembroDTO.class))
                 .thenReturn(membro1);

         MembroDTO resultado = membroService.criarMembro(membro1);
         
         assertEquals(1L, resultado.getId());
         assertEquals("Rafael", resultado.getNome());
         assertEquals(AtribuicaoMembro.FUNCIONARIO, resultado.getAtribuicaoMembro());

     }

     @Test
     @DisplayName("Teste Unitário - Deve editar um membro")
     public void deveEditarUmMembro(){
         MembroDTO membroAtualizado = new MembroDTO();
         membroAtualizado.setId(1L);
         membroAtualizado.setNome("Carlos");
         membroAtualizado.setAtribuicaoMembro(AtribuicaoMembro.GERENTE);

         doNothing().when(restTemplate).put("http://localhost:8080/api/membros/1", membroAtualizado);

         membroService.atualizarMembro(1L, membroAtualizado);

         verify(restTemplate).put("http://localhost:8080/api/membros/1", membroAtualizado);
     }

    @Test
    @DisplayName("Teste Unitário - Deve deletar um membro")
    public void deveDeletarUmMembro(){

        doNothing().when(restTemplate).delete("http://localhost:8080/api/membros/1");

        membroService.deletarMembro(1L);

        verify(restTemplate).delete("http://localhost:8080/api/membros/1");
    }
}