package com.imepac.attendance.service;

import com.imepac.attendance.repository.AtendimentoRepository;
import com.imepac.attendance.service.impl.ListarAtendimentosPorPacienteServiceImpl;
import com.imepac.commons.entity.Atendimento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarAtendimentosPorPacienteServiceImplTest {

    @Mock
    private AtendimentoRepository repo;

    @InjectMocks
    private ListarAtendimentosPorPacienteServiceImpl service;

    @Test
    void listarAtendimentosPorPaciente_returnsList() {
        Atendimento a = new Atendimento();
        a.setId(3L);
        when(repo.findAllByPacienteIdOrderByRegistradoEmAsc(4L)).thenReturn(List.of(a));

        var resp = service.listarAtendimentosPorPaciente(4L);
        assertThat(resp).hasSize(1);
        assertThat(resp.get(0).getId()).isEqualTo(3L);
    }
}
