package com.imepac.commons.dto;

import com.imepac.commons.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
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

    @Size(max = 30)
    private String telefoneFixo;

    @Size(max = 30)
    private String telefoneCelular;

    @NotNull
    private Boolean possuiSeguro;

    @Size(max = 200)
    private String nomeEmpresaSeguro;

    @Size(max = 200)
    private String rua;

    @Size(max = 20)
    private String numero;

    @Size(max = 100)
    private String complemento;

    @Size(max = 100)
    private String bairro;

    @Size(max = 100)
    private String cidade;

    @Size(max = 2)
    private String estado;

    @Size(max = 20)
    private String cep;
}

