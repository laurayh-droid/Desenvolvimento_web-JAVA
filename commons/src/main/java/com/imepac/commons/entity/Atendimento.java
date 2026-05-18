package com.imepac.commons.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "atendimentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agendamento_id", nullable = false)
    private Long agendamentoId;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registradoEm;

    @Column(name = "diagnostico", length = 1000)
    private String diagnostico;

    @Column(name = "observacoes", length = 2000)
    private String observacoes;

    @Column(name = "prontuario", length = 10000)
    private String prontuario;

    @Column(name = "receituario", length = 10000)
    private String receituario;

    @Column(name = "exames_solicitados", length = 10000)
    private String examesSolicitados;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    void prePersist() {
        LocalDateTime agora = LocalDateTime.now();
        criadoEm = agora;
        if (registradoEm == null) {
            registradoEm = agora;
        }
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}

