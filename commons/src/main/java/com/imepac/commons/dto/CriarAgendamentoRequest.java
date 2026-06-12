package com.imepac.commons.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.Jacksonized;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
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

