package com.imepac.administrative.converter;

import com.imepac.commons.dto.*;
import com.imepac.commons.entity.*;

public final class AdministracaoConversor {

    private AdministracaoConversor() {
    }

    public static Funcionario toEntity(CriarFuncionarioRequest request) {
        return Funcionario.builder()
                .nomeCompleto(request.getNomeCompleto())
                .rg(request.getRg())
                .cpf(request.getCpf())
                .dataNascimento(request.getDataNascimento())
                .telefoneFixo(request.getTelefoneFixo())
                .telefoneCelular(request.getTelefoneCelular())
                .rua(request.getRua())
                .numero(request.getNumero())
                .complemento(request.getComplemento())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .numeroCtps(request.getNumeroCtps())
                .numeroPis(request.getNumeroPis())
                .build();
    }

    public static RespostaFuncionario toResponse(Funcionario funcionario) {
        return RespostaFuncionario.builder()
                .id(funcionario.getId())
                .nomeCompleto(funcionario.getNomeCompleto())
                .rg(funcionario.getRg())
                .cpf(funcionario.getCpf())
                .dataNascimento(funcionario.getDataNascimento())
                .telefoneFixo(funcionario.getTelefoneFixo())
                .telefoneCelular(funcionario.getTelefoneCelular())
                .rua(funcionario.getRua())
                .numero(funcionario.getNumero())
                .complemento(funcionario.getComplemento())
                .bairro(funcionario.getBairro())
                .cidade(funcionario.getCidade())
                .estado(funcionario.getEstado())
                .cep(funcionario.getCep())
                .numeroCtps(funcionario.getNumeroCtps())
                .numeroPis(funcionario.getNumeroPis())
                .criadoEm(funcionario.getCriadoEm())
                .atualizadoEm(funcionario.getAtualizadoEm())
                .build();
    }

    public static void updateEntity(Funcionario funcionario, AtualizarFuncionarioRequest request) {
        if (request.getNomeCompleto() != null) funcionario.setNomeCompleto(request.getNomeCompleto());
        if (request.getRg() != null) funcionario.setRg(request.getRg());
        if (request.getCpf() != null) funcionario.setCpf(request.getCpf());
        if (request.getDataNascimento() != null) funcionario.setDataNascimento(request.getDataNascimento());
        if (request.getTelefoneFixo() != null) funcionario.setTelefoneFixo(request.getTelefoneFixo());
        if (request.getTelefoneCelular() != null) funcionario.setTelefoneCelular(request.getTelefoneCelular());
        if (request.getRua() != null) funcionario.setRua(request.getRua());
        if (request.getNumero() != null) funcionario.setNumero(request.getNumero());
        if (request.getComplemento() != null) funcionario.setComplemento(request.getComplemento());
        if (request.getBairro() != null) funcionario.setBairro(request.getBairro());
        if (request.getCidade() != null) funcionario.setCidade(request.getCidade());
        if (request.getEstado() != null) funcionario.setEstado(request.getEstado());
        if (request.getCep() != null) funcionario.setCep(request.getCep());
        if (request.getNumeroCtps() != null) funcionario.setNumeroCtps(request.getNumeroCtps());
        if (request.getNumeroPis() != null) funcionario.setNumeroPis(request.getNumeroPis());
    }

    public static Usuario toEntity(CriarUsuarioRequest request) {
        return Usuario.builder()
                .idUser(request.getIdUser())
                .senha(request.getSenha())
                .funcionarioId(request.getFuncionarioId())
                .perfil(Perfil.builder().id(request.getPerfilId()).build())
                .permissoes(request.getPermissoes())
                .build();
    }

    public static RespostaUsuario toResponse(Usuario usuario) {
        return RespostaUsuario.builder()
                .id(usuario.getId())
                .idUser(usuario.getIdUser())
                .funcionarioId(usuario.getFuncionarioId())
                .perfilId(usuario.getPerfil() != null ? usuario.getPerfil().getId() : null)
                .perfilNome(usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null)
                .permissoes(usuario.getPermissoes())
                .criadoEm(usuario.getCriadoEm())
                .atualizadoEm(usuario.getAtualizadoEm())
                .build();
    }

    public static void updateEntity(Usuario usuario, AtualizarUsuarioRequest request) {
        if (request.getIdUser() != null) usuario.setIdUser(request.getIdUser());
        if (request.getSenha() != null) usuario.setSenha(request.getSenha());
        if (request.getFuncionarioId() != null) usuario.setFuncionarioId(request.getFuncionarioId());
        if (request.getPerfilId() != null) usuario.setPerfil(Perfil.builder().id(request.getPerfilId()).build());
        if (request.getPermissoes() != null) usuario.setPermissoes(request.getPermissoes());
    }

    public static Especialidade toEntity(CriarEspecialidadeRequest request) {
        return Especialidade.builder()
                .descricao(request.getDescricao())
                .build();
    }

    public static RespostaEspecialidade toResponse(Especialidade especialidade) {
        return RespostaEspecialidade.builder()
                .id(especialidade.getId())
                .descricao(especialidade.getDescricao())
                .criadoEm(especialidade.getCriadoEm())
                .atualizadoEm(especialidade.getAtualizadoEm())
                .build();
    }

    public static void updateEntity(Especialidade especialidade, AtualizarEspecialidadeRequest request) {
        if (request.getDescricao() != null) especialidade.setDescricao(request.getDescricao());
    }

    public static Medico toEntity(CriarMedicoRequest request) {
        return Medico.builder()
                .nome(request.getNome())
                .crm(request.getCrm())
                .especialidadeId(request.getEspecialidadeId())
                .build();
    }

    public static RespostaMedico toResponse(Medico medico) {
        return RespostaMedico.builder()
                .id(medico.getId())
                .nome(medico.getNome())
                .crm(medico.getCrm())
                .especialidadeId(medico.getEspecialidadeId())
                .criadoEm(medico.getCriadoEm())
                .atualizadoEm(medico.getAtualizadoEm())
                .build();
    }

    public static void updateEntity(Medico medico, AtualizarMedicoRequest request) {
        if (request.getNome() != null) medico.setNome(request.getNome());
        if (request.getCrm() != null) medico.setCrm(request.getCrm());
        if (request.getEspecialidadeId() != null) medico.setEspecialidadeId(request.getEspecialidadeId());
    }

    public static Convenio toEntity(CriarConvenioRequest request) {
        return Convenio.builder()
                .nomeEmpresa(request.getNomeEmpresa())
                .cnpj(request.getCnpj())
                .telefone(request.getTelefone())
                .build();
    }

    public static RespostaConvenio toResponse(Convenio convenio) {
        return RespostaConvenio.builder()
                .id(convenio.getId())
                .nomeEmpresa(convenio.getNomeEmpresa())
                .cnpj(convenio.getCnpj())
                .telefone(convenio.getTelefone())
                .criadoEm(convenio.getCriadoEm())
                .atualizadoEm(convenio.getAtualizadoEm())
                .build();
    }

    public static void updateEntity(Convenio convenio, AtualizarConvenioRequest request) {
        if (request.getNomeEmpresa() != null) convenio.setNomeEmpresa(request.getNomeEmpresa());
        if (request.getCnpj() != null) convenio.setCnpj(request.getCnpj());
        if (request.getTelefone() != null) convenio.setTelefone(request.getTelefone());
    }
}
