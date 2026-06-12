package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AtualizarEspecialidadeRequest {

    @Size(max = 200)
    private String descricao;
}
