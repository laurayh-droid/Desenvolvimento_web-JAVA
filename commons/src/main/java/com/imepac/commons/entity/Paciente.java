package com.imepac.commons.entity;

import com.imepac.commons.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 200)
    private String nomeCompleto;

    @Column(name = "rg", nullable = false, unique = true, length = 30)
    private String rg;

    @Column(name = "cpf", nullable = false, unique = true, length = 30)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDateTime dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender genero;


    @Column(name = "telefone", length = 30)
    private String telefone;

    @Column(name = "telefone_fixo", length = 30)
    private String telefoneFixo;

    @Column(name = "telefone_celular", length = 30)
    private String telefoneCelular;

    @Column(name = "has_seguro", nullable = false)
    private boolean possuiSeguro;

    @Column(name = "rua", length = 200)
    private String rua;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "cep", length = 20)
    private String cep;

    @Column(name = "nome_empresa_seguro", length = 200)
    private String nomeEmpresaSeguro;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
