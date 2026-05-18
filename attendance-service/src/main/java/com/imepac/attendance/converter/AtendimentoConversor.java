package com.imepac.attendance.converter;

import com.imepac.commons.dto.CriarAtendimentoRequest;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.entity.Atendimento;


public final class AtendimentoConversor {

    private AtendimentoConversor() {
    }

    public static Atendimento toEntity(CriarAtendimentoRequest request) {
        return Atendimento.builder()
                .agendamentoId(request.getAgendamentoId())
                .pacienteId(request.getPacienteId())
                .medicoId(request.getMedicoId())
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

