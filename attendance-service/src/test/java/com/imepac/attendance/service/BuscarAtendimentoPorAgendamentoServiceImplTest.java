package com.imepac.attendance.service;

import com.imepac.attendance.repository.AtendimentoRepository;
import com.imepac.attendance.service.impl.BuscarAtendimentoPorAgendamentoServiceImpl;
import com.imepac.commons.entity.Atendimento;
import com.imepac.commons.exception.AgendamentoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarAtendimentoPorAgendamentoServiceImplTest {

    @Mock
    private AtendimentoRepository repo;

    @InjectMocks
    private BuscarAtendimentoPorAgendamentoServiceImpl service;

    @Test
    void buscarPorAgendamento_notFound_throws() {
        when(repo.findByAgendamentoId(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscarPorAgendamento(5L)).isInstanceOf(AgendamentoNaoEncontradoException.class);
    }

    @Test
    void buscarPorAgendamento_success() {
        Atendimento a = new Atendimento();
        a.setId(2L);
        when(repo.findByAgendamentoId(2L)).thenReturn(Optional.of(a));

        var resp = service.buscarPorAgendamento(2L);
        assertThat(resp.getId()).isEqualTo(2L);
    }
}
