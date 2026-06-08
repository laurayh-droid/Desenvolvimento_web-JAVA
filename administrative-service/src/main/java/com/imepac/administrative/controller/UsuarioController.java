package com.imepac.administrative.controller;

import com.imepac.administrative.service.UsuarioService;
import com.imepac.commons.dto.AtualizarUsuarioRequest;
import com.imepac.commons.dto.CriarUsuarioRequest;
import com.imepac.commons.dto.RespostaUsuario;
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
@RequestMapping("/api/v1/admin/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários administrativos")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar usuário")
    public ApiResponse<RespostaUsuario> cadastrarUsuario(@Valid @RequestBody CriarUsuarioRequest request) {
        return ApiResponse.success(usuarioService.cadastrarUsuario(request), "Usuário cadastrado com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    public ApiResponse<List<RespostaUsuario>> listarUsuarios() {
        return ApiResponse.success(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por id")
    public ApiResponse<RespostaUsuario> buscarUsuarioPorId(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        return ApiResponse.success(usuarioService.buscarUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ApiResponse<RespostaUsuario> atualizarUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioRequest request) {
        return ApiResponse.success(usuarioService.atualizarUsuario(id, request), "Usuário atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário")
    public ApiResponse<Void> excluirUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ApiResponse.success("Usuário excluído com sucesso");
    }
}
