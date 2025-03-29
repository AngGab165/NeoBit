package com.neobit.sugerencia.datos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neobit.sugerencia.negocio.modelo.Comentario;

/**
 * Repositorio para Comentarios 2
 */
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}