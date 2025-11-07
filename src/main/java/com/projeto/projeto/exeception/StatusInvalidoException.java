package com.projeto.projeto.exeception;

public class StatusInvalidoException extends RuntimeException{

    public StatusInvalidoException(String mensagem){
        super(mensagem);
    }
}
