package com.projeto.projeto.exeception;

public class OperacaoNaoPermitidaException extends  RuntimeException{

    public OperacaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}
