package com.neobit.sugerencia.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neobit.sugerencia.datos.ComentarioRepository;
import com.neobit.sugerencia.negocio.modelo.Comentario;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;

@Service
/**
 * Servicio relacionado con los comentarios
 */
public class ServicioComentario {

    @Autowired
    ComentarioRepository comentarioRepository;

    /**
     * Agrega un nuevo comentario a una sugerencia
     * 
     * @param sugerencia La sugerencia a la que se va a agregar el comentario
     * @param comentario El comentario a agregar
     */
    public void agregarComentario(Sugerencia sugerencia, Comentario comentario) {
        comentario.setSugerencia(sugerencia);
        comentarioRepository.save(comentario);
    }

    /**
     * Edita un comentario existente
     * 
     * @param comentario El comentario a editar
     */
    public void editarComentario(Comentario comentario) {
        comentarioRepository.save(comentario);
    }

    /**
     * Elimina un comentario
     * 
     * @param comentario El comentario a eliminar
     */
    public void eliminarComentario(Comentario comentario) {
        comentarioRepository.delete(comentario);
    }

    public void guardar(Comentario comentario) {
        // Guardar el comentario en la base de datos
        comentarioRepository.save(comentario);
    }
}