package com.imepac.administrative.controller;

import com.imepac.administrative.service.FuncionarioService;
import com.imepac.commons.dto.AtualizarFuncionarioRequest;
import com.imepac.commons.dto.CriarFuncionarioRequest;
import com.imepac.commons.dto.RespostaFuncionario;
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
@RequestMapping("/api/v1/admin/funcionarios")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Gerenciamento de funcionários administrativos")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar funcionário")
    public ApiResponse<RespostaFuncionario> cadastrarFuncionario(@Valid @RequestBody CriarFuncionarioRequest request) {
        return ApiResponse.success(funcionarioService.cadastrarFuncionario(request), "Funcionário cadastrado com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar funcionários")
    public ApiResponse<List<RespostaFuncionario>> listarFuncionarios() {
        return ApiResponse.success(funcionarioService.listarFuncionarios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionário por id")
    public ApiResponse<RespostaFuncionario> buscarFuncionarioPorId(
            @Parameter(description = "ID do funcionário") @PathVariable Long id) {
        return ApiResponse.success(funcionarioService.buscarFuncionarioPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar funcionário")
    public ApiResponse<RespostaFuncionario> atualizarFuncionario(
            @Parameter(description = "ID do funcionário") @PathVariable Long id,
            @Valid @RequestBody AtualizarFuncionarioRequest request) {
        return ApiResponse.success(funcionarioService.atualizarFuncionario(id, request), "Funcionário atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir funcionário")
    public ApiResponse<Void> excluirFuncionario(
            @Parameter(description = "ID do funcionário") @PathVariable Long id) {
        funcionarioService.excluirFuncionario(id);
        return ApiResponse.success("Funcionário excluído com sucesso");
    }
}
