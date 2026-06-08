package com.imepac.appointment.service.impl;

import com.imepac.appointment.converter.AgendamentoConversor;
import com.imepac.appointment.repository.PacienteRepository;
import com.imepac.appointment.service.PacienteService;
import com.imepac.commons.dto.AtualizarPacienteRequest;
import com.imepac.commons.dto.CriarPacienteRequest;
import com.imepac.commons.dto.RespostaPaciente;
import com.imepac.commons.entity.Paciente;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.PacienteNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Override
    @Transactional
    public RespostaPaciente cadastrarPaciente(CriarPacienteRequest request) {
        log.info("Criando paciente com cpf: {}", request.getCpf());

        if (pacienteRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Paciente já cadastrado com o CPF: " + request.getCpf());
        }
        if (pacienteRepository.existsByRg(request.getRg())) {
            throw new BusinessException("Paciente já cadastrado com o RG: " + request.getRg());
        }

        validateSeguro(request);

        Paciente paciente = AgendamentoConversor.toEntity(request);
        Paciente salvo = pacienteRepository.save(paciente);
        return AgendamentoConversor.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespostaPaciente> listarPacientes() {
        return pacienteRepository.findAll().stream().map(AgendamentoConversor::toResponse).toList();
    }

    private void validateSeguro(CriarPacienteRequest request) {
        if (Boolean.TRUE.equals(request.getPossuiSeguro()) && (request.getNomeEmpresaSeguro() == null || request.getNomeEmpresaSeguro().isBlank())) {
            throw new BusinessException("Nome da empresa do seguro é obrigatório quando o paciente possui convênio/plano de saúde");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaPaciente buscarPacientePorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoEncontradoException(id));
        return AgendamentoConversor.toResponse(paciente);
    }

    @Override
    @Transactional
    public RespostaPaciente atualizarPaciente(Long id, AtualizarPacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoEncontradoException(id));

        if (request.getNomeCompleto() != null) paciente.setNomeCompleto(request.getNomeCompleto());
        if (request.getTelefoneFixo() != null) paciente.setTelefoneFixo(request.getTelefoneFixo());
        if (request.getTelefoneCelular() != null) paciente.setTelefoneCelular(request.getTelefoneCelular());

        if (request.getRua() != null) paciente.setRua(request.getRua());
        if (request.getNumero() != null) paciente.setNumero(request.getNumero());
        if (request.getComplemento() != null) paciente.setComplemento(request.getComplemento());
        if (request.getBairro() != null) paciente.setBairro(request.getBairro());
        if (request.getCidade() != null) paciente.setCidade(request.getCidade());
        if (request.getEstado() != null) paciente.setEstado(request.getEstado());
        if (request.getCep() != null) paciente.setCep(request.getCep());

        if (request.getPossuiSeguro() != null) paciente.setPossuiSeguro(request.getPossuiSeguro());
        if (request.getNomeEmpresaSeguro() != null) paciente.setNomeEmpresaSeguro(request.getNomeEmpresaSeguro());
        if (request.getGenero() != null) paciente.setGenero(request.getGenero());
        if (request.getDataNascimento() != null) paciente.setDataNascimento(request.getDataNascimento());

        Paciente salvo = pacienteRepository.save(paciente);
        return AgendamentoConversor.toResponse(salvo);
    }
}
