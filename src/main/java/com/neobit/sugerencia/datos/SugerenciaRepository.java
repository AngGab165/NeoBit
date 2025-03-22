package com.neobit.sugerencia.datos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neobit.sugerencia.negocio.modelo.Sugerencia;

/**
 * Repositorio para Sugerencias
 */
@Repository
public interface SugerenciaRepository extends JpaRepository<Sugerencia, Long> {

    /**
     * Encuentra una sugerencia a partir de un título
     * 
     * @param titulo
     * @return
     */
    public Sugerencia findByTitulo(String titulo);
}