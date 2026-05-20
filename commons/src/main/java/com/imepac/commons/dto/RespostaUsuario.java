package com.imepac.commons.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class RespostaUsuario {

    private Long id;
    private String idUser;
    private Long funcionarioId;
    private Set<String> permissoes;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
