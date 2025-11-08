package com.projeto.projeto.dtos;

import com.projeto.projeto.enums.StatusDoProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoDTO {
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;
    
    @NotNull(message = "Previsão de término é obrigatória")
    private LocalDate previsaoTermino;
    
    private LocalDate dataRealTermino;
    
    @NotNull(message = "Orçamento total é obrigatório")
    @Positive(message = "Orçamento deve ser positivo")
    private BigDecimal orcamentoTotal;
    
    private String descricao;
    
    @NotNull(message = "Gerente é obrigatório")
    private Long gerenteId;
    
    private StatusDoProjeto statusDoProjeto;
    private String classificacaoRisco;
}
