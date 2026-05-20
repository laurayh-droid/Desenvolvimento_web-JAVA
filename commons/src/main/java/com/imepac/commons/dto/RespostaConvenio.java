package com.imepac.commons.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespostaConvenio {

    private Long id;
    private String nomeEmpresa;
    private String cnpj;
    private String telefone;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
