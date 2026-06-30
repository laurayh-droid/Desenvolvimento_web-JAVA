package com.imepac.attendance.service;

import com.imepac.attendance.repository.AtendimentoRepository;
import com.imepac.attendance.service.impl.RegistrarAtendimentoServiceImpl;
import com.imepac.commons.dto.CriarAtendimentoRequest;
import com.imepac.commons.entity.Atendimento;
import com.imepac.commons.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarAtendimentoServiceImplTest {

    @Mock
    private AtendimentoRepository atendimentoRepository;

    @InjectMocks
    private RegistrarAtendimentoServiceImpl service;

    @Test
    void registrarAtendimento_alreadyExists_throws() {
        CriarAtendimentoRequest req = CriarAtendimentoRequest.builder()
                .agendamentoId(1L)
                .pacienteId(2L)
                .medicoId(3L)
                .diagnostico("d")
                .prontuario("p")
                .build();

        when(atendimentoRepository.findByAgendamentoId(1L)).thenReturn(Optional.of(new Atendimento()));

        assertThatThrownBy(() -> service.registrarAtendimento(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void registrarAtendimento_success_saves() {
        CriarAtendimentoRequest req = CriarAtendimentoRequest.builder()
                .agendamentoId(1L)
                .pacienteId(2L)
                .medicoId(3L)
                .diagnostico("d")
                .prontuario("p")
                .build();

        when(atendimentoRepository.findByAgendamentoId(1L)).thenReturn(Optional.empty());
        when(atendimentoRepository.save(any(Atendimento.class))).thenAnswer(inv -> {
            Atendimento a = inv.getArgument(0);
            a.setId(7L);
            return a;
        });

        var resp = service.registrarAtendimento(req);
        // response mapping returns object; assert id
        assertThat(resp.getId()).isEqualTo(7L);
        verify(atendimentoRepository).save(any(Atendimento.class));
    }
}
