package com.projeto.projeto.exeception;

public class ProjetoNotFoundException extends RuntimeException{

    public ProjetoNotFoundException(Long id) {
        super("Projeto não encontrado com o ID: " + id);
    }
}
