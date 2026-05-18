package com.imepac.commons.entity;

import com.imepac.commons.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "agendado_em", nullable = false)
    private LocalDateTime agendadoEm;

    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    @Column(name = "motivo_cancelamento", length = 500)
    private String motivoCancelamento;

    @Column(name = "senha_cancelamento_hash", length = 255)
    private String senhaCancelamentoHash;

    @Column(name = "agendamento_retorno_em")
    private LocalDateTime agendamentoRetornoEm;

    @Column(name = "prontuario", columnDefinition = "TEXT")
    private String prontuario;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
        if (status == null) {
            status = StatusAgendamento.AGENDADO;
        }
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
