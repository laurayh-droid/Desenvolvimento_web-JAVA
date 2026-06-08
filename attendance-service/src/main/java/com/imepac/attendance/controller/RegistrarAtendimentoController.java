package com.imepac.attendance.controller;

import com.imepac.attendance.service.RegistrarAtendimentoService;
import com.imepac.commons.dto.CriarAtendimentoRequest;
import com.imepac.commons.dto.RespostaAtendimento;
import com.imepac.commons.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/atendimentos")
@RequiredArgsConstructor
@Tag(name = "Atendimentos", description = "Registro de atendimentos clínicos")
public class RegistrarAtendimentoController {

    private final RegistrarAtendimentoService registrarAtendimentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar atendimento")
    public ApiResponse<RespostaAtendimento> registrarAtendimento(
            @Valid @RequestBody CriarAtendimentoRequest request) {
        return ApiResponse.success(registrarAtendimentoService.registrarAtendimento(request), "Atendimento registrado com sucesso");
    }
}
