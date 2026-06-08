package com.imepac.attendance.service;

import com.imepac.commons.dto.RespostaAtendimento;

import java.util.List;

public interface ListarAtendimentosPorPacienteService {

    List<RespostaAtendimento> listarAtendimentosPorPaciente(Long pacienteId);
}
