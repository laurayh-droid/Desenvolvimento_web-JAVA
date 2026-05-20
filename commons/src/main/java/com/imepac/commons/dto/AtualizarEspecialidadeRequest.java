package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AtualizarEspecialidadeRequest {

    @Size(max = 200)
    private String descricao;
}
