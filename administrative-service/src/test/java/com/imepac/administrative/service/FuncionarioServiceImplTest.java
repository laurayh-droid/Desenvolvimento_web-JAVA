package com.imepac.administrative.service;

import com.imepac.administrative.repository.FuncionarioRepository;
import com.imepac.administrative.service.impl.FuncionarioServiceImpl;
import com.imepac.commons.dto.CriarFuncionarioRequest;
import com.imepac.commons.entity.Funcionario;
import com.imepac.commons.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceImplTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioServiceImpl service;

    @Test
    void cadastrarFuncionario_duplicateCpf_throws() {
        CriarFuncionarioRequest req = CriarFuncionarioRequest.builder().cpf("111").rg("r").build();
        when(funcionarioRepository.existsByCpf("111")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrarFuncionario(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void cadastrarFuncionario_success() {
        CriarFuncionarioRequest req = CriarFuncionarioRequest.builder().cpf("111").rg("r").build();
        when(funcionarioRepository.existsByCpf("111")).thenReturn(false);
        when(funcionarioRepository.existsByRg("r")).thenReturn(false);
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(inv -> {
            Funcionario f = inv.getArgument(0);
            f.setId(13L);
            return f;
        });

        var resp = service.cadastrarFuncionario(req);
        assertThat(resp.getId()).isEqualTo(13L);
    }
}
