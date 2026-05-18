package com.imepac.commons.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespostaAtendimento {

    private Long id;
    private Long agendamentoId;
    private Long pacienteId;
    private Long medicoId;

    private LocalDateTime registradoEm;

    private String diagnostico;
    private String observacoes;

    private String prontuario;
    private String receituario;
    private String examesSolicitados;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

