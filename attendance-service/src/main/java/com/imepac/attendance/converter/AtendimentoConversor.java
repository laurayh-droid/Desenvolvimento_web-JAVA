package com.imepac.attendance.converter;

import com.imepac.commons.dto.CriarAtendimentoRequest;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.entity.Atendimento;


public final class AtendimentoConversor {

    private AtendimentoConversor() {
    }

    public static Atendimento toEntity(CriarAtendimentoRequest request) {
        // Garantia extra para evitar violação NOT NULL caso o @PrePersist não seja acionado
        // (ou em caso de mudanças futuras no fluxo de persistência).
        var agora = java.time.LocalDateTime.now();

        return Atendimento.builder()
                .agendamentoId(request.getAgendamentoId())
                .pacienteId(request.getPacienteId())
                .medicoId(request.getMedicoId())
                .registradoEm(agora)
                .diagnostico(request.getDiagnostico())
                .observacoes(request.getObservacoes())
                .prontuario(request.getProntuario())
                .receituario(request.getReceituario())
                .examesSolicitados(request.getExamesSolicitados())
                .build();
    }


    public static RespostaAtendimento toResponse(Atendimento atendimento) {
        return RespostaAtendimento.builder()
                .id(atendimento.getId())
                .agendamentoId(atendimento.getAgendamentoId())
                .pacienteId(atendimento.getPacienteId())
                .medicoId(atendimento.getMedicoId())
                .registradoEm(atendimento.getRegistradoEm())
                .diagnostico(atendimento.getDiagnostico())
                .observacoes(atendimento.getObservacoes())
                .prontuario(atendimento.getProntuario())
                .receituario(atendimento.getReceituario())
                .examesSolicitados(atendimento.getExamesSolicitados())
                .criadoEm(atendimento.getCriadoEm())
                .atualizadoEm(atendimento.getAtualizadoEm())
                .build();
    }
}

