package com.imepac.commons.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
public class AtualizarFuncionarioRequest {

    @Size(max = 200)
    private String nomeCompleto;

    @Size(max = 30)
    private String rg;

    @Size(max = 30)
    private String cpf;

    private LocalDateTime dataNascimento;

    @Size(max = 30)
    private String telefoneFixo;

    @Size(max = 30)
    private String telefoneCelular;

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

    @Size(max = 50)
    private String numeroCtps;

    @Size(max = 50)
    private String numeroPis;
}
