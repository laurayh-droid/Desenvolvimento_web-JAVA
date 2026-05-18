package com.imepac.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelarAgendamentoRequest {

    @NotBlank
    @Size(max = 500)
    private String motivo;

    @NotBlank
    @Size(max = 50)
    private String senha;
}

