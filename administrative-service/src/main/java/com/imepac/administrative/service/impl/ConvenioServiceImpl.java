package com.imepac.administrative.service.impl;

import com.imepac.administrative.converter.AdministracaoConversor;
import com.imepac.administrative.repository.ConvenioRepository;
import com.imepac.administrative.service.ConvenioService;
import com.imepac.commons.dto.AtualizarConvenioRequest;
import com.imepac.commons.dto.CriarConvenioRequest;
import com.imepac.commons.dto.RespostaConvenio;
import com.imepac.commons.entity.Convenio;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConvenioServiceImpl implements ConvenioService {

    private final ConvenioRepository convenioRepository;

    @Override
    public RespostaConvenio cadastrarConvenio(CriarConvenioRequest request) {
        if (convenioRepository.existsByCnpj(request.getCnpj())) {
            throw new BusinessException("Já existe convênio cadastrado com este CNPJ");
        }
        Convenio convenio = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(convenioRepository.save(convenio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaConvenio> listarConvenios() {
        return convenioRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaConvenio buscarConvenioPorId(Long id) {
        return convenioRepository.findById(id)
                .map(AdministracaoConversor::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Convenio", id));
    }

    @Override
    public RespostaConvenio atualizarConvenio(Long id, AtualizarConvenioRequest request) {
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Convenio", id));

        if (request.getCnpj() != null && !request.getCnpj().equals(convenio.getCnpj())
                && convenioRepository.existsByCnpjAndIdNot(request.getCnpj(), id)) {
            throw new BusinessException("Já existe outro convênio cadastrado com este CNPJ");
        }

        AdministracaoConversor.updateEntity(convenio, request);
        return AdministracaoConversor.toResponse(convenioRepository.save(convenio));
    }

    @Override
    public void excluirConvenio(Long id) {
        convenioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Convenio", id));
        convenioRepository.deleteById(id);
    }
}
