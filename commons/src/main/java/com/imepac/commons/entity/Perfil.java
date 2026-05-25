package com.imepac.commons.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;
}
