package com.projeto.projeto.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;


@Getter
@Setter
@Entity
public class AlocacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ProjetoEntity projeto;

    @ManyToOne(optional = false)
    private MembroEntity membro;

    private LocalDate dataAlocacao;



}
