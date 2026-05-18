package com.imepac.commons.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CriarAgendamentoRequest {

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long medicoId;

    @NotNull
    @Future
    private LocalDateTime agendadoEm;

    private String prontuario;
}

