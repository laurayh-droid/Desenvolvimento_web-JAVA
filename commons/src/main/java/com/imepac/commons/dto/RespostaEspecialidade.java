package com.imepac.commons.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespostaEspecialidade {

    private Long id;
    private String descricao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
