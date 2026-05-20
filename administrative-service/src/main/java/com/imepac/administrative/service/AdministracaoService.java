package com.imepac.administrative.service;

import com.imepac.commons.dto.*;

import java.util.List;

public interface AdministracaoService {

    RespostaFuncionario cadastrarFuncionario(CriarFuncionarioRequest request);
    List<RespostaFuncionario> listarFuncionarios();
    RespostaFuncionario buscarFuncionarioPorId(Long id);
    RespostaFuncionario atualizarFuncionario(Long id, AtualizarFuncionarioRequest request);
    void excluirFuncionario(Long id);

    RespostaUsuario cadastrarUsuario(CriarUsuarioRequest request);
    List<RespostaUsuario> listarUsuarios();
    RespostaUsuario buscarUsuarioPorId(Long id);
    RespostaUsuario atualizarUsuario(Long id, AtualizarUsuarioRequest request);
    void excluirUsuario(Long id);

    RespostaEspecialidade cadastrarEspecialidade(CriarEspecialidadeRequest request);
    List<RespostaEspecialidade> listarEspecialidades();
    RespostaEspecialidade buscarEspecialidadePorId(Long id);
    RespostaEspecialidade atualizarEspecialidade(Long id, AtualizarEspecialidadeRequest request);
    void excluirEspecialidade(Long id);

    RespostaMedico cadastrarMedico(CriarMedicoRequest request);
    List<RespostaMedico> listarMedicos();
    RespostaMedico buscarMedicoPorId(Long id);
    RespostaMedico atualizarMedico(Long id, AtualizarMedicoRequest request);
    void excluirMedico(Long id);

    RespostaConvenio cadastrarConvenio(CriarConvenioRequest request);
    List<RespostaConvenio> listarConvenios();
    RespostaConvenio buscarConvenioPorId(Long id);
    RespostaConvenio atualizarConvenio(Long id, AtualizarConvenioRequest request);
    void excluirConvenio(Long id);
}
