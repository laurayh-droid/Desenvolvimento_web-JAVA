package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AtualizarConvenioRequest {

    @Size(max = 200)
    private String nomeEmpresa;

    @Size(max = 30)
    private String cnpj;

    @Size(max = 30)
    private String telefone;
}
