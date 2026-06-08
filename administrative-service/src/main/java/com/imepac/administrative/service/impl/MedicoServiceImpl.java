package com.imepac.administrative.service.impl;

import com.imepac.administrative.converter.AdministracaoConversor;
import com.imepac.administrative.repository.EspecialidadeRepository;
import com.imepac.administrative.repository.MedicoRepository;
import com.imepac.administrative.service.MedicoService;
import com.imepac.commons.dto.AtualizarMedicoRequest;
import com.imepac.commons.dto.CriarMedicoRequest;
import com.imepac.commons.dto.RespostaMedico;
import com.imepac.commons.entity.Medico;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadeRepository especialidadeRepository;

    @Override
    public RespostaMedico cadastrarMedico(CriarMedicoRequest request) {
        if (medicoRepository.existsByCrm(request.getCrm())) {
            throw new BusinessException("Já existe médico cadastrado com o CRM informado");
        }
        especialidadeRepository.findById(request.getEspecialidadeId())
                .orElseThrow(() -> new EntityNotFoundException("Especialidade", request.getEspecialidadeId()));

        Medico medico = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(medicoRepository.save(medico));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaMedico> listarMedicos() {
        return medicoRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaMedico buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id)
                .map(AdministracaoConversor::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Medico", id));
    }

    @Override
    public RespostaMedico atualizarMedico(Long id, AtualizarMedicoRequest request) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medico", id));

        if (request.getCrm() != null && !request.getCrm().equals(medico.getCrm()) && medicoRepository.existsByCrmAndIdNot(request.getCrm(), id)) {
            throw new BusinessException("Já existe outro médico cadastrado com este CRM");
        }
        if (request.getEspecialidadeId() != null) {
            especialidadeRepository.findById(request.getEspecialidadeId())
                    .orElseThrow(() -> new EntityNotFoundException("Especialidade", request.getEspecialidadeId()));
        }

        AdministracaoConversor.updateEntity(medico, request);
        return AdministracaoConversor.toResponse(medicoRepository.save(medico));
    }

    @Override
    public void excluirMedico(Long id) {
        medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medico", id));
        medicoRepository.deleteById(id);
    }
}
