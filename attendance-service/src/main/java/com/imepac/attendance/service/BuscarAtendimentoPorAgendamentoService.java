package com.imepac.attendance.service;

import com.imepac.commons.dto.RespostaAtendimento;

public interface BuscarAtendimentoPorAgendamentoService {

    RespostaAtendimento buscarPorAgendamento(Long agendamentoId);
}
