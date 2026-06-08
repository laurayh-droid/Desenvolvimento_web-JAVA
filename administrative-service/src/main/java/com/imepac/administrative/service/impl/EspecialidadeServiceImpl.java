package com.imepac.administrative.service.impl;

import com.imepac.administrative.converter.AdministracaoConversor;
import com.imepac.administrative.repository.EspecialidadeRepository;
import com.imepac.administrative.service.EspecialidadeService;
import com.imepac.commons.dto.AtualizarEspecialidadeRequest;
import com.imepac.commons.dto.CriarEspecialidadeRequest;
import com.imepac.commons.dto.RespostaEspecialidade;
import com.imepac.commons.entity.Especialidade;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadeServiceImpl implements EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    @Override
    public RespostaEspecialidade cadastrarEspecialidade(CriarEspecialidadeRequest request) {
        if (especialidadeRepository.existsByDescricao(request.getDescricao())) {
            throw new BusinessException("Já existe especialidade com a mesma descrição");
        }
        Especialidade especialidade = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(especialidadeRepository.save(especialidade));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaEspecialidade> listarEspecialidades() {
        return especialidadeRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaEspecialidade buscarEspecialidadePorId(Long id) {
        return especialidadeRepository.findById(id)
                .map(AdministracaoConversor::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade", id));
    }

    @Override
    public RespostaEspecialidade atualizarEspecialidade(Long id, AtualizarEspecialidadeRequest request) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade", id));

        if (request.getDescricao() != null && !request.getDescricao().equalsIgnoreCase(especialidade.getDescricao())
                && especialidadeRepository.existsByDescricaoAndIdNot(request.getDescricao(), id)) {
            throw new BusinessException("Já existe outra especialidade cadastrado com a mesma descrição");
        }

        AdministracaoConversor.updateEntity(especialidade, request);
        return AdministracaoConversor.toResponse(especialidadeRepository.save(especialidade));
    }

    @Override
    public void excluirEspecialidade(Long id) {
        especialidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade", id));
        especialidadeRepository.deleteById(id);
    }
}
