package com.imepac.appointment.exception;

import com.imepac.commons.exception.EntityNotFoundException;

public class PacienteNaoEncontradoException extends EntityNotFoundException {

    public PacienteNaoEncontradoException(Long id) {
        super("Paciente", id);
    }
}

