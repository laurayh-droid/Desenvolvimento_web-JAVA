package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.Jacksonized;

import java.util.Set;

@Getter
@Builder
@Jacksonized
public class AtualizarUsuarioRequest {

    @Size(max = 100)
    private String idUser;

    @Size(max = 200)
    private String senha;

    private Long funcionarioId;

    private Long perfilId;

    @Size(min = 1)
    private Set<@Size(max = 100) String> permissoes;
}
