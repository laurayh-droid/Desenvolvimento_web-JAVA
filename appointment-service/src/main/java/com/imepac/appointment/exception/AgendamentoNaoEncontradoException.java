package com.imepac.appointment.exception;

import com.imepac.commons.exception.EntityNotFoundException;

public class AgendamentoNaoEncontradoException extends EntityNotFoundException {

    public AgendamentoNaoEncontradoException(Long id) {
        super("Agendamento", id);
    }
}

