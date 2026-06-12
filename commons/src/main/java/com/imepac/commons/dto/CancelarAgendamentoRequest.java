package com.imepac.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CancelarAgendamentoRequest {

    @NotBlank
    @Size(max = 500)
    private String motivo;

    @NotBlank
    @Size(max = 50)
    private String senha;
}

