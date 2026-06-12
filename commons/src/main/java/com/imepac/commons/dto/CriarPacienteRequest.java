package com.imepac.commons.dto;

import com.imepac.commons.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.Jacksonized;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
public class CriarPacienteRequest {

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

    @NotNull
    private Gender genero;

    @NotBlank
    @Size(max = 30)
    private String telefoneFixo;

    @NotBlank
    @Size(max = 30)
    private String telefoneCelular;

    @NotNull
    private Boolean possuiSeguro;

    @Size(max = 200)
    private String nomeEmpresaSeguro;

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
}

