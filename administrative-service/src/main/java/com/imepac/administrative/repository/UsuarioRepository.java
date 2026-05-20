package com.imepac.administrative.repository;

import com.imepac.commons.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByIdUser(String idUser);
    boolean existsByIdUserAndIdNot(String idUser, Long id);
}
