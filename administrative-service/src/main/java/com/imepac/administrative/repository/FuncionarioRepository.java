package com.imepac.administrative.repository;

import com.imepac.commons.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByRg(String rg);
    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByRgAndIdNot(String rg, Long id);
}
