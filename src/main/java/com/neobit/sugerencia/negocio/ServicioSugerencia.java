package com.neobit.sugerencia.negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neobit.sugerencia.datos.NotificacionesRepository;
import com.neobit.sugerencia.datos.SugerenciaRepository;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;

import jakarta.transaction.Transactional;

@Service
/**
 * Servicio relacionado con las sugerencias
 */
public class ServicioSugerencia {

    @Autowired
    SugerenciaRepository sugerenciaRepository;

    @Autowired
    NotificacionesRepository notificacionRepository;

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
    @Transactional
    public void agregaSugerencia(Sugerencia sugerencia) {
        System.out.println("Guardando sugerencia: " + sugerencia.getTitulo()); // Mensaje de depuración
        sugerenciaRepository.save(sugerencia);
        System.out.println("Sugerencia guardada correctamente.");
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

    /**
     * Recomienda una sugerencia y envía una notificación al autor.
     * 
     * @param id El ID de la sugerencia a recomendar
     */
    @Transactional
    public void recomendarSugerencia(Long id) {
        Sugerencia sugerencia = sugerenciaRepository.findById(id).orElse(null);
        if (sugerencia != null) {
            sugerencia.setRecomendada(true);
            sugerenciaRepository.save(sugerencia);
            enviarNotificacion(sugerencia.getAutor(),
                    "Tu sugerencia '" + sugerencia.getTitulo() + "' ha sido recomendada.");
        } else {
            throw new IllegalArgumentException("No se encontró la sugerencia con ID: " + id);
        }
    }

    /**
     * Envía una notificación a un autor.
     * 
     * @param autor   El nombre del autor de la sugerencia
     * @param mensaje El mensaje de la notificación
     */
    @Transactional
    public void enviarNotificacion(String autor, String mensaje) {
        Notificaciones notificacion = new Notificaciones();
        notificacion.setDestinatario(autor);
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
}