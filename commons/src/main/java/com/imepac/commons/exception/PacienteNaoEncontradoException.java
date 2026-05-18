package com.imepac.commons.exception;

public class PacienteNaoEncontradoException extends EntityNotFoundException {

    public PacienteNaoEncontradoException(Long id) {
        super("Paciente", id);
    }
}

