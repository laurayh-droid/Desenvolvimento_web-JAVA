package com.imepac.administrative.repository;

import com.imepac.commons.entity.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    boolean existsByDescricao(String descricao);
    boolean existsByDescricaoAndIdNot(String descricao, Long id);
}
