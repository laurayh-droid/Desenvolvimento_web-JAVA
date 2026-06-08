package com.imepac.appointment.service.impl;

import com.imepac.appointment.converter.AgendamentoConversor;
import com.imepac.appointment.repository.AgendamentoRepository;
import com.imepac.appointment.repository.PacienteRepository;
import com.imepac.appointment.service.ConsultaService;
import com.imepac.commons.dto.AgendarRetornoRequest;
import com.imepac.commons.dto.CancelarAgendamentoRequest;
import com.imepac.commons.dto.CriarAgendamentoRequest;
import com.imepac.commons.dto.RespostaAgendamento;
import com.imepac.commons.entity.Agendamento;
import com.imepac.commons.entity.Paciente;
import com.imepac.commons.enums.StatusAgendamento;
import com.imepac.commons.exception.AgendamentoNaoEncontradoException;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.PacienteNaoEncontradoException;
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
public class ConsultaServiceImpl implements ConsultaService {

    private final PacienteRepository pacienteRepository;
    private final AgendamentoRepository agendamentoRepository;

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
