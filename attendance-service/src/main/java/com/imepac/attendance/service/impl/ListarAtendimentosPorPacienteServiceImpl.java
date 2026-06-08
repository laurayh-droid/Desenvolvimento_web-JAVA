package com.imepac.attendance.service.impl;

import com.imepac.attendance.converter.AtendimentoConversor;
import com.imepac.attendance.repository.AtendimentoRepository;
import com.imepac.attendance.service.ListarAtendimentosPorPacienteService;
import com.imepac.commons.dto.RespostaAtendimento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListarAtendimentosPorPacienteServiceImpl implements ListarAtendimentosPorPacienteService {

    private final AtendimentoRepository atendimentoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RespostaAtendimento> listarAtendimentosPorPaciente(Long pacienteId) {
        return atendimentoRepository.findAllByPacienteIdOrderByRegistradoEmAsc(pacienteId)
                .stream()
                .map(AtendimentoConversor::toResponse)
                .toList();
    }
}
