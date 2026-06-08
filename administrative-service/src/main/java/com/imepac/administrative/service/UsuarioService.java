package com.imepac.administrative.service;

import com.imepac.commons.dto.AtualizarUsuarioRequest;
import com.imepac.commons.dto.CriarUsuarioRequest;
import com.imepac.commons.dto.RespostaUsuario;

import java.util.List;

public interface UsuarioService {

    RespostaUsuario cadastrarUsuario(CriarUsuarioRequest request);

    List<RespostaUsuario> listarUsuarios();

    RespostaUsuario buscarUsuarioPorId(Long id);

    RespostaUsuario atualizarUsuario(Long id, AtualizarUsuarioRequest request);

    void excluirUsuario(Long id);
}
