package com.neobit.sugerencia.datos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neobit.sugerencia.negocio.modelo.RespuestaForo;
import com.neobit.sugerencia.negocio.modelo.TemaForo;

@Repository
public interface RespuestaForoRepository extends JpaRepository<RespuestaForo, Long> {

    // Método para buscar respuestas por tema
    List<RespuestaForo> findByTemaForo(TemaForo temaForo);
}