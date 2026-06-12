package com.imepac.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Getter
@Builder
@Jacksonized
public class CriarUsuarioRequest {

    @NotBlank
    @Size(max = 100)
    private String idUser;

    @NotBlank
    @Size(max = 200)
    private String senha;

    @NotNull
    private Long funcionarioId;

    @NotNull
    private Long perfilId;

    @NotNull
    @Size(min = 1)
    private Set<@Size(max = 100) String> permissoes;
}
