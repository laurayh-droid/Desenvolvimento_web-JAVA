package com.imepac.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CriarMedicoRequest {

    @NotBlank
    @Size(max = 200)
    private String nome;

    @NotBlank
    @Size(max = 50)
    private String crm;

    @NotNull
    private Long especialidadeId;
}
