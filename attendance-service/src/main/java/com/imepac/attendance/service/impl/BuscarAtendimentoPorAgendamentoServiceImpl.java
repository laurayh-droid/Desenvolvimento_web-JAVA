package com.imepac.attendance.service.impl;

import com.imepac.attendance.converter.AtendimentoConversor;
import com.imepac.attendance.repository.AtendimentoRepository;
import com.imepac.attendance.service.BuscarAtendimentoPorAgendamentoService;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.exception.AgendamentoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuscarAtendimentoPorAgendamentoServiceImpl implements BuscarAtendimentoPorAgendamentoService {

    private final AtendimentoRepository atendimentoRepository;

    @Override
    @Transactional(readOnly = true)
    public RespostaAtendimento buscarPorAgendamento(Long agendamentoId) {
        return atendimentoRepository.findByAgendamentoId(agendamentoId)
                .map(AtendimentoConversor::toResponse)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(agendamentoId));
    }
}
