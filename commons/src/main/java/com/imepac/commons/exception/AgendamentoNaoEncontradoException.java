package com.imepac.commons.exception;

public class AgendamentoNaoEncontradoException extends EntityNotFoundException {

    public AgendamentoNaoEncontradoException(Long id) {
        super("Agendamento", id);
    }
}

