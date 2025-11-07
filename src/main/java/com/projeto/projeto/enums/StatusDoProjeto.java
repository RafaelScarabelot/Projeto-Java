package com.projeto.projeto.enums;

import java.util.Arrays;
import java.util.List;

public enum StatusDoProjeto {
    EM_ANALISE,
    ANALISE_REALIZADA,
    ANALISE_APROVADA,
    INICIADO,
    PLANEJADO,
    EM_ANDAMENTO,
    ENCERRADO,
    CANCELADO;

    public List<StatusDoProjeto> proximoStatusPermitido() {
        switch (this) {
            case EM_ANALISE:
                return Arrays.asList(ANALISE_REALIZADA, CANCELADO);
            case ANALISE_REALIZADA:
                return Arrays.asList(ANALISE_APROVADA, CANCELADO);
            case ANALISE_APROVADA:
                return Arrays.asList(INICIADO, CANCELADO);
            case INICIADO:
                return Arrays.asList(PLANEJADO, CANCELADO);
            case PLANEJADO:
                return Arrays.asList(EM_ANDAMENTO, CANCELADO);
            case EM_ANDAMENTO:
                return Arrays.asList(ENCERRADO, CANCELADO);
            case ENCERRADO:
                return Arrays.asList(CANCELADO);
            case CANCELADO:
                return Arrays.asList();
            default:
                return Arrays.asList();
        }
    }
}

