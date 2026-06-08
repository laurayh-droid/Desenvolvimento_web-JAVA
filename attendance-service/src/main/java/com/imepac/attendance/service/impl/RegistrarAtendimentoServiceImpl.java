package com.imepac.attendance.service.impl;

import com.imepac.attendance.converter.AtendimentoConversor;
import com.imepac.attendance.repository.AtendimentoRepository;
import com.imepac.attendance.service.RegistrarAtendimentoService;
import com.imepac.commons.dto.CriarAtendimentoRequest;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.entity.Atendimento;
import com.imepac.commons.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrarAtendimentoServiceImpl implements RegistrarAtendimentoService {

    private final AtendimentoRepository atendimentoRepository;

    @Override
    @Transactional
    public RespostaAtendimento registrarAtendimento(CriarAtendimentoRequest request) {
        log.info("Registrando atendimento para agendamento {} e paciente {}", request.getAgendamentoId(), request.getPacienteId());

        Atendimento atendimento = AtendimentoConversor.toEntity(request);

        atendimentoRepository.findByAgendamentoId(request.getAgendamentoId())
                .ifPresent(a -> {
                    throw new BusinessException("Já existe atendimento registrado para o agendamento informado");
                });

        Atendimento salvo = atendimentoRepository.save(atendimento);
        return AtendimentoConversor.toResponse(salvo);
    }
}
