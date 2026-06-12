package com.imepac.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CriarAtendimentoRequest {

    @NotNull
    private Long agendamentoId;

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long medicoId;

    @NotBlank
    private String diagnostico;

    private String observacoes;

    @NotBlank
    private String prontuario;

    private String receituario;

    private String examesSolicitados;
}

