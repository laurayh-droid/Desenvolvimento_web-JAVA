package com.imepac.appointment.service;

import com.imepac.commons.dto.AgendarRetornoRequest;
import com.imepac.commons.dto.CancelarAgendamentoRequest;
import com.imepac.commons.dto.CriarAgendamentoRequest;
import com.imepac.commons.dto.RespostaAgendamento;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaService {

    RespostaAgendamento agendarConsulta(CriarAgendamentoRequest request);

    List<RespostaAgendamento> listarConsultasPorPaciente(Long pacienteId);

    List<RespostaAgendamento> encontrarDisponibilidade(Long medicoId, LocalDateTime inicio, LocalDateTime fim);

    RespostaAgendamento cancelarConsulta(Long agendamentoId, CancelarAgendamentoRequest request);

    RespostaAgendamento agendarRetorno(Long agendamentoId, AgendarRetornoRequest request);

    RespostaAgendamento registrarProntuario(Long agendamentoId, String prontuario);
}
