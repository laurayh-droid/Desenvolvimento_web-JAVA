package com.imepac.attendance.service;

import com.imepac.commons.dto.*;



import java.util.List;

public interface AtendimentoService {

    RespostaAtendimento registrarAtendimento(CriarAtendimentoRequest request);

    List<RespostaAtendimento> listarAtendimentosPorPaciente(Long pacienteId);

    RespostaAtendimento buscarPorAgendamento(Long agendamentoId);
}

