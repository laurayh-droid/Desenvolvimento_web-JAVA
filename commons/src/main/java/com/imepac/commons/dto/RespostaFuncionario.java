package com.imepac.commons.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespostaFuncionario {

    private Long id;
    private String nomeCompleto;
    private String rg;
    private String cpf;
    private LocalDateTime dataNascimento;
    private String telefoneFixo;
    private String telefoneCelular;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String numeroCtps;
    private String numeroPis;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
