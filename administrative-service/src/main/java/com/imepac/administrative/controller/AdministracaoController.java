package com.imepac.administrative.controller;

import com.imepac.administrative.service.AdministracaoService;
import com.imepac.commons.dto.*;
import com.imepac.commons.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Administração", description = "Gerenciamento de cadastros administrativos da clínica")
public class AdministracaoController {

    private final AdministracaoService administracaoService;

    @PostMapping("/funcionarios")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar funcionário")
    public ApiResponse<RespostaFuncionario> cadastrarFuncionario(@Valid @RequestBody CriarFuncionarioRequest request) {
        return ApiResponse.success(administracaoService.cadastrarFuncionario(request), "Funcionário cadastrado com sucesso");
    }

    @GetMapping("/funcionarios")
    @Operation(summary = "Listar funcionários")
    public ApiResponse<List<RespostaFuncionario>> listarFuncionarios() {
        return ApiResponse.success(administracaoService.listarFuncionarios());
    }

    @GetMapping("/funcionarios/{id}")
    @Operation(summary = "Buscar funcionário por id")
    public ApiResponse<RespostaFuncionario> buscarFuncionarioPorId(
            @Parameter(description = "ID do funcionário") @PathVariable Long id) {
        return ApiResponse.success(administracaoService.buscarFuncionarioPorId(id));
    }

    @PutMapping("/funcionarios/{id}")
    @Operation(summary = "Atualizar funcionário")
    public ApiResponse<RespostaFuncionario> atualizarFuncionario(
            @Parameter(description = "ID do funcionário") @PathVariable Long id,
            @Valid @RequestBody AtualizarFuncionarioRequest request) {
        return ApiResponse.success(administracaoService.atualizarFuncionario(id, request), "Funcionário atualizado com sucesso");
    }

    @DeleteMapping("/funcionarios/{id}")
    @Operation(summary = "Excluir funcionário")
    public ApiResponse<Void> excluirFuncionario(
            @Parameter(description = "ID do funcionário") @PathVariable Long id) {
        administracaoService.excluirFuncionario(id);
        return ApiResponse.success("Funcionário excluído com sucesso");
    }

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar usuário")
    public ApiResponse<RespostaUsuario> cadastrarUsuario(@Valid @RequestBody CriarUsuarioRequest request) {
        return ApiResponse.success(administracaoService.cadastrarUsuario(request), "Usuário cadastrado com sucesso");
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuários")
    public ApiResponse<List<RespostaUsuario>> listarUsuarios() {
        return ApiResponse.success(administracaoService.listarUsuarios());
    }

    @GetMapping("/usuarios/{id}")
    @Operation(summary = "Buscar usuário por id")
    public ApiResponse<RespostaUsuario> buscarUsuarioPorId(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        return ApiResponse.success(administracaoService.buscarUsuarioPorId(id));
    }

    @PutMapping("/usuarios/{id}")
    @Operation(summary = "Atualizar usuário")
    public ApiResponse<RespostaUsuario> atualizarUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioRequest request) {
        return ApiResponse.success(administracaoService.atualizarUsuario(id, request), "Usuário atualizado com sucesso");
    }

    @DeleteMapping("/usuarios/{id}")
    @Operation(summary = "Excluir usuário")
    public ApiResponse<Void> excluirUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        administracaoService.excluirUsuario(id);
        return ApiResponse.success("Usuário excluído com sucesso");
    }

    @PostMapping("/especialidades")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar especialidade")
    public ApiResponse<RespostaEspecialidade> cadastrarEspecialidade(@Valid @RequestBody CriarEspecialidadeRequest request) {
        return ApiResponse.success(administracaoService.cadastrarEspecialidade(request), "Especialidade cadastrada com sucesso");
    }

    @GetMapping("/especialidades")
    @Operation(summary = "Listar especialidades")
    public ApiResponse<List<RespostaEspecialidade>> listarEspecialidades() {
        return ApiResponse.success(administracaoService.listarEspecialidades());
    }

    @GetMapping("/especialidades/{id}")
    @Operation(summary = "Buscar especialidade por id")
    public ApiResponse<RespostaEspecialidade> buscarEspecialidadePorId(
            @Parameter(description = "ID da especialidade") @PathVariable Long id) {
        return ApiResponse.success(administracaoService.buscarEspecialidadePorId(id));
    }

    @PutMapping("/especialidades/{id}")
    @Operation(summary = "Atualizar especialidade")
    public ApiResponse<RespostaEspecialidade> atualizarEspecialidade(
            @Parameter(description = "ID da especialidade") @PathVariable Long id,
            @Valid @RequestBody AtualizarEspecialidadeRequest request) {
        return ApiResponse.success(administracaoService.atualizarEspecialidade(id, request), "Especialidade atualizada com sucesso");
    }

    @DeleteMapping("/especialidades/{id}")
    @Operation(summary = "Excluir especialidade")
    public ApiResponse<Void> excluirEspecialidade(
            @Parameter(description = "ID da especialidade") @PathVariable Long id) {
        administracaoService.excluirEspecialidade(id);
        return ApiResponse.success("Especialidade excluída com sucesso");
    }

    @PostMapping("/medicos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar médico")
    public ApiResponse<RespostaMedico> cadastrarMedico(@Valid @RequestBody CriarMedicoRequest request) {
        return ApiResponse.success(administracaoService.cadastrarMedico(request), "Médico cadastrado com sucesso");
    }

    @GetMapping("/medicos")
    @Operation(summary = "Listar médicos")
    public ApiResponse<List<RespostaMedico>> listarMedicos() {
        return ApiResponse.success(administracaoService.listarMedicos());
    }

    @GetMapping("/medicos/{id}")
    @Operation(summary = "Buscar médico por id")
    public ApiResponse<RespostaMedico> buscarMedicoPorId(
            @Parameter(description = "ID do médico") @PathVariable Long id) {
        return ApiResponse.success(administracaoService.buscarMedicoPorId(id));
    }

    @PutMapping("/medicos/{id}")
    @Operation(summary = "Atualizar médico")
    public ApiResponse<RespostaMedico> atualizarMedico(
            @Parameter(description = "ID do médico") @PathVariable Long id,
            @Valid @RequestBody AtualizarMedicoRequest request) {
        return ApiResponse.success(administracaoService.atualizarMedico(id, request), "Médico atualizado com sucesso");
    }

    @DeleteMapping("/medicos/{id}")
    @Operation(summary = "Excluir médico")
    public ApiResponse<Void> excluirMedico(
            @Parameter(description = "ID do médico") @PathVariable Long id) {
        administracaoService.excluirMedico(id);
        return ApiResponse.success("Médico excluído com sucesso");
    }

    @PostMapping("/convenios")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar convênio")
    public ApiResponse<RespostaConvenio> cadastrarConvenio(@Valid @RequestBody CriarConvenioRequest request) {
        return ApiResponse.success(administracaoService.cadastrarConvenio(request), "Convênio cadastrado com sucesso");
    }

    @GetMapping("/convenios")
    @Operation(summary = "Listar convênios")
    public ApiResponse<List<RespostaConvenio>> listarConvenios() {
        return ApiResponse.success(administracaoService.listarConvenios());
    }

    @GetMapping("/convenios/{id}")
    @Operation(summary = "Buscar convênio por id")
    public ApiResponse<RespostaConvenio> buscarConvenioPorId(
            @Parameter(description = "ID do convênio") @PathVariable Long id) {
        return ApiResponse.success(administracaoService.buscarConvenioPorId(id));
    }

    @PutMapping("/convenios/{id}")
    @Operation(summary = "Atualizar convênio")
    public ApiResponse<RespostaConvenio> atualizarConvenio(
            @Parameter(description = "ID do convênio") @PathVariable Long id,
            @Valid @RequestBody AtualizarConvenioRequest request) {
        return ApiResponse.success(administracaoService.atualizarConvenio(id, request), "Convênio atualizado com sucesso");
    }

    @DeleteMapping("/convenios/{id}")
    @Operation(summary = "Excluir convênio")
    public ApiResponse<Void> excluirConvenio(
            @Parameter(description = "ID do convênio") @PathVariable Long id) {
        administracaoService.excluirConvenio(id);
        return ApiResponse.success("Convênio excluído com sucesso");
    }
}
