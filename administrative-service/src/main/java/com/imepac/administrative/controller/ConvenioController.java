package com.imepac.administrative.controller;

import com.imepac.administrative.service.ConvenioService;
import com.imepac.commons.dto.AtualizarConvenioRequest;
import com.imepac.commons.dto.CriarConvenioRequest;
import com.imepac.commons.dto.RespostaConvenio;
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
@RequestMapping("/api/v1/admin/convenios")
@RequiredArgsConstructor
@Tag(name = "Convênios", description = "Gerenciamento de convênios da clínica")
public class ConvenioController {

    private final ConvenioService convenioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar convênio")
    public ApiResponse<RespostaConvenio> cadastrarConvenio(@Valid @RequestBody CriarConvenioRequest request) {
        return ApiResponse.success(convenioService.cadastrarConvenio(request), "Convênio cadastrado com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar convênios")
    public ApiResponse<List<RespostaConvenio>> listarConvenios() {
        return ApiResponse.success(convenioService.listarConvenios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar convênio por id")
    public ApiResponse<RespostaConvenio> buscarConvenioPorId(
            @Parameter(description = "ID do convênio") @PathVariable Long id) {
        return ApiResponse.success(convenioService.buscarConvenioPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar convênio")
    public ApiResponse<RespostaConvenio> atualizarConvenio(
            @Parameter(description = "ID do convênio") @PathVariable Long id,
            @Valid @RequestBody AtualizarConvenioRequest request) {
        return ApiResponse.success(convenioService.atualizarConvenio(id, request), "Convênio atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir convênio")
    public ApiResponse<Void> excluirConvenio(
            @Parameter(description = "ID do convênio") @PathVariable Long id) {
        convenioService.excluirConvenio(id);
        return ApiResponse.success("Convênio excluído com sucesso");
    }
}
