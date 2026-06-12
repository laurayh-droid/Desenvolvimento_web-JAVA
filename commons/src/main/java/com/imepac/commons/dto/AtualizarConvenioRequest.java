package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AtualizarConvenioRequest {

    @Size(max = 200)
    private String nomeEmpresa;

    @Size(max = 30)
    private String cnpj;

    @Size(max = 30)
    private String telefone;
}
