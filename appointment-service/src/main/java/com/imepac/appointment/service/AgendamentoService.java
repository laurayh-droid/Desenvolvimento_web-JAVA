package com.imepac.appointment.service;

import com.imepac.appointment.dto.*;

import java.time.LocalDateTime;
import java.util.List;


public interface AgendamentoService {

    RespostaPaciente cadastrarPaciente(CriarPacienteRequest request);

    List<RespostaPaciente> listarPacientes();

    RespostaPaciente buscarPacientePorId(Long id);

    RespostaPaciente atualizarPaciente(Long id, AtualizarPacienteRequest request);

    RespostaAgendamento agendarConsulta(CriarAgendamentoRequest request);

    List<RespostaAgendamento> listarConsultasPorPaciente(Long pacienteId);

    List<RespostaAgendamento> encontrarDisponibilidade(Long medicoId, LocalDateTime inicio, LocalDateTime fim);

    RespostaAgendamento cancelarConsulta(Long agendamentoId, CancelarAgendamentoRequest request);

    RespostaAgendamento agendarRetorno(Long agendamentoId, AgendarRetornoRequest request);

    RespostaAgendamento registrarProntuario(Long agendamentoId, String prontuario);
}

