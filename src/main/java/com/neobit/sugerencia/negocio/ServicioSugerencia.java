package com.neobit.sugerencia.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neobit.sugerencia.datos.SugerenciaRepository;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;

@Service
/**
 * Servicio relacionado con las sugerencias
 */
public class ServicioSugerencia {

    @Autowired
    SugerenciaRepository sugerenciaRepository;

    /**
     * Recupera todas las sugerencias
     * 
     * @return lista de sugerencias
     */
    public List<Sugerencia> recuperaSugerencias() {
        List<Sugerencia> sugerencias = new ArrayList<>();
        for (Sugerencia sugerencia : sugerenciaRepository.findAll()) {
            sugerencias.add(sugerencia);
        }
        return sugerencias;
    }

    /**
     * Agrega una nueva sugerencia
     * 
     * @param sugerencia La sugerencia a agregar
     */
    public void agregaSugerencia(Sugerencia sugerencia) {
        sugerenciaRepository.save(sugerencia);
    }

    /**
     * Elimina una sugerencia
     * 
     * @param sugerencia La sugerencia a eliminar
     */
    public void eliminaSugerencia(Sugerencia sugerencia) {
        sugerenciaRepository.delete(sugerencia);
    }

    /**
     * Actualiza el estado de una sugerencia.
     * 
     * @param sugerencia La sugerencia a actualizar
     */
    public void actualizaSugerencia(Sugerencia sugerencia) {
        sugerenciaRepository.save(sugerencia);
    }

    /**
     * Busca una sugerencia por su ID.
     * 
     * @param id El ID de la sugerencia a buscar
     * @return La sugerencia encontrada, o null si no existe
     */
    public Sugerencia buscaSugerenciaPorId(Long id) {
        return sugerenciaRepository.findById(id).orElse(null);
    }
}