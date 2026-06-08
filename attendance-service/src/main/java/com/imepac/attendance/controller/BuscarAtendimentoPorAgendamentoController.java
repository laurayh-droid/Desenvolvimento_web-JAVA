package com.imepac.attendance.controller;

import com.imepac.attendance.service.BuscarAtendimentoPorAgendamentoService;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/atendimentos")
@RequiredArgsConstructor
@Tag(name = "Atendimentos", description = "Busca de atendimento por agendamento")
public class BuscarAtendimentoPorAgendamentoController {

    private final BuscarAtendimentoPorAgendamentoService buscarAtendimentoPorAgendamentoService;

    @GetMapping("/agendamento/{agendamentoId}")
    @Operation(summary = "Buscar atendimento por agendamento")
    public ApiResponse<RespostaAtendimento> buscarPorAgendamento(
            @Parameter(description = "ID do agendamento") @PathVariable Long agendamentoId) {
        return ApiResponse.success(buscarAtendimentoPorAgendamentoService.buscarPorAgendamento(agendamentoId));
    }
}
