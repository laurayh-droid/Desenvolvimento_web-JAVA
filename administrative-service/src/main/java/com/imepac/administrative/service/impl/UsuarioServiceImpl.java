package com.imepac.administrative.service.impl;

import com.imepac.administrative.converter.AdministracaoConversor;
import com.imepac.administrative.repository.FuncionarioRepository;
import com.imepac.administrative.repository.PerfilRepository;
import com.imepac.administrative.repository.UsuarioRepository;

import com.imepac.administrative.service.UsuarioService;
import com.imepac.commons.dto.AtualizarUsuarioRequest;
import com.imepac.commons.dto.CriarUsuarioRequest;
import com.imepac.commons.dto.RespostaUsuario;
import com.imepac.commons.entity.Usuario;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PerfilRepository perfilRepository;


    @Override
    public RespostaUsuario cadastrarUsuario(CriarUsuarioRequest request) {
        if (usuarioRepository.existsByIdUser(request.getIdUser())) {
            throw new BusinessException("Já existe usuário cadastrado com este ID de usuário");
        }
        funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new EntityNotFoundException("Funcionario", request.getFuncionarioId()));

        perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil", request.getPerfilId()));

        Usuario usuario = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaUsuario> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaUsuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(AdministracaoConversor::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", id));
    }

    @Override
    public RespostaUsuario atualizarUsuario(Long id, AtualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", id));

        if (request.getIdUser() != null && !request.getIdUser().equals(usuario.getIdUser()) && usuarioRepository.existsByIdUserAndIdNot(request.getIdUser(), id)) {
            throw new BusinessException("Já existe outro usuário cadastrado com este ID de usuário");
        }
        if (request.getFuncionarioId() != null) {
            funcionarioRepository.findById(request.getFuncionarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Funcionario", request.getFuncionarioId()));
        }

        AdministracaoConversor.updateEntity(usuario, request);
        return AdministracaoConversor.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void excluirUsuario(Long id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", id));
        usuarioRepository.deleteById(id);
    }
}
