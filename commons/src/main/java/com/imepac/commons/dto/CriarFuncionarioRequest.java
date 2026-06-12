package com.imepac.commons.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
public class CriarFuncionarioRequest {

    @NotBlank
    @Size(max = 200)
    private String nomeCompleto;

    @NotBlank
    @Size(max = 30)
    private String rg;

    @NotBlank
    @Size(max = 30)
    private String cpf;

    @NotNull
    @Past
    private LocalDateTime dataNascimento;

    @NotBlank
    @Size(max = 30)
    private String telefoneFixo;

    @NotBlank
    @Size(max = 30)
    private String telefoneCelular;

    @NotBlank
    @Size(max = 200)
    private String rua;

    @NotBlank
    @Size(max = 20)
    private String numero;

    @NotBlank
    @Size(max = 100)
    private String complemento;

    @NotBlank
    @Size(max = 100)
    private String bairro;

    @NotBlank
    @Size(max = 100)
    private String cidade;

    @NotBlank
    @Size(max = 2)
    private String estado;

    @NotBlank
    @Size(max = 20)
    private String cep;

    @NotBlank
    @Size(max = 50)
    private String numeroCtps;

    @NotBlank
    @Size(max = 50)
    private String numeroPis;
}
