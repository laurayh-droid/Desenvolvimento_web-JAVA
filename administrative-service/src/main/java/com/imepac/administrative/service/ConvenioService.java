package com.imepac.administrative.service;

import com.imepac.commons.dto.AtualizarConvenioRequest;
import com.imepac.commons.dto.CriarConvenioRequest;
import com.imepac.commons.dto.RespostaConvenio;

import java.util.List;

public interface ConvenioService {

    RespostaConvenio cadastrarConvenio(CriarConvenioRequest request);

    List<RespostaConvenio> listarConvenios();

    RespostaConvenio buscarConvenioPorId(Long id);

    RespostaConvenio atualizarConvenio(Long id, AtualizarConvenioRequest request);

    void excluirConvenio(Long id);
}
