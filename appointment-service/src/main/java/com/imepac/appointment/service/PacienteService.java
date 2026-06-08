package com.imepac.appointment.service;

import com.imepac.commons.dto.AtualizarPacienteRequest;
import com.imepac.commons.dto.CriarPacienteRequest;
import com.imepac.commons.dto.RespostaPaciente;

import java.util.List;

public interface PacienteService {

    RespostaPaciente cadastrarPaciente(CriarPacienteRequest request);

    List<RespostaPaciente> listarPacientes();

    RespostaPaciente buscarPacientePorId(Long id);

    RespostaPaciente atualizarPaciente(Long id, AtualizarPacienteRequest request);
}
