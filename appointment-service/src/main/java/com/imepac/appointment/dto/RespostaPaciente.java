package com.imepac.appointment.dto;

import com.imepac.appointment.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespostaPaciente {

    private Long id;
    private String nomeCompleto;
    private String rg;
    private String cpf;
    private LocalDateTime dataNascimento;
    private Gender genero;
    private String telefoneFixo;

    private String telefoneCelular;

    private Boolean possuiSeguro;
    private String nomeEmpresaSeguro;

    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;
}

