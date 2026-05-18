package com.imepac.attendance.service.impl;

import com.imepac.attendance.converter.AtendimentoConversor;
import com.imepac.commons.dto.CriarAtendimentoRequest;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.entity.Atendimento;
import com.imepac.attendance.repository.AtendimentoRepository;
import com.imepac.attendance.service.AtendimentoService;

import com.imepac.commons.exception.AgendamentoNaoEncontradoException;
import com.imepac.commons.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtendimentoServiceImpl implements AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;

    @Override
    @Transactional
    public RespostaAtendimento registrarAtendimento(CriarAtendimentoRequest request) {
        log.info("Registrando atendimento para agendamento {} e paciente {}", request.getAgendamentoId(), request.getPacienteId());

        Atendimento atendimento = AtendimentoConversor.toEntity(request);

        // Regra simples (sem performance/infra): impede registro duplicado do mesmo agendamento
        atendimentoRepository.findByAgendamentoId(request.getAgendamentoId())
                .ifPresent(a -> {
                    throw new BusinessException("Já existe atendimento registrado para o agendamento informado");
                });

        Atendimento salvo = atendimentoRepository.save(atendimento);
        return AtendimentoConversor.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaAtendimento> listarAtendimentosPorPaciente(Long pacienteId) {
        return atendimentoRepository.findAllByPacienteIdOrderByRegistradoEmAsc(pacienteId)
                .stream()
                .map(AtendimentoConversor::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaAtendimento buscarPorAgendamento(Long agendamentoId) {
        Atendimento atendimento = atendimentoRepository.findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(agendamentoId));

        return AtendimentoConversor.toResponse(atendimento);
    }
}

