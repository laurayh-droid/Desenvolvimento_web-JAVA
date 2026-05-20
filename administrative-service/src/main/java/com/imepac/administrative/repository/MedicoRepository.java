package com.imepac.administrative.repository;

import com.imepac.commons.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    boolean existsByCrm(String crm);
    boolean existsByCrmAndIdNot(String crm, Long id);
}
