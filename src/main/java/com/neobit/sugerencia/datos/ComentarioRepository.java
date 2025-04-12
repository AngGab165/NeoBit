package com.neobit.sugerencia.datos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neobit.sugerencia.negocio.modelo.Comentario;

/**
 * Repositorio para Comentarios 2
 */
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    /**
     * Método para obtener todos los comentarios de una sugerencia
     *
     * @param idSugerencia ID de la sugerencia
     * @return Lista de comentarios asociados a la sugerencia
     */
    List<Comentario> findByIdSugerencia(Long idSugerencia);

}