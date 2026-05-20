package com.imepac.administrative.service.impl;

import com.imepac.administrative.converter.AdministracaoConversor;
import com.imepac.administrative.repository.*;
import com.imepac.administrative.service.AdministracaoService;
import com.imepac.commons.dto.*;
import com.imepac.commons.entity.*;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdministracaoServiceImpl implements AdministracaoService {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final MedicoRepository medicoRepository;
    private final ConvenioRepository convenioRepository;

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
    public List<RespostaFuncionario> listarFuncionarios() {
        return funcionarioRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
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

    @Override
    public RespostaUsuario cadastrarUsuario(CriarUsuarioRequest request) {
        if (usuarioRepository.existsByIdUser(request.getIdUser())) {
            throw new BusinessException("Já existe usuário cadastrado com este ID de usuário");
        }
        funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new EntityNotFoundException("Funcionario", request.getFuncionarioId()));

        Usuario usuario = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public List<RespostaUsuario> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
    public RespostaUsuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(AdministracaoConversor::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", id));
    }

    @Override
    public RespostaUsuario atualizarUsuario(Long id, AtualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", id));

        if (request.getIdUser() != null && !request.getIdUser().equals(usuario.getIdUser()) && usuarioRepository.existsByIdUserAndIdNot(request.getIdUser(), id)) {
            throw new BusinessException("Já existe outro usuário cadastrado com este ID de usuário");
        }
        if (request.getFuncionarioId() != null) {
            funcionarioRepository.findById(request.getFuncionarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Funcionario", request.getFuncionarioId()));
        }

        AdministracaoConversor.updateEntity(usuario, request);
        return AdministracaoConversor.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void excluirUsuario(Long id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", id));
        usuarioRepository.deleteById(id);
    }

    @Override
    public RespostaEspecialidade cadastrarEspecialidade(CriarEspecialidadeRequest request) {
        if (especialidadeRepository.existsByDescricao(request.getDescricao())) {
            throw new BusinessException("Já existe especialidade com a mesma descrição");
        }
        Especialidade especialidade = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(especialidadeRepository.save(especialidade));
    }

    @Override
    public List<RespostaEspecialidade> listarEspecialidades() {
        return especialidadeRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
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
            throw new BusinessException("Já existe outra especialidade com a mesma descrição");
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
    public List<RespostaMedico> listarMedicos() {
        return medicoRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
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

    @Override
    public RespostaConvenio cadastrarConvenio(CriarConvenioRequest request) {
        if (convenioRepository.existsByCnpj(request.getCnpj())) {
            throw new BusinessException("Já existe convênio cadastrado com este CNPJ");
        }
        Convenio convenio = AdministracaoConversor.toEntity(request);
        return AdministracaoConversor.toResponse(convenioRepository.save(convenio));
    }

    @Override
    public List<RespostaConvenio> listarConvenios() {
        return convenioRepository.findAll().stream()
                .map(AdministracaoConversor::toResponse)
                .toList();
    }

    @Override
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
