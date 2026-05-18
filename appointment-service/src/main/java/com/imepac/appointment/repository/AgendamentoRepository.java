package com.imepac.appointment.repository;

import com.imepac.commons.entity.Agendamento;
import com.imepac.commons.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findById(Long id);

    List<Agendamento> findAllByPacienteId(Long pacienteId);

    List<Agendamento> findAllByMedicoIdAndAgendadoEmBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByPacienteIdAndAgendadoEm(Long pacienteId, LocalDateTime agendadoEm);

    boolean existsByMedicoIdAndAgendadoEmAndStatusNot(Long medicoId, LocalDateTime agendadoEm, StatusAgendamento excludedStatus);

    List<Agendamento> findAllByPacienteIdAndStatusNot(Long pacienteId, StatusAgendamento excludedStatus);

    boolean existsByMedicoIdAndAgendadoEmAndStatusNotIn(Long medicoId, LocalDateTime agendadoEm, List<StatusAgendamento> excludedStatuses);
}

