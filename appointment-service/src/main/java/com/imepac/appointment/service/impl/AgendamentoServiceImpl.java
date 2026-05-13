package com.imepac.appointment.service.impl;

import com.imepac.appointment.converter.AgendamentoConversor;
import com.imepac.appointment.dto.*;
import com.imepac.appointment.entity.Agendamento;
import com.imepac.appointment.entity.Paciente;
import com.imepac.appointment.enums.StatusAgendamento;
import com.imepac.appointment.exception.AgendamentoNaoEncontradoException;
import com.imepac.appointment.exception.PacienteNaoEncontradoException;
import com.imepac.appointment.repository.AgendamentoRepository;
import com.imepac.appointment.repository.PacienteRepository;
import com.imepac.appointment.service.AgendamentoService;
import com.imepac.commons.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoServiceImpl implements AgendamentoService {

    private final PacienteRepository pacienteRepository;
    private final AgendamentoRepository agendamentoRepository;

    @Override
    @Transactional
    public RespostaPaciente cadastrarPaciente(CriarPacienteRequest request) {
        log.info("Criando paciente com cpf: {}", request.getCpf());

        if (pacienteRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Paciente já cadastrado com o CPF: " + request.getCpf());
        }
        if (pacienteRepository.existsByRg(request.getRg())) {
            throw new BusinessException("Paciente já cadastrado com o RG: " + request.getRg());
        }

        Paciente paciente = AgendamentoConversor.toEntity(request);
        Paciente salvo = pacienteRepository.save(paciente);
        return AgendamentoConversor.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaPaciente> listarPacientes() {
        return pacienteRepository.findAll().stream().map(AgendamentoConversor::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaPaciente buscarPacientePorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoEncontradoException(id));
        return AgendamentoConversor.toResponse(paciente);
    }

    @Override
    @Transactional
    public RespostaPaciente atualizarPaciente(Long id, AtualizarPacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoEncontradoException(id));

        if (request.getNomeCompleto() != null) paciente.setNomeCompleto(request.getNomeCompleto());
        if (request.getTelefoneFixo() != null) paciente.setTelefoneFixo(request.getTelefoneFixo());
        if (request.getTelefoneCelular() != null) paciente.setTelefoneCelular(request.getTelefoneCelular());

        if (request.getRua() != null) paciente.setRua(request.getRua());
        if (request.getNumero() != null) paciente.setNumero(request.getNumero());
        if (request.getComplemento() != null) paciente.setComplemento(request.getComplemento());
        if (request.getBairro() != null) paciente.setBairro(request.getBairro());
        if (request.getCidade() != null) paciente.setCidade(request.getCidade());
        if (request.getEstado() != null) paciente.setEstado(request.getEstado());
        if (request.getCep() != null) paciente.setCep(request.getCep());

        if (request.getPossuiSeguro() != null) paciente.setPossuiSeguro(request.getPossuiSeguro());
        if (request.getNomeEmpresaSeguro() != null) paciente.setNomeEmpresaSeguro(request.getNomeEmpresaSeguro());
        if (request.getGenero() != null) paciente.setGenero(request.getGenero());
        if (request.getDataNascimento() != null) paciente.setDataNascimento(request.getDataNascimento());

        Paciente salvo = pacienteRepository.save(paciente);
        return AgendamentoConversor.toResponse(salvo);
    }

    @Override
    @Transactional
    public RespostaAgendamento agendarConsulta(CriarAgendamentoRequest request) {
        log.info("Agendando consulta para o paciente {} em {}", request.getPacienteId(), request.getAgendadoEm());

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new PacienteNaoEncontradoException(request.getPacienteId()));

        boolean conflito = agendamentoRepository.existsByMedicoIdAndAgendadoEmAndStatusNotIn(
                request.getMedicoId(),
                request.getAgendadoEm(),
                List.of(StatusAgendamento.CANCELADO, StatusAgendamento.RETORNO_CANCELADO)
        );

        if (conflito) {
            throw new BusinessException("Horário já reservado para este médico neste horário");
        }

        Agendamento agendamento = AgendamentoConversor.toEntity(request);
        agendamento.setPacienteId(paciente.getId());

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return AgendamentoConversor.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaAgendamento> listarConsultasPorPaciente(Long pacienteId) {
        return agendamentoRepository.findAllByPacienteIdAndStatusNot(pacienteId, StatusAgendamento.CANCELADO)
                .stream()
                .map(AgendamentoConversor::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaAgendamento> encontrarDisponibilidade(Long medicoId, LocalDateTime inicio, LocalDateTime fim) {
        List<RespostaAgendamento> disponiveis = new ArrayList<>();

        LocalDateTime cursor = inicio;
        while (!cursor.isAfter(fim)) {
            boolean ocupado = agendamentoRepository.existsByMedicoIdAndAgendadoEmAndStatusNotIn(
                    medicoId,
                    cursor,
                    List.of(StatusAgendamento.CANCELADO, StatusAgendamento.RETORNO_CANCELADO)
            );

            if (!ocupado) {
                disponiveis.add(RespostaAgendamento.builder()
                        .medicoId(medicoId)
                        .agendadoEm(cursor)
                        .status(StatusAgendamento.AGENDADO)
                        .build());
            }

            cursor = cursor.plusHours(1);
        }

        return disponiveis;
    }

    @Override
    @Transactional
    public RespostaAgendamento cancelarConsulta(Long agendamentoId, CancelarAgendamentoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(agendamentoId));

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("A consulta já foi cancelada");
        }

        agendamento.setMotivoCancelamento(request.getMotivo());
        agendamento.setSenhaCancelamentoHash(String.valueOf(request.getSenha().hashCode()));
        agendamento.setStatus(StatusAgendamento.CANCELADO);

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return AgendamentoConversor.toResponse(salvo);
    }

    @Override
    @Transactional
    public RespostaAgendamento agendarRetorno(Long agendamentoId, AgendarRetornoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(agendamentoId));

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Não é possível agendar retorno para uma consulta cancelada");
        }

        boolean conflito = agendamentoRepository.existsByMedicoIdAndAgendadoEmAndStatusNotIn(
                agendamento.getMedicoId(),
                request.getAgendadoRetornoEm(),
                List.of(StatusAgendamento.CANCELADO, StatusAgendamento.RETORNO_CANCELADO)
        );

        if (conflito) {
            throw new BusinessException("Horário de retorno já reservado para este médico neste horário");
        }

        agendamento.setAgendamentoRetornoEm(request.getAgendadoRetornoEm());
        agendamento.setProntuario(request.getProntuario());
        agendamento.setStatus(StatusAgendamento.RETORNO_AGENDADO);

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return AgendamentoConversor.toResponse(salvo);
    }

    @Override
    @Transactional
    public RespostaAgendamento registrarProntuario(Long agendamentoId, String prontuario) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(agendamentoId));

        agendamento.setProntuario(prontuario);
        Agendamento salvo = agendamentoRepository.save(agendamento);
        return AgendamentoConversor.toResponse(salvo);
    }
}

