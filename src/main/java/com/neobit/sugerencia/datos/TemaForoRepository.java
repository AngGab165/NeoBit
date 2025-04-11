package com.neobit.sugerencia.datos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neobit.sugerencia.negocio.modelo.TemaForo;

@Repository
public interface TemaForoRepository extends JpaRepository<TemaForo, Long> {
}