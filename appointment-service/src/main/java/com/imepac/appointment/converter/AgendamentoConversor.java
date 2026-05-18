package com.imepac.appointment.converter;

import com.imepac.commons.dto.*;
import com.imepac.commons.entity.Agendamento;
import com.imepac.commons.entity.Paciente;
import com.imepac.commons.enums.StatusAgendamento;

import java.time.LocalDateTime;

public final class AgendamentoConversor {

    private AgendamentoConversor() {
    }

    public static Paciente toEntity(CriarPacienteRequest request) {
        return Paciente.builder()
                .nomeCompleto(request.getNomeCompleto())
                .rg(request.getRg())
                .cpf(request.getCpf())
                .dataNascimento(request.getDataNascimento())
                .genero(request.getGenero())
                .telefoneFixo(request.getTelefoneFixo())
                .telefoneCelular(request.getTelefoneCelular())
                .possuiSeguro(request.getPossuiSeguro())

                .nomeEmpresaSeguro(request.getNomeEmpresaSeguro())
                .rua(request.getRua())
                .numero(request.getNumero())
                .complemento(request.getComplemento())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .build();

    }

    public static RespostaPaciente toResponse(Paciente paciente) {
        return RespostaPaciente.builder()
                .id(paciente.getId())
                .nomeCompleto(paciente.getNomeCompleto())
                .rg(paciente.getRg())
                .cpf(paciente.getCpf())
                .dataNascimento(paciente.getDataNascimento())
                .genero(paciente.getGenero())
                .telefoneFixo(paciente.getTelefoneFixo())
                .telefoneCelular(paciente.getTelefoneCelular())
                .possuiSeguro(paciente.isPossuiSeguro())

                .nomeEmpresaSeguro(paciente.getNomeEmpresaSeguro())
                .rua(paciente.getRua())
                .numero(paciente.getNumero())
                .complemento(paciente.getComplemento())
                .bairro(paciente.getBairro())
                .cidade(paciente.getCidade())
                .estado(paciente.getEstado())
                .cep(paciente.getCep())
                .criadoEm(paciente.getCriadoEm())

                .atualizadoEm(paciente.getAtualizadoEm())
                .build();
    }

    public static Agendamento toEntity(CriarAgendamentoRequest request) {
        return Agendamento.builder()
                .pacienteId(request.getPacienteId())
                .agendadoEm(request.getAgendadoEm())
                .medicoId(request.getMedicoId())
                .status(StatusAgendamento.AGENDADO)
                .prontuario(request.getProntuario())
                .build();
    }

    public static RespostaAgendamento toResponse(Agendamento agendamento) {
        return RespostaAgendamento.builder()
                .id(agendamento.getId())
                .pacienteId(agendamento.getPacienteId())
                .medicoId(agendamento.getMedicoId())
                .agendadoEm(agendamento.getAgendadoEm())
                .agendamentoRetornoEm(agendamento.getAgendamentoRetornoEm())
                .status(agendamento.getStatus())
                .motivoCancelamento(agendamento.getMotivoCancelamento())
                .prontuario(agendamento.getProntuario())
                .criadoEm(agendamento.getCriadoEm())
                .atualizadoEm(agendamento.getAtualizadoEm())
                .build();
    }

    public static LocalDateTime toAgendamentoRetornoEm(LocalDateTime date) {
        return date;
    }
}

