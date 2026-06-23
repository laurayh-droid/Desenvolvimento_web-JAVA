package com.imepac.administrative.service;

import com.imepac.administrative.repository.EspecialidadeRepository;
import com.imepac.administrative.service.impl.EspecialidadeServiceImpl;
import com.imepac.commons.dto.CriarEspecialidadeRequest;
import com.imepac.commons.entity.Especialidade;
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
class EspecialidadeServiceImplTest {

    @Mock
    private EspecialidadeRepository repo;

    @InjectMocks
    private EspecialidadeServiceImpl service;

    @Test
    void cadastrarEspecialidade_duplicate_throws() {
        CriarEspecialidadeRequest req = CriarEspecialidadeRequest.builder().descricao("Cardio").build();
        when(repo.existsByDescricao("Cardio")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrarEspecialidade(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void buscarEspecialidade_notFound_throws() {
        when(repo.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscarEspecialidadePorId(5L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void cadastrarEspecialidade_success() {
        CriarEspecialidadeRequest req = CriarEspecialidadeRequest.builder().descricao("Cardio").build();
        when(repo.existsByDescricao("Cardio")).thenReturn(false);
        when(repo.save(any(Especialidade.class))).thenAnswer(inv -> {
            Especialidade e = inv.getArgument(0);
            e.setId(21L);
            return e;
        });

        var resp = service.cadastrarEspecialidade(req);
        assertThat(resp.getId()).isEqualTo(21L);
    }
}
