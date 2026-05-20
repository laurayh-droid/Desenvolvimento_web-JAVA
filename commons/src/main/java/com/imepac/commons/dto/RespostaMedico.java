package com.imepac.commons.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespostaMedico {

    private Long id;
    private String nome;
    private String crm;
    private Long especialidadeId;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
