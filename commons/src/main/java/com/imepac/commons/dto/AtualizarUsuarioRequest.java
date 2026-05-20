package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class AtualizarUsuarioRequest {

    @Size(max = 100)
    private String idUser;

    @Size(max = 200)
    private String senha;

    private Long funcionarioId;

    @Size(min = 1)
    private Set<@Size(max = 100) String> permissoes;
}
