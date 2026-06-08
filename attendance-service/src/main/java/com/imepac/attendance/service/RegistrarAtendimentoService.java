package com.imepac.attendance.service;

import com.imepac.commons.dto.CriarAtendimentoRequest;
import com.imepac.commons.dto.RespostaAtendimento;

public interface RegistrarAtendimentoService {

    RespostaAtendimento registrarAtendimento(CriarAtendimentoRequest request);
}
