package com.imepac.administrative.service;

import com.imepac.administrative.repository.ConvenioRepository;
import com.imepac.administrative.service.impl.ConvenioServiceImpl;
import com.imepac.commons.dto.CriarConvenioRequest;
import com.imepac.commons.entity.Convenio;
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
class ConvenioServiceImplTest {

    @Mock
    private ConvenioRepository repo;

    @InjectMocks
    private ConvenioServiceImpl service;

    @Test
    void cadastrarConvenio_duplicate_throws() {
        CriarConvenioRequest req = CriarConvenioRequest.builder().cnpj("123").build();
        when(repo.existsByCnpj("123")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrarConvenio(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void buscarConvenio_notFound_throws() {
        when(repo.findById(7L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscarConvenioPorId(7L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void cadastrarConvenio_success() {
        CriarConvenioRequest req = CriarConvenioRequest.builder().cnpj("123").build();
        when(repo.existsByCnpj("123")).thenReturn(false);
        when(repo.save(any(Convenio.class))).thenAnswer(inv -> {
            Convenio c = inv.getArgument(0);
            c.setId(31L);
            return c;
        });

        var resp = service.cadastrarConvenio(req);
        assertThat(resp.getId()).isEqualTo(31L);
    }
}
