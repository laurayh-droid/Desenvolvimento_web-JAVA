package com.imepac.attendance.repository;

import com.imepac.commons.entity.Atendimento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    Optional<Atendimento> findByAgendamentoId(Long agendamentoId);

    List<Atendimento> findAllByPacienteIdOrderByRegistradoEmAsc(Long pacienteId);
}

