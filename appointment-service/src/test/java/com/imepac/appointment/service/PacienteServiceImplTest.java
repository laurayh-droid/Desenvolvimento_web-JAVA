package com.imepac.appointment.service;

import com.imepac.appointment.repository.PacienteRepository;
import com.imepac.appointment.service.impl.PacienteServiceImpl;
import com.imepac.commons.dto.CriarPacienteRequest;
import com.imepac.commons.dto.AtualizarPacienteRequest;
import com.imepac.commons.entity.Paciente;
import com.imepac.commons.enums.Gender;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.PacienteNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    @Test
    void cadastrarPaciente_success() {
        CriarPacienteRequest req = CriarPacienteRequest.builder()
                .nomeCompleto("João Silva")
                .rg("12345")
                .cpf("11122233344")
                .dataNascimento(LocalDateTime.now().minusYears(30))
                .genero(Gender.MALE)
                .telefoneFixo("1111")
                .telefoneCelular("99999")
                .possuiSeguro(false)
                .rua("Rua A")
                .numero("10")
                .complemento("apt")
                .bairro("B")
                .cidade("C")
                .estado("SP")
                .cep("00000-000")
                .build();

        when(pacienteRepository.existsByCpf(req.getCpf())).thenReturn(false);
        when(pacienteRepository.existsByRg(req.getRg())).thenReturn(false);

        Paciente saved = Paciente.builder().id(1L).cpf(req.getCpf()).rg(req.getRg()).nomeCompleto(req.getNomeCompleto()).build();
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(saved);

        var resp = pacienteService.cadastrarPaciente(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void cadastrarPaciente_duplicateCpf_throws() {
        CriarPacienteRequest req = CriarPacienteRequest.builder()
                .nomeCompleto("X")
                .rg("rg")
                .cpf("cpf")
                .dataNascimento(LocalDateTime.now().minusYears(20))
                .genero(Gender.MALE)
                .telefoneFixo("t")
                .telefoneCelular("t")
                .possuiSeguro(false)
                .rua("r")
                .numero("n")
                .complemento("c")
                .bairro("b")
                .cidade("c")
                .estado("SP")
                .cep("cep")
                .build();

        when(pacienteRepository.existsByCpf(req.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> pacienteService.cadastrarPaciente(req))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void cadastrarPaciente_missingSeguroName_throws() {
        CriarPacienteRequest req = CriarPacienteRequest.builder()
                .nomeCompleto("X")
                .rg("rg")
                .cpf("cpf")
                .dataNascimento(LocalDateTime.now().minusYears(20))
                .genero(Gender.MALE)
                .telefoneFixo("t")
                .telefoneCelular("t")
                .possuiSeguro(true)
                .nomeEmpresaSeguro(null)
                .rua("r")
                .numero("n")
                .complemento("c")
                .bairro("b")
                .cidade("c")
                .estado("SP")
                .cep("cep")
                .build();

        when(pacienteRepository.existsByCpf(req.getCpf())).thenReturn(false);
        when(pacienteRepository.existsByRg(req.getRg())).thenReturn(false);

        assertThatThrownBy(() -> pacienteService.cadastrarPaciente(req))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void buscarPacientePorId_notFound_throws() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.buscarPacientePorId(1L))
                .isInstanceOf(PacienteNaoEncontradoException.class);
    }

    @Test
    void atualizarPaciente_updatesFields() {
        Paciente existing = Paciente.builder().id(1L).nomeCompleto("Old").build();
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(existing));

        AtualizarPacienteRequest req = AtualizarPacienteRequest.builder()
                .nomeCompleto("New")
                .build();

        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        var resp = pacienteService.atualizarPaciente(1L, req);

        assertThat(resp.getNomeCompleto()).isEqualTo("New");
        verify(pacienteRepository).save(any(Paciente.class));
    }
}

