package com.imepac.commons.dto;

import com.imepac.commons.enums.StatusAgendamento;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespostaAgendamento {

    private Long id;
    private Long pacienteId;
    private Long medicoId;

    private LocalDateTime agendadoEm;
    private LocalDateTime agendamentoRetornoEm;

    private StatusAgendamento status;

    private String motivoCancelamento;
    private String prontuario;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

