package com.projeto.projeto.entity;

import com.projeto.projeto.enums.AtribuicaoMembro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;




@Getter
@Setter

@Entity
public class MembroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Enumerated(EnumType.STRING)
    private AtribuicaoMembro atribuicaoMembro;



}
