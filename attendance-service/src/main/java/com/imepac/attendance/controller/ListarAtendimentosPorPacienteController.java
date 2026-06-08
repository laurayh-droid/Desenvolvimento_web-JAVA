package com.imepac.attendance.controller;

import com.imepac.attendance.service.ListarAtendimentosPorPacienteService;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/atendimentos")
@RequiredArgsConstructor
@Tag(name = "Atendimentos", description = "Consulta de atendimentos por paciente")
public class ListarAtendimentosPorPacienteController {

    private final ListarAtendimentosPorPacienteService listarAtendimentosPorPacienteService;

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar atendimentos por paciente")
    public ApiResponse<List<RespostaAtendimento>> listarAtendimentosPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        return ApiResponse.success(listarAtendimentosPorPacienteService.listarAtendimentosPorPaciente(pacienteId));
    }
}
