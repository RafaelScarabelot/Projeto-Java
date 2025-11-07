package com.projeto.projeto.dtos;

import com.projeto.projeto.enums.AtribuicaoMembro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembroDTO {

    private Long id;
    private String nome;
    private AtribuicaoMembro atribuicaoMembro;
}
