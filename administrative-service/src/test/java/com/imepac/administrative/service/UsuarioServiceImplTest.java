package com.imepac.administrative.service;

import com.imepac.administrative.repository.FuncionarioRepository;
import com.imepac.administrative.repository.UsuarioRepository;
import com.imepac.administrative.service.impl.UsuarioServiceImpl;
import com.imepac.commons.dto.CriarUsuarioRequest;
import com.imepac.commons.entity.Usuario;
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
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private UsuarioServiceImpl service;

    @Test
    void cadastrarUsuario_duplicateIdUser_throws() {
        CriarUsuarioRequest req = CriarUsuarioRequest.builder().idUser("u").funcionarioId(1L).build();
        when(usuarioRepository.existsByIdUser("u")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrarUsuario(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void cadastrarUsuario_missingFuncionario_throws() {
        CriarUsuarioRequest req = CriarUsuarioRequest.builder().idUser("u").funcionarioId(2L).build();
        when(usuarioRepository.existsByIdUser("u")).thenReturn(false);
        when(funcionarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cadastrarUsuario(req)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void cadastrarUsuario_success() {
        CriarUsuarioRequest req = CriarUsuarioRequest.builder().idUser("u").funcionarioId(1L).build();
        when(usuarioRepository.existsByIdUser("u")).thenReturn(false);
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(mock(com.imepac.commons.entity.Funcionario.class)));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(9L);
            return u;
        });

        var resp = service.cadastrarUsuario(req);
        assertThat(resp.getId()).isEqualTo(9L);
    }
}
