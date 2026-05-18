package com.imepac.attendance.controller;

import com.imepac.commons.dto.*;

import com.imepac.attendance.service.AtendimentoService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Atendimentos", description = "Registro e consulta de atendimentos clínicos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    @PostMapping("/atendimentos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar atendimento")
    public ApiResponse<RespostaAtendimento> registrarAtendimento(
            @Valid @RequestBody CriarAtendimentoRequest request) {
        return ApiResponse.success(atendimentoService.registrarAtendimento(request), "Atendimento registrado com sucesso");
    }

    @GetMapping("/atendimentos/paciente/{pacienteId}")
    @Operation(summary = "Listar atendimentos por paciente")
    public ApiResponse<List<RespostaAtendimento>> listarAtendimentosPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        return ApiResponse.success(atendimentoService.listarAtendimentosPorPaciente(pacienteId));
    }

    @GetMapping("/atendimentos/agendamento/{agendamentoId}")
    @Operation(summary = "Buscar atendimento por agendamento")
    public ApiResponse<RespostaAtendimento> buscarPorAgendamento(
            @Parameter(description = "ID do agendamento") @PathVariable Long agendamentoId) {
        return ApiResponse.success(atendimentoService.buscarPorAgendamento(agendamentoId));
    }
}

