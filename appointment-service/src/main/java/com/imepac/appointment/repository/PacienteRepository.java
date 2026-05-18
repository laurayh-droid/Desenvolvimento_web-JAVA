package com.imepac.appointment.repository;

import com.imepac.commons.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    boolean existsByRg(String rg);
    Optional<Paciente> findByRg(String rg);
}

