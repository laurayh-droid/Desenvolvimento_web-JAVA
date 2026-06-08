package com.imepac.administrative.service;

import com.imepac.commons.dto.AtualizarFuncionarioRequest;
import com.imepac.commons.dto.CriarFuncionarioRequest;
import com.imepac.commons.dto.RespostaFuncionario;

import java.util.List;

public interface FuncionarioService {

    RespostaFuncionario cadastrarFuncionario(CriarFuncionarioRequest request);

    List<RespostaFuncionario> listarFuncionarios();

    RespostaFuncionario buscarFuncionarioPorId(Long id);

    RespostaFuncionario atualizarFuncionario(Long id, AtualizarFuncionarioRequest request);

    void excluirFuncionario(Long id);
}
