package com.imepac.administrative.service;

import com.imepac.administrative.repository.EspecialidadeRepository;
import com.imepac.administrative.repository.MedicoRepository;
import com.imepac.administrative.service.impl.MedicoServiceImpl;
import com.imepac.commons.dto.CriarMedicoRequest;
import com.imepac.commons.entity.Medico;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.EntityNotFoundException;
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
class MedicoServiceImplTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @InjectMocks
    private MedicoServiceImpl medicoService;

    @Test
    void cadastrarMedico_duplicateCrm_throws() {
        CriarMedicoRequest req = CriarMedicoRequest.builder().nome("M").crm("123").especialidadeId(1L).build();
        when(medicoRepository.existsByCrm(req.getCrm())).thenReturn(true);

        assertThatThrownBy(() -> medicoService.cadastrarMedico(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void cadastrarMedico_missingEspecialidade_throws() {
        CriarMedicoRequest req = CriarMedicoRequest.builder().nome("M").crm("123").especialidadeId(2L).build();
        when(medicoRepository.existsByCrm(req.getCrm())).thenReturn(false);
        when(especialidadeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicoService.cadastrarMedico(req)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void cadastrarMedico_success_saves() {
        CriarMedicoRequest req = CriarMedicoRequest.builder().nome("M").crm("123").especialidadeId(1L).build();
        when(medicoRepository.existsByCrm(req.getCrm())).thenReturn(false);
        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(mock(com.imepac.administrative.entity.Especialidade.class)));

        when(medicoRepository.save(any(Medico.class))).thenAnswer(inv -> {
            Medico m = inv.getArgument(0);
            m.setId(5L);
            return m;
        });

        var resp = medicoService.cadastrarMedico(req);
        assertThat(resp).isNotNull();
        // id set by save stub
        assertThat(resp.getId()).isEqualTo(5L);
    }
}
