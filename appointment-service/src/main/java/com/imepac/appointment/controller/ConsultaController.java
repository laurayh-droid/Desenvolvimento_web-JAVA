package com.imepac.appointment.controller;

import com.imepac.commons.dto.*;
import com.imepac.commons.response.ApiResponse;
import com.imepac.appointment.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Gerenciamento de consultas e agendamentos")
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agendar consulta")
    public ApiResponse<RespostaAgendamento> agendarConsulta(@Valid @RequestBody CriarAgendamentoRequest request) {
        return ApiResponse.success(consultaService.agendarConsulta(request), "Consulta agendada com sucesso");
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar consultas por paciente")
    public ApiResponse<List<RespostaAgendamento>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {
        return ApiResponse.success(consultaService.listarConsultasPorPaciente(pacienteId));
    }

    @GetMapping("/medico/disponibilidade")
    @Operation(summary = "Encontrar horários disponíveis para o médico")
    public ApiResponse<List<RespostaAgendamento>> encontrarDisponibilidade(
            @RequestParam Long medicoId,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ApiResponse.success(consultaService.encontrarDisponibilidade(medicoId, inicio, fim));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar consulta")
    public ApiResponse<RespostaAgendamento> cancelarConsulta(
            @PathVariable Long id,
            @Valid @RequestBody CancelarAgendamentoRequest request) {
        return ApiResponse.success(consultaService.cancelarConsulta(id, request), "Consulta cancelada com sucesso");
    }

    @PostMapping("/{id}/retorno")
    @Operation(summary = "Agendar retorno")
    public ApiResponse<RespostaAgendamento> agendarRetorno(
            @PathVariable Long id,
            @Valid @RequestBody AgendarRetornoRequest request) {
        return ApiResponse.success(consultaService.agendarRetorno(id, request), "Retorno agendado com sucesso");
    }

    @PutMapping("/{id}/prontuario")
    @Operation(summary = "Registrar atendimento/prontuário")
    public ApiResponse<RespostaAgendamento> registrarProntuario(
            @PathVariable Long id,
            @RequestBody String prontuario) {
        return ApiResponse.success(consultaService.registrarProntuario(id, prontuario), "Prontuário registrado com sucesso");
    }
}
