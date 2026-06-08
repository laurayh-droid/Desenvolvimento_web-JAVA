package com.imepac.appointment.controller;

import com.imepac.commons.dto.*;
import com.imepac.commons.response.ApiResponse;
import com.imepac.appointment.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes da clínica")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar paciente")
    public ApiResponse<RespostaPaciente> cadastrarPaciente(@Valid @RequestBody CriarPacienteRequest request) {
        return ApiResponse.success(pacienteService.cadastrarPaciente(request), "Paciente cadastrado com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar pacientes")
    public ApiResponse<List<RespostaPaciente>> listarPacientes() {
        return ApiResponse.success(pacienteService.listarPacientes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por id")
    public ApiResponse<RespostaPaciente> buscarPacientePorId(@PathVariable Long id) {
        return ApiResponse.success(pacienteService.buscarPacientePorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar paciente")
    public ApiResponse<RespostaPaciente> atualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPacienteRequest request) {
        return ApiResponse.success(pacienteService.atualizarPaciente(id, request), "Paciente atualizado com sucesso");
    }
}
