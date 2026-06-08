package com.imepac.administrative.controller;

import com.imepac.administrative.service.MedicoService;
import com.imepac.commons.dto.AtualizarMedicoRequest;
import com.imepac.commons.dto.CriarMedicoRequest;
import com.imepac.commons.dto.RespostaMedico;
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
@RequestMapping("/api/v1/admin/medicos")
@RequiredArgsConstructor
@Tag(name = "Médicos", description = "Gerenciamento de médicos da clínica")
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar médico")
    public ApiResponse<RespostaMedico> cadastrarMedico(@Valid @RequestBody CriarMedicoRequest request) {
        return ApiResponse.success(medicoService.cadastrarMedico(request), "Médico cadastrado com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar médicos")
    public ApiResponse<List<RespostaMedico>> listarMedicos() {
        return ApiResponse.success(medicoService.listarMedicos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar médico por id")
    public ApiResponse<RespostaMedico> buscarMedicoPorId(
            @Parameter(description = "ID do médico") @PathVariable Long id) {
        return ApiResponse.success(medicoService.buscarMedicoPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar médico")
    public ApiResponse<RespostaMedico> atualizarMedico(
            @Parameter(description = "ID do médico") @PathVariable Long id,
            @Valid @RequestBody AtualizarMedicoRequest request) {
        return ApiResponse.success(medicoService.atualizarMedico(id, request), "Médico atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir médico")
    public ApiResponse<Void> excluirMedico(
            @Parameter(description = "ID do médico") @PathVariable Long id) {
        medicoService.excluirMedico(id);
        return ApiResponse.success("Médico excluído com sucesso");
    }
}
