package com.imepac.administrative.service;

import com.imepac.commons.dto.AtualizarMedicoRequest;
import com.imepac.commons.dto.CriarMedicoRequest;
import com.imepac.commons.dto.RespostaMedico;

import java.util.List;

public interface MedicoService {

    RespostaMedico cadastrarMedico(CriarMedicoRequest request);

    List<RespostaMedico> listarMedicos();

    RespostaMedico buscarMedicoPorId(Long id);

    RespostaMedico atualizarMedico(Long id, AtualizarMedicoRequest request);

    void excluirMedico(Long id);
}
