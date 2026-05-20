package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AtualizarMedicoRequest {

    @Size(max = 200)
    private String nome;

    @Size(max = 50)
    private String crm;

    private Long especialidadeId;
}
