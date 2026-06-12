package com.imepac.commons.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
public class AgendarRetornoRequest {

    @NotNull
    @Future
    private LocalDateTime agendadoRetornoEm;

    private String prontuario;
}

