package com.imepac.appointment.controller;

import com.imepac.appointment.dto.*;

import com.imepac.appointment.service.AgendamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.imepac.commons.response.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos da clínica")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping("/pacientes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar paciente")
    public ApiResponse<RespostaPaciente> cadastrarPaciente(@Valid @RequestBody CriarPacienteRequest request) {
        return ApiResponse.success(agendamentoService.cadastrarPaciente(request), "Paciente cadastrado com sucesso");
    }

    @GetMapping("/pacientes")
    @Operation(summary = "Listar pacientes")
    public ApiResponse<List<RespostaPaciente>> listarPacientes() {
        return ApiResponse.success(agendamentoService.listarPacientes());
    }

    @GetMapping("/pacientes/{id}")
    @Operation(summary = "Buscar paciente por id")
    public ApiResponse<RespostaPaciente> buscarPacientePorId(
            @Parameter(description = "ID do paciente") @PathVariable Long id) {
        return ApiResponse.success(agendamentoService.buscarPacientePorId(id));
    }

    @PutMapping("/pacientes/{id}")
    @Operation(summary = "Atualizar paciente")
    public ApiResponse<RespostaPaciente> atualizarPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long id,
            @Valid @RequestBody AtualizarPacienteRequest request) {
        return ApiResponse.success(agendamentoService.atualizarPaciente(id, request), "Paciente atualizado com sucesso");
    }

    @PostMapping("/consultas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agendar consulta")
    public ApiResponse<RespostaAgendamento> agendarConsulta(@Valid @RequestBody CriarAgendamentoRequest request) {
        return ApiResponse.success(agendamentoService.agendarConsulta(request), "Consulta agendada com sucesso");
    }

    @GetMapping("/consultas/paciente/{pacienteId}")
    @Operation(summary = "Listar consultas por paciente")
    public ApiResponse<List<RespostaAgendamento>> listarConsultasPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        return ApiResponse.success(agendamentoService.listarConsultasPorPaciente(pacienteId));
    }

    @GetMapping("/consultas/medico/disponibilidade")
    @Operation(summary = "Encontrar horários disponíveis para o médico")
    public ApiResponse<List<RespostaAgendamento>> encontrarDisponibilidade(
            @RequestParam Long medicoId,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ApiResponse.success(agendamentoService.encontrarDisponibilidade(medicoId, inicio, fim));
    }

    @PostMapping("/consultas/{id}/cancelar")
    @Operation(summary = "Cancelar consulta")
    public ApiResponse<RespostaAgendamento> cancelarConsulta(
            @PathVariable Long id,
            @Valid @RequestBody CancelarAgendamentoRequest request) {
        return ApiResponse.success(agendamentoService.cancelarConsulta(id, request), "Consulta cancelada com sucesso");
    }

    @PostMapping("/consultas/{id}/retorno")
    @Operation(summary = "Agendar retorno")
    public ApiResponse<RespostaAgendamento> agendarRetorno(
            @PathVariable Long id,
            @Valid @RequestBody AgendarRetornoRequest request) {
        return ApiResponse.success(agendamentoService.agendarRetorno(id, request), "Retorno agendado com sucesso");
    }

    @PutMapping("/consultas/{id}/prontuario")
    @Operation(summary = "Registrar atendimento/prontuário")
    public ApiResponse<RespostaAgendamento> registrarProntuario(
            @PathVariable Long id,
            @RequestBody String prontuario) {
        return ApiResponse.success(agendamentoService.registrarProntuario(id, prontuario), "Prontuário registrado com sucesso");
    }
}

