package com.imepac.administrative.controller;

import com.imepac.administrative.service.EspecialidadeService;
import com.imepac.commons.dto.AtualizarEspecialidadeRequest;
import com.imepac.commons.dto.CriarEspecialidadeRequest;
import com.imepac.commons.dto.RespostaEspecialidade;
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
@RequestMapping("/api/v1/admin/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "Gerenciamento de especialidades médicas")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar especialidade")
    public ApiResponse<RespostaEspecialidade> cadastrarEspecialidade(@Valid @RequestBody CriarEspecialidadeRequest request) {
        return ApiResponse.success(especialidadeService.cadastrarEspecialidade(request), "Especialidade cadastrada com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar especialidades")
    public ApiResponse<List<RespostaEspecialidade>> listarEspecialidades() {
        return ApiResponse.success(especialidadeService.listarEspecialidades());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar especialidade por id")
    public ApiResponse<RespostaEspecialidade> buscarEspecialidadePorId(
            @Parameter(description = "ID da especialidade") @PathVariable Long id) {
        return ApiResponse.success(especialidadeService.buscarEspecialidadePorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar especialidade")
    public ApiResponse<RespostaEspecialidade> atualizarEspecialidade(
            @Parameter(description = "ID da especialidade") @PathVariable Long id,
            @Valid @RequestBody AtualizarEspecialidadeRequest request) {
        return ApiResponse.success(especialidadeService.atualizarEspecialidade(id, request), "Especialidade atualizada com sucesso");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir especialidade")
    public ApiResponse<Void> excluirEspecialidade(
            @Parameter(description = "ID da especialidade") @PathVariable Long id) {
        especialidadeService.excluirEspecialidade(id);
        return ApiResponse.success("Especialidade excluída com sucesso");
    }
}
