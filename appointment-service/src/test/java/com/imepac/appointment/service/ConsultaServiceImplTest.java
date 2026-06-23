package com.imepac.appointment.service;

import com.imepac.appointment.repository.AgendamentoRepository;
import com.imepac.appointment.repository.PacienteRepository;
import com.imepac.appointment.service.impl.ConsultaServiceImpl;
import com.imepac.commons.dto.CriarAgendamentoRequest;
import com.imepac.commons.dto.CancelarAgendamentoRequest;
import com.imepac.commons.entity.Agendamento;
import com.imepac.commons.entity.Paciente;
import com.imepac.commons.enums.StatusAgendamento;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.PacienteNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private ConsultaServiceImpl service;

    @Test
    void agendarConsulta_success() {
        CriarAgendamentoRequest req = CriarAgendamentoRequest.builder()
                .pacienteId(1L)
                .medicoId(2L)
                .agendadoEm(LocalDateTime.now().plusDays(1).withHour(10))
                .build();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(Paciente.builder().id(1L).build()));
        when(agendamentoRepository.existsByMedicoIdAndAgendadoEmAndStatusNotIn(anyLong(), any(LocalDateTime.class), anyList())).thenReturn(false);
        when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(inv -> {
            Agendamento a = inv.getArgument(0);
            a.setId(11L);
            return a;
        });

        var resp = service.agendarConsulta(req);
        assertThat(resp.getId()).isEqualTo(11L);
    }

    @Test
    void agendarConsulta_pacienteNotFound_throws() {
        when(pacienteRepository.findById(5L)).thenReturn(Optional.empty());
        CriarAgendamentoRequest req = CriarAgendamentoRequest.builder().pacienteId(5L).medicoId(2L).agendadoEm(LocalDateTime.now()).build();

        assertThatThrownBy(() -> service.agendarConsulta(req)).isInstanceOf(PacienteNaoEncontradoException.class);
    }

    @Test
    void agendarConsulta_conflict_throws() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(Paciente.builder().id(1L).build()));
        when(agendamentoRepository.existsByMedicoIdAndAgendadoEmAndStatusNotIn(anyLong(), any(LocalDateTime.class), anyList())).thenReturn(true);

        CriarAgendamentoRequest req = CriarAgendamentoRequest.builder().pacienteId(1L).medicoId(2L).agendadoEm(LocalDateTime.now()).build();

        assertThatThrownBy(() -> service.agendarConsulta(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void encontrarDisponibilidade_returnsSlots() {
        LocalDateTime inicio = LocalDateTime.of(2026,1,1,9,0);
        LocalDateTime fim = inicio.plusHours(3);

        when(agendamentoRepository.existsByMedicoIdAndAgendadoEmAndStatusNotIn(anyLong(), any(LocalDateTime.class), anyList()))
                .thenReturn(false);

        var slots = service.encontrarDisponibilidade(2L, inicio, fim);
        // inicio..fim inclusive per implementation stepping by hour => 4 slots
        assertThat(slots).isNotEmpty();
    }

    @Test
    void cancelarConsulta_alreadyCancelled_throws() {
        Agendamento ag = Agendamento.builder().id(10L).status(StatusAgendamento.CANCELADO).build();
        when(agendamentoRepository.findById(10L)).thenReturn(Optional.of(ag));

        CancelarAgendamentoRequest req = CancelarAgendamentoRequest.builder().motivo("m").senha("s").build();

        assertThatThrownBy(() -> service.cancelarConsulta(10L, req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void registrarProntuario_success() {
        Agendamento ag = Agendamento.builder().id(12L).status(StatusAgendamento.AGENDADO).build();
        when(agendamentoRepository.findById(12L)).thenReturn(Optional.of(ag));
        when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(inv -> inv.getArgument(0));

        var resp = service.registrarProntuario(12L, "pront");
        assertThat(resp.getProntuario()).isEqualTo("pront");
    }
}
