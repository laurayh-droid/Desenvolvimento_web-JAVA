package com.imepac.administrative.service.impl;

import com.imepac.administrative.converter.AdministracaoConversor;
import com.imepac.administrative.repository.FuncionarioRepository;
import com.imepac.administrative.service.FuncionarioService;
import com.imepac.commons.dto.AtualizarFuncionarioRequest;
import com.imepac.commons.dto.CriarFuncionarioRequest;
import com.imepac.commons.dto.RespostaFuncionario;
import com.imepac.commons.entity.Funcionario;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Override
    public RespostaFuncionario cadastrarFuncionario(CriarFuncionarioRequest request) {
        if (funcionarioRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe funcionário cadastrado com o CPF informado");
        }
        if (funcionarioRepository.existsByRg(request.getRg())) {
            throw new BusinessException("Já existe funcionário cadastrado com o RG informado");
        }
        Funcionario funcionario = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(funcionarioRepository.save(funcionario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaFuncionario> listarFuncionarios() {
        return funcionarioRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaFuncionario buscarFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id)
                .map(AdministracaoConversor::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Funcionario", id));
    }

    @Override
    public RespostaFuncionario atualizarFuncionario(Long id, AtualizarFuncionarioRequest request) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionario", id));

        if (request.getCpf() != null && !request.getCpf().equals(funcionario.getCpf()) && funcionarioRepository.existsByCpfAndIdNot(request.getCpf(), id)) {
            throw new BusinessException("Já existe outro funcionário cadastrado com o CPF informado");
        }
        if (request.getRg() != null && !request.getRg().equals(funcionario.getRg()) && funcionarioRepository.existsByRgAndIdNot(request.getRg(), id)) {
            throw new BusinessException("Já existe outro funcionário cadastrado com o RG informado");
        }

        AdministracaoConversor.updateEntity(funcionario, request);
        return AdministracaoConversor.toResponse(funcionarioRepository.save(funcionario));
    }

    @Override
    public void excluirFuncionario(Long id) {
        funcionarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionario", id));
        funcionarioRepository.deleteById(id);
    }
}
