package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.Mesa;
import com.costasruben.ovellanegra.enums.EstadoMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByNumero(Integer numero);
    List<Mesa> findByEstado(EstadoMesa estado);
}
