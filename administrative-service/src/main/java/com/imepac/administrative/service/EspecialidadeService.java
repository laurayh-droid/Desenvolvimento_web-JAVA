package com.imepac.administrative.service;

import com.imepac.commons.dto.AtualizarEspecialidadeRequest;
import com.imepac.commons.dto.CriarEspecialidadeRequest;
import com.imepac.commons.dto.RespostaEspecialidade;

import java.util.List;

public interface EspecialidadeService {

    RespostaEspecialidade cadastrarEspecialidade(CriarEspecialidadeRequest request);

    List<RespostaEspecialidade> listarEspecialidades();

    RespostaEspecialidade buscarEspecialidadePorId(Long id);

    RespostaEspecialidade atualizarEspecialidade(Long id, AtualizarEspecialidadeRequest request);

    void excluirEspecialidade(Long id);
}
